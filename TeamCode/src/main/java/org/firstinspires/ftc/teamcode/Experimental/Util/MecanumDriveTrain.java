package org.firstinspires.ftc.teamcode.Experimental.Util;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

import static java.lang.Math.PI;

public class MecanumDriveTrain {

    /* TODO: CHANGE THE VALUES: TOO HIGH*/

    private DcMotorEx rightTop;
    private DcMotorEx leftTop;
    private DcMotorEx rightBottom;
    private DcMotorEx leftBottom;

    // OpMode
    private LinearOpMode opMode;

    // Tracking X/Y/Theta
    public double x;
    public double y;
    public double theta;
    private double deltaHeading = 0;

    // Odometry
//    public double pod1 = 0;
    public double pod2 = 0;
    public double pod3 = 0;
    //    private double lastPod1 = 0;
    private double lastPod2 = 0;
    private double lastPod3 = 0;
    private double deltaPod1;
    private double deltaPod2;
    private double deltaPod3;

    // Motor Caching
    private double lastRTPower = 0;
    private double lastRBPower = 0;
    private double lastLTPower = 0;
    private double lastLBPower = 0;
    private final double motorUpdateTolerance = 0.05;

    // Odometry constants
//    public static double ticksToInch1 = 0.00597622428;
    public static double ticksToInch2 = 0.00597622428;
    public static double ticksToInch3 = 0.00596020226;
    public static double ticksToCm2 = ticksToInch2 * 2.54;
    public static double ticksToCm3 = ticksToInch3 * 2.54;
    public static double ODOMETRY_TRACK_WIDTH = 13.655 * 2.54;
    public static double ODOMETRY_HORIZONTAL_OFFSET = -1.81;
    private final double ODOMETRY_HEADING_THRESHOLD = PI/8;

    // PD controller constants
    public final static double xKp = 0.53;
    public final static double yKp = 0.55;
    public final static double thetaKp = 2.0;
    public final static double xKd = 0.04;
    public final static double yKd = 0.04;
    public final static double thetaKd = 0.05;

    // Odometry delta 0 counters
    public int zero1, zero2, zero3;

    public boolean zeroStrafeCorrection = false;

//    private DcMotorEx intake;
//    private DcMotorEx intake2;

    private IMU imu;

    // Constructor
    public MecanumDriveTrain(LinearOpMode opMode, double initialX, double initialY, double initialTheta) {
        this.opMode = opMode;
        HardwareMap hardwareMap = opMode.hardwareMap;

        imu = new IMU(initialTheta, opMode);

        rightTop = hardwareMap.get(DcMotorEx.class, "RT");
        leftTop = hardwareMap.get(DcMotorEx.class, "LT");
        rightBottom = hardwareMap.get(DcMotorEx.class, "RB");
        leftBottom = hardwareMap.get(DcMotorEx.class, "LB");

//        intake = hardwareMap.get(DcMotorEx.class, "intake");
//        intake2 = hardwareMap.get(DcMotorEx.class, "intake2");

        leftTop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightTop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBottom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBottom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        intake2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftTop.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightTop.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBottom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBottom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        leftTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightTop.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBottom.setDirection(DcMotorSimple.Direction.REVERSE);

        x = initialX;
        y = initialY;
        theta = initialTheta;
    }

    // reset odometry
    public void resetOdo(double newX, double newY, double newTheta) {
        x = newX;
        y = newY;
        theta = newTheta;
        imu.resetHeading(newTheta);
    }

    // robot centric movement
    public void setControls(double xdot, double ydot, double w) {
        double FRpower, FLpower, BRpower, BLpower;

        if (!zeroStrafeCorrection) {
            FRpower = ydot + xdot + w;
            FLpower = -ydot + xdot - w;
            BRpower = -ydot + xdot + w;
            BLpower = ydot + xdot - w;
        } else {
            FRpower = xdot + w;
            FLpower = xdot - w;
            BRpower = xdot + w;
            BLpower = xdot - w;
        }

        double maxpower = Math.max(Math.abs(FRpower), Math.max(Math.abs(FLpower), Math.max(Math.abs(BRpower), Math.abs(BLpower))));

        if (maxpower > 1) {
            FRpower /= maxpower;
            FLpower /= maxpower;
            BRpower /= maxpower;
            BLpower /= maxpower;
        }

        if (xdot == 0 && ydot == 0 && w == 0) {
            // Set Motor Powers
            rightTop.setPower(FRpower);
            leftTop.setPower(FLpower);
            rightBottom.setPower(BRpower);
            leftBottom.setPower(BLpower);

            // Cache New Motor Powers
            lastRTPower = FRpower;
            lastLTPower = FLpower;
            lastRBPower = BRpower;
            lastLBPower = BLpower;

        } else if (Math.abs(FRpower - lastRTPower) > motorUpdateTolerance || Math.abs(FLpower - lastLTPower) > motorUpdateTolerance
                || Math.abs(BRpower - lastRBPower) > motorUpdateTolerance || Math.abs(BLpower - lastLBPower) > motorUpdateTolerance) {

            // Set Motor Powers
            rightTop.setPower(FRpower);
            leftTop.setPower(FLpower);
            rightBottom.setPower(BRpower);
            leftBottom.setPower(BLpower);

            // Cache New Motor Powers
            lastRTPower = FRpower;
            lastLTPower = FLpower;
            lastRBPower = BRpower;
            lastLBPower = BLpower;
        }
    }

    public void setRawPower(double frontRight, double frontLeft, double backRight, double backLeft) {
        // Set Motor Powers
        rightTop.setPower(frontRight);
        leftTop.setPower(frontLeft);
        rightBottom.setPower(backRight);
        leftBottom.setPower(backLeft);

        // Cache New Motor Powers
        lastRTPower = frontRight;
        lastLTPower = frontLeft;
        lastRBPower = backRight;
        lastLBPower = backLeft;
    }

    // field centric movement
    public void setGlobalControls(double xvelocity, double yvelocity, double w) {
        double xdot = xvelocity * Math.cos(-theta) - yvelocity * Math.sin(-theta);
        double ydot = yvelocity * Math.cos(-theta) + xvelocity * Math.sin(-theta);
        setControls(xdot, ydot, w);
    }

    // stop drivetrain
    public void stop() {
        setGlobalControls(0, 0, 0);
    }

    // update position from odometry
    public void updatePose() {
        try {
//            pod1 = motorFrontLeft.getCurrentPosition() * ticksToInch1;
            pod2 = rightBottom.getCurrentPosition() * ticksToCm2;
            pod3 = leftTop.getCurrentPosition() * -ticksToCm3;

//            deltaPod1 = pod1 - lastPod1;
            deltaPod2 = pod2 - lastPod2;
            deltaPod3 = pod3 - lastPod3;

//            deltaHeading = (deltaPod2 - deltaPod1) / ODOMETRY_TRACK_WIDTH;

            imu.updateHeading();
            theta = imu.getTheta();
            deltaHeading = imu.getDeltaHeading();

            deltaPod1 = deltaPod2 - deltaHeading * ODOMETRY_TRACK_WIDTH;

            if (!(deltaPod1 == 0 && deltaPod2 == 0 && deltaPod3 == 0)) {
                if (deltaPod1 == 0) {
                    Log.w("pod-delta-log", "pod1 delta 0");
                    zero1++;
                }
                if (deltaPod2 == 0) {
                    Log.w("pod-delta-log", "pod2 delta 0");
                    zero2++;
                }
                if (deltaPod3 == 0) {
                    Log.w("pod-delta-log", "pod3 delta 0");
                    zero3++;
                }
            }

            double localX = (deltaPod1 + deltaPod2) / 2;
            double localY = deltaPod3 - deltaHeading * ODOMETRY_HORIZONTAL_OFFSET;

//            Robot.log(deltaPod1 + " " + deltaPod2 + " " + deltaPod3 + " " + deltaHeading);

            if (deltaHeading < ODOMETRY_HEADING_THRESHOLD) {
                x += localX * Math.cos(theta) - localY * Math.sin(theta);
                y += localY * Math.cos(theta) + localX * Math.sin(theta);

            } else {
                x += (localX * Math.sin(theta + deltaHeading) + localY * Math.cos(theta + deltaHeading)
                        - localX * Math.sin(theta) - localY * Math.cos(theta)) / deltaHeading;
                y += (localY * Math.sin(theta + deltaHeading) - localX * Math.cos(theta + deltaHeading)
                        - localY * Math.sin(theta) + localX * Math.cos(theta)) / deltaHeading;
            }

//            theta += deltaHeading;
//            theta = theta % (2*PI);
//            if (theta < 0) theta += 2*PI;

//            lastPod1 = pod1;
            lastPod2 = pod2;
            lastPod3 = pod3;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}