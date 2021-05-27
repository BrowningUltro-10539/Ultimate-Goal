package org.firstinspires.ftc.teamcode.Experimental.Tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Util.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.Experimental.Util.T265;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.addPacket;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.drawField;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.drawRobot;
import static org.firstinspires.ftc.teamcode.Experimental.Debugging.Dashboard.sendPacket;


@TeleOp(name = "EXP: DT265")
@Config
public class DashboardT265 extends LinearOpMode{
    /* TODO: Eric, please change these values (x, y) (not theta) as I'm only doing these off of arbitrary values. */
    public static double startX = 50;
    public static double startY = 0;
    public double startTheta = Math.PI/2;
    private double x;
    private double y;
    private double theta;
    private double prevTime;

    @Override
    public void runOpMode(){
        MecanumDriveTrain mdt = new MecanumDriveTrain(this, startX, startY, startTheta);
        T265 t265 = new T265(this, startX, startY, startTheta);

        waitForStart();
        t265.startCam();

        while(opModeIsActive()){
            mdt.setControls(gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x);

            if(gamepad1.x){
                t265.setCameraPose(startX, startY, startTheta);
            }

            if(gamepad1.a){
                t265.exportMap();
            }

            if(gamepad1.b){
                t265.stopCam();
            }

            t265.updateCamPose();
            x = t265.getX();
            y = t265.getY();
            theta = t265.getTheta();

            double curTime = (double) System.currentTimeMillis() / 1000;
            double timeDiff = curTime - prevTime;
            prevTime = curTime;

            drawField();
            drawRobot(x, y, theta, t265.confidenceColor());
            addPacket("X", x);
            addPacket("Y", y);
            addPacket("Theta", theta);
            addPacket("isEmpty", t265.isEmpty);
            addPacket("Update Frequency (Hz)", 1 / timeDiff);
            sendPacket();

            telemetry.addData("X: ", x);
            telemetry.addData("Y: ", y);
            telemetry.addData("Theta: ", theta);
            telemetry.update();
        }

        t265.stopCam();

    }
}
