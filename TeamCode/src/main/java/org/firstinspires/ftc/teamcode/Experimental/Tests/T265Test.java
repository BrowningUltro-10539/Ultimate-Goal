package org.firstinspires.ftc.teamcode.Experimental.Tests;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.spartronics4915.lib.T265Camera;

import org.firstinspires.ftc.teamcode.Experimental.Util.MecanumDriveTrain;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.addPacket;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.drawField;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.drawRobot;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.sendPacket;
@TeleOp(name = "EXP: T265 Test")
@Disabled
public class T265Test extends LinearOpMode {
    private static T265Camera vslam = null;

    /* TODO: Eric, please change these values as I'm only doing these off of arbitrary values. */
    public static double startX = 50;
    public static double startY = 0;
    public double startTheta = Math.PI/2;
    private static final double INCH_TO_METER = 0.0254;

    @Override
    public void runOpMode(){
        MecanumDriveTrain mdt = new MecanumDriveTrain(this, startX, startY, startTheta);
        if(vslam == null){
            vslam = new T265Camera(new Transform2d(), 0.1, hardwareMap.appContext);
        }

        waitForStart();
        vslam.start();
        vslam.setPose(new Pose2d(startX * INCH_TO_METER, startY * INCH_TO_METER, new Rotation2d(startTheta)));

        while(opModeIsActive()){
            mdt.RCControls(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);

            T265Camera.CameraUpdate up = vslam.getLastReceivedCameraUpdate();
            if(up == null) continue;

            Translation2d translation = new Translation2d(up.pose.getTranslation().getX() / 0.0254, up.pose.getTranslation().getY() / 0.0254);
            Rotation2d rotation = up.pose.getRotation();

            drawField();
            drawRobot(translation.getX(), translation.getY(), rotation.getRadians(), "blue");

            addPacket("X", translation.getX());
            addPacket("Y", translation.getY());
            addPacket("Theta", rotation.getRadians());
            sendPacket();
        }

        vslam.stop();
    }

}
