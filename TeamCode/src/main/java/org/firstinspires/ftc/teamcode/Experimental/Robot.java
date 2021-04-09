package org.firstinspires.ftc.teamcode.Experimental;

import android.annotation.SuppressLint;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.lynx.LynxModule;

import org.firstinspires.ftc.teamcode.Experimental.Debugging.Logger;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Pose;
import org.firstinspires.ftc.teamcode.Experimental.Vision.Ring;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Path;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.PI;

public class Robot {
    public MecanumDriveTrain driveTrain;
    public Logger logger;

    private List<LynxModule> allHubs;
    private ElapsedTime profiler;
    private VoltageSensor battery;
    private boolean startVoltTooLow = false;

    private final int loggerUpdatePeriod = 2;
    private final int sensorUpdatePeriod = 15;
    private final double xyTolerance = 1;
    private final double thetaTolerance = PI/35;
    private double odoCovariance = 1;

    private final boolean isAuto;

    public double x, y, theta, vx, vy, w;
    private double prevX, prevY, prevTheta, prevVx, prevVy, prevW, prevTime, ax, ay, a;
    public double startTime;

    public ArrayList<Ring> shotRings = new ArrayList<>();
    public ArrayList<Ring> ringPos = new ArrayList<>();

    private LinearOpMode op;

    public Robot(LinearOpMode op, double x, double y, double theta, boolean isAuto) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.op = op;
        this.isAuto = isAuto;

        driveTrain = new MecanumDriveTrain(op, x, y, theta);
        /* Create classes that let you run specific functions for intake and wobble goal */
        logger = new Logger();
        profiler = new ElapsedTime();

        allHubs = op.hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        battery = op.hardwareMap.voltageSensor.iterator().next();
        log("Battery Voltage: " + battery.getVoltage() + "v");

        /* 12.4V is value that can be tested; I do believe that we can go
           lower but I have noticed the best performance with 12.4V + */
        if(battery.getVoltage() < 12.4) { startVoltTooLow = true; }
    }

    public void stop(){
        logger.stopLogging();
    }

    /* Similar to the initializeCords system Eric developed */
    public void resetOdo(double x, double y, double theta){
        driveTrain.resetOdo(x, y, theta);
    }

    /* Odometry Functions */

    public void setTargetPoint(double xTarget, double yTarget, double thetaTarget, double vxTarget, double vyTarget, double wTarget, double xKp, double yKp, double thetaKp, double xKd, double yKd, double thetaKd) {
        // Make Sure thetaTarget is Between 0 and 2pi
        thetaTarget = thetaTarget % (2*PI);
        if (thetaTarget < 0) {
            thetaTarget += 2*PI;
        }
        // Picking the Smaller Distance to Rotate
        double thetaControl;
        if (Math.abs(theta - thetaTarget) > PI) {
            thetaControl = Math.abs(theta - thetaTarget) / (theta - thetaTarget) * (Math.abs(theta - thetaTarget) - 2*PI);
        } else {
            thetaControl = theta - thetaTarget;
        }

        driveTrain.FCControls(xKp * (xTarget - x) + xKd * (vxTarget - vx), yKp * (yTarget - y) + yKd * (vyTarget - vy), thetaKp * (-thetaControl) + thetaKd * (wTarget - w));
    }

    // Set target point (default Kp and Kv gains)
    public void setTargetPoint(double xTarget, double yTarget, double thetaTarget){
        setTargetPoint(xTarget, yTarget, thetaTarget, 0,0,0, MecanumDriveTrain.xKp, MecanumDriveTrain.yKp, MecanumDriveTrain.thetaKp, MecanumDriveTrain.xKd, MecanumDriveTrain.yKd, MecanumDriveTrain.thetaKd );
    }
    // Set target point (using pose, velocity specification, default Kp and Kv gains)
    public void setTargetPoint(Pose pose) {
        setTargetPoint(pose.x, pose.y, pose.theta, pose.vx, pose.vy, pose.w, MecanumDriveTrain.xKp, MecanumDriveTrain.yKp, MecanumDriveTrain.thetaKp, MecanumDriveTrain.xKd, MecanumDriveTrain.yKd, MecanumDriveTrain.thetaKd);
    }

    // Set target point (using pose, custom theta and omega, default Kp and Kv gains)
    public void setTargetPoint(Pose pose, double theta, double w) {
        setTargetPoint(pose.x, pose.y, theta, pose.vx, pose.vy, w, MecanumDriveTrain.xKp, MecanumDriveTrain.yKp, MecanumDriveTrain.thetaKp, MecanumDriveTrain.xKd, MecanumDriveTrain.yKd, MecanumDriveTrain.thetaKd);
    }

    // Set target point (using target object)
    public void setTargetPoint(Target target) {
        Pose pose = target.getPose();
        setTargetPoint(pose.x, pose.y, pose.theta, pose.vx, pose.vy, pose.w, target.xKp(), target.yKp(), target.thetaKp(), target.xKd(), target.yKd(), target.thetaKd());
    }

    // Check if robot is at a certain point/angle (default tolerance)
    public boolean isAtPose(double targetX, double targetY, double targetTheta) {
        return isAtPose(targetX, targetY, targetTheta, xyTolerance, xyTolerance, thetaTolerance);
    }

    // Check if robot is at a certain point/angle (custom tolerance)
    public boolean isAtPose(double targetX, double targetY, double targetTheta, double xTolerance, double yTolerance, double thetaTolerance) {
        return (Math.abs(x - targetX) < xTolerance && Math.abs(y - targetY) < yTolerance && Math.abs(theta - targetTheta) < thetaTolerance);
    }

    public boolean notMoving() {
        /* Arbitrary Values */
        return notMoving(1.5, 0.1);
    }

    public boolean notMoving(double xyThreshold, double thetaThreshold) {
        return (Math.abs(vx) + Math.abs(vy) < xyThreshold && Math.abs(w) < thetaThreshold);
    }









    public static void log(String message) {
        Log.w("robot-log", message);
    }

}

