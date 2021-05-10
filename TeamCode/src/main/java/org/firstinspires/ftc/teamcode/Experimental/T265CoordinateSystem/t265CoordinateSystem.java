package org.firstinspires.ftc.teamcode.Experimental.T265CoordinateSystem;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.spartronics4915.lib.T265Camera;

import org.firstinspires.ftc.teamcode.Auto.OdometryDrive;
import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.Experimental.Util.T265;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

public class t265CoordinateSystem {
    private static T265Camera t265 = null;
    OdometryDrive drive = new OdometryDrive();
    imuDrive gyro = new imuDrive();

    private double xPos;
    private double yPos;

    private double currentAngle;

    private static final double RADIANS_TO_DEGREES = 180/Math.PI;

    private static final double DEGREES_TO_RADIANS = Math.PI/180;



    public void intializeSystem(double startingX, double startingY, double startingAngle, HardwareMap hardwareMap, OpMode opMode){
        if(t265 == null){
            t265 = new T265Camera(new Transform2d(), 0.1, hardwareMap.appContext);

        }

        xPos = startingX;
        yPos = startingY;

        currentAngle = startingAngle;

        t265.start();
        t265.setPose(new Pose2d(startingX, startingY, new Rotation2d(startingAngle)));

        T265Camera.CameraUpdate up = t265.getLastReceivedCameraUpdate();

        Translation2d translation = new Translation2d(up.pose.getTranslation().getX(), up.pose.getTranslation().getY());
        Rotation2d rotation = up.pose.getRotation();

        currentAngle = rotation.getRadians();

        xPos = translation.getX();
        yPos = translation.getY();

        opMode.telemetry.addData("X:", xPos);
        opMode.telemetry.addData("Y:", yPos);
        opMode.telemetry.addData("Angle:", currentAngle);
        opMode.telemetry.update();


    }

    public void t265update(OpMode opMode){

        T265Camera.CameraUpdate up = t265.getLastReceivedCameraUpdate();

        Translation2d translation = new Translation2d(up.pose.getTranslation().getX(), up.pose.getTranslation().getY());
        Rotation2d rotation = up.pose.getRotation();

        currentAngle = rotation.getRadians();

        xPos = translation.getX();
        yPos = translation.getY();

        opMode.telemetry.addData("X:", xPos);
        opMode.telemetry.addData("Y:", yPos);
        opMode.telemetry.addData("Angle:", currentAngle);
        opMode.telemetry.update();
    }

    public Translation2d t265Translation(){
        T265Camera.CameraUpdate up = t265.getLastReceivedCameraUpdate();
        Translation2d translation = new Translation2d(up.pose.getTranslation().getX(), up.pose.getTranslation().getY());
        return translation;

    }

    public void stop(){
        t265.stop();
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getCurrentAngleDegrees() {
        return currentAngle*RADIANS_TO_DEGREES;
    }

    public double getCurrentAngleRadians(){
        return currentAngle;
    }
}

