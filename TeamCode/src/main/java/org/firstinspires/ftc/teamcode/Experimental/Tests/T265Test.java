package org.firstinspires.ftc.teamcode.Experimental.Tests;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Experimental.T265CoordinateSystem.t265CoordinateSystem;
import org.firstinspires.ftc.teamcode.Util.MecanumDriveTrain;

import static java.lang.Math.PI;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.drawRobot;

import org.firstinspires.ftc.teamcode.Experimental.T265PP.Pathing.Pose;
@TeleOp(name = "EXP: T265 Test")
public class T265Test extends LinearOpMode {

    /* TODO: Eric, please change these values as I'm only doing these off of arbitrary values. */
    public static double startX = 0;
    public static double startY = 0;
    public double startTheta = PI/2;
    private static final double INCH_TO_METER = 0.0254;
    private double x;
    private double y;
    private double theta;
    private double prevTime;

    public Rotation2d rotation = new Rotation2d(0);
    public Pose2d reset_pos = new Pose2d(0, 0, rotation);

    public Pose startingPose;
    private Pose currentPose;


    @Override
    public void runOpMode(){
        MecanumDriveTrain mdt = new MecanumDriveTrain(this, startX, startY, startTheta);
        t265CoordinateSystem coords = new t265CoordinateSystem();


        startingPose = new Pose(0, 0, PI/2);
        currentPose = new Pose().copy(startingPose);
        //map.init(hardwareMap);

        coords.intializeSystem(startingPose, hardwareMap, this);
        coords.stop();
        coords.intializeSystem(startingPose, hardwareMap, this);



        waitForStart();


        while(opModeIsActive()){

            mdt.setControls(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);

            coords.updateTrackedPos(startingPose, currentPose, telemetry);
//            coords.t265update(this);
//
//            coords.t265update(this);

            if (gamepad1.b) {
                coords.stop();
            }

            if(gamepad1.a){

                coords.getT265().setPose(reset_pos);
            }

//            x = coords.getxPos();
//            y = coords.getyPos();
//            theta = coords.getCurrentAngleDegrees();

            double curTime = (double) System.currentTimeMillis() / 1000;
            double timeDiff = curTime - prevTime;
            prevTime = curTime;

//            telemetry.addData("X: ", x);
//            telemetry.addData("Y: ", y);
//            telemetry.addData("Theta: ", theta);
//            telemetry.update();
        }

        coords.stop();
    }

    public Pose getStartingPose(){
        return startingPose;
    }

}
