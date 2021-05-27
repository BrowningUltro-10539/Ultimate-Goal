package org.firstinspires.ftc.teamcode.Experimental.T265CoordinateSystem;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.spartronics4915.lib.T265Camera;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Auto.OdometryDrive;
import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.Experimental.T265PP.Pathing.Pose;
import org.firstinspires.ftc.teamcode.Experimental.Tests.T265Test;
import org.firstinspires.ftc.teamcode.Experimental.Util.T265;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

import static java.lang.Math.*;

public class t265CoordinateSystem {
    private static T265Camera t265 = null;
    OdometryDrive drive = new OdometryDrive();
    imuDrive gyro = new imuDrive();

    private double xPos;
    private double yPos;

    private double currentAngle;

    private static final double RADIANS_TO_DEGREES = 180/ PI;

    private static final double DEGREES_TO_RADIANS = PI/180;

    private static final double METERS_TO_INCHES = 39.37;

    public Pose startingPose = new Pose(0, 0, PI/2);

    public double translationY;
    public double translationX;
    public double rotation;



    public void intializeSystem(Pose pose, HardwareMap hardwareMap, OpMode opMode){
        if(t265 == null){
            t265 = new T265Camera(new Transform2d(), 0.1, hardwareMap.appContext);
            T265Camera.CameraUpdate up = t265.getLastReceivedCameraUpdate();
            translationY = up.pose.getTranslation().getY();
            translationX = up.pose.getTranslation().getX();
            rotation = up.pose.getRotation().getDegrees();





        }

        xPos = pose.x;
        yPos = pose.y;

        currentAngle = pose.angle;

        t265.start();
        t265.setPose(new Pose2d(pose.x, pose.y, new Rotation2d(pose.angle)));

        T265Camera.CameraUpdate up = t265.getLastReceivedCameraUpdate();

        Translation2d translation = new Translation2d(up.pose.getTranslation().getX(), up.pose.getTranslation().getY());
        Rotation2d rotation = up.pose.getRotation();

        currentAngle = rotation.getRadians();

        yPos = translation.getX() * METERS_TO_INCHES;
        xPos = translation.getY() * METERS_TO_INCHES;

        opMode.telemetry.addData("X:", yPos);
        opMode.telemetry.addData("Y:", xPos);
        opMode.telemetry.addData("Angle:", currentAngle);
        opMode.telemetry.update();


    }

    public void t265update(OpMode opMode){

        T265Camera.CameraUpdate up = t265.getLastReceivedCameraUpdate();

        Translation2d translation = new Translation2d(up.pose.getTranslation().getX(), up.pose.getTranslation().getY());
        Rotation2d rotation = up.pose.getRotation();

        currentAngle = rotation.getRadians();

        xPos = translation.getX() * METERS_TO_INCHES;
        yPos = translation.getY() * METERS_TO_INCHES;

        opMode.telemetry.addData("X:", xPos);
        opMode.telemetry.addData("Y:", yPos);
        opMode.telemetry.addData("Angle:", currentAngle);
        opMode.telemetry.update();
    }

    public Translation2d t265Translation(){
        T265Camera.CameraUpdate up = t265.getLastReceivedCameraUpdate();
        Translation2d translation = new Translation2d(up.pose.getTranslation().getX() * METERS_TO_INCHES, up.pose.getTranslation().getY() * METERS_TO_INCHES);
        return translation;

    }

    public void setPose(Pose2d new_Pose){
        t265.setPose((new_Pose));
    }

    public static void updateTrackedPos(Pose startingPose, Pose currentPose, Telemetry telemetry){
        T265Camera.CameraUpdate up = t265.getLastReceivedCameraUpdate();

        Translation2d translation = new Translation2d(up.pose.getTranslation().getX() / 0.0254, up.pose.getTranslation().getY() / 0.0254);
        Rotation2d rotation = up.pose.getRotation();

        currentPose.x =
        currentPose.x = startingPose.x + -translation.getY();
        currentPose.y = startingPose.y + translation.getX();
        currentPose.angle = startingPose.angle - rotation.getDegrees();

        telemetry.addData("X", currentPose.x);
        telemetry.addData("Y", currentPose.y);
        telemetry.addData("Rotation", currentPose.angle);
        telemetry.update();
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

    public T265Camera getT265() { return t265; }

    public Pose getStartingPose() {
        return startingPose;
    }

    public double getCurrentY(){
        return translationY;
    }

    public double getCurrentX(){
        return translationX;
    }

    public double getRotation(){
        return rotation;
    }

}

