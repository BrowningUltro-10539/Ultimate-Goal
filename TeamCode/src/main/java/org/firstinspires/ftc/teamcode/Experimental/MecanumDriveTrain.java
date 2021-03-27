package org.firstinspires.ftc.teamcode.Experimental;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import static java.lang.Math.PI;

public class MecanumDriveTrain {

    private DeviceMap map;
    private DcMotor leftTop;
    private DcMotor rightTop;
    private DcMotor leftBottom;
    private DcMotor rightBottom;

    private LinearOpMode opMode;

    public double x;
    public double y;
    public double theta;
    private double deltaHeading = 0;

    /* Odometry */
    public double pod1 = 0;
    public double pod2 = 0;
    public double pod3 = 0;
    private double lastpod1 = 0;
    private double lastpod2 = 0;
    private double lastpod3 = 0;
    private double deltapod1;
    private double deltapod2;
    private double deltapod3;

    /* Odometry Constants */
    public static double ticksToInch1 = 0.00588046031;
    public static double ticksToInch2 = 0.00582564639;
    public static double ticksToInch3 = 0.00583427502;
    public static double OdometryTrackWidth = 13.565;
    public static double OdometryHorizontalOffset = -2.845;
    private final double OdometryHeadingThreshold = PI/8;
    public int zero1, zero2, zero3;


    /* Motor Caching */
    private double lastRTPower = 0;
    private double lastRBPower = 0;
    private double lastLTPower = 0;
    private double lastLBPower = 0;
    private final double motorUpdateTolerance = 0.05;

    /* Other Constants */
    public final static double xKp = 0.5;
    public final static double yKp = 0.5;
    public final static double thetaKp = 4.0;
    public final static double xKd = 0.047;
    public final static double yKd = 0.047;
    public final static double thetaKd = 0.15;

    public boolean zeroStrafeCorrection = false;

    public MecanumDriveTrain(LinearOpMode opMode, double initialX, double initialY, double initialTheta){
        this.opMode = opMode;
        HardwareMap hardwareMap = opMode.hardwareMap;

        leftTop = map.getLeftTop();
        rightTop = map.getRightTop();
        leftBottom = map.getLeftBottom();
        rightBottom = map.getRightBottom();

        map.setUpDriveMotors(hardwareMap);

        x = initialX;
        y = initialY;
        theta = initialTheta;
    }

    public void resetOdo(double newX, double newY, double newTheta){
        x = newX;
        y = newY;
        theta = newTheta;
    }

    /* Robot Centric Movement  */
    public void RCControls(double xDot, double yDot, double w){
        double LTpower;
        double RTpower;
        double LBpower;
        double RBpower;

        if(!zeroStrafeCorrection){
            RTpower = yDot + xDot + w;
            LBpower = yDot + xDot - w;
            LTpower = -yDot + xDot - w;
            RBpower = -yDot + xDot + w;
        } else {
            RTpower = xDot + w;
            LBpower = xDot - w;
            LTpower = xDot - w;
            RBpower = xDot + w;
        }

        double maxPower = Math.max(Math.abs(RTpower), Math.max(Math.abs(LBpower), Math.max(Math.abs(LTpower), Math.abs(RBpower))));

        if (maxPower > 1) {
            RTpower /= maxPower;
            LBpower /= maxPower;
            LTpower /= maxPower;
            RBpower /= maxPower;
        }

        if(xDot == 0 && yDot == 0 && w == 0) {
            leftTop.setPower(LBpower);
            rightTop.setPower(RTpower);
            leftBottom.setPower(LTpower);
            rightBottom.setPower(RBpower);

            lastRTPower = RTpower;
            lastRBPower = RBpower;
            lastLTPower = LTpower;
            lastLBPower = RBpower;
        } else if (Math.abs(RTpower - lastRTPower) > motorUpdateTolerance || Math.abs(LTpower - lastLTPower) > motorUpdateTolerance ||
                Math.abs(RBpower - lastRBPower) > motorUpdateTolerance || Math.abs(LBpower = lastLBPower) > motorUpdateTolerance) {
            leftTop.setPower(LTpower);
            rightTop.setPower(RTpower);
            leftBottom.setPower(LBpower);
            rightBottom.setPower(RBpower);

            lastLTPower = LTpower;
            lastRTPower = RTpower;
            lastRBPower = RBpower;
            lastLBPower = LBpower;
        }
    }

    public void FCControls(double xvelocity, double yvelocity, double w){
        double xdot = xvelocity * Math.cos(-theta) - yvelocity * Math.sin(-theta);
        double ydot = yvelocity * Math.cos(-theta) + xvelocity * Math.sin(-theta);
        RCControls(xdot, ydot, w);
    }

    public void stop(){
        RCControls(0,0,0);
    }

    /* Updating Position from Odometry */
    public void updatePose(){
        try {
            pod1 = rightTop.getCurrentPosition() * -ticksToInch1;
            pod2 = rightBottom.getCurrentPosition() * ticksToInch2;
            pod3 = leftBottom.getCurrentPosition() * ticksToInch3;

            deltapod1 = pod1 - lastpod1;
            deltapod2 = pod2 - lastpod2;
            deltapod3 = pod3 - lastpod3;

            if(!(deltapod1 == 0 && deltapod2 == 0 && deltapod3 == 0)) {
                if (deltapod1 == 0) {
                    Log.w("Pod-Delta-Log", "pod1 delta 0 ");
                    zero1++;
                }

                if (deltapod2 == 0) {
                    Log.w("Pod-Delta-Log", "pod2 delta 0");
                    zero2++;
                }

                if (deltapod3 == 0) {
                    Log.w("Pod-Delta-Log", "pod3 delta 0");
                    zero3++;
                }
            }

            deltaHeading = (deltapod2 - deltapod1) / OdometryTrackWidth;

            double localX = (deltapod1 + deltapod2) / 2;
            double localY = deltapod3 - deltaHeading * OdometryHorizontalOffset;

            if(deltaHeading < OdometryHeadingThreshold) {
                x += localX * Math.cos(theta) - localY * Math.sin(theta);
                y += localY * Math.cos(theta) + localX * Math.sin(theta);
            } else {
                x += (localX * Math.sin(theta + deltaHeading) + localY * Math.cos(theta + deltaHeading)
                        - localX * Math.sin(theta) - localY * Math.cos(theta)) / deltaHeading;
                y += (localY * Math.sin(theta + deltaHeading) - localX * Math.cos(theta + deltaHeading)
                        - localY * Math.sin(theta) + localX * Math.cos(theta)) / deltaHeading;
            }

            theta += deltaHeading;
            theta = theta % (PI * 2);

            if (theta < 0) theta += PI * 2;

            lastpod1 = pod1;
            lastpod2 = pod2;
            lastpod3 = pod3;
        } catch (Exception e) { e.printStackTrace(); }

    }









}
