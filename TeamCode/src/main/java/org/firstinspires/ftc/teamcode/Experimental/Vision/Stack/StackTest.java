package org.firstinspires.ftc.teamcode.Experimental.Vision.Stack;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "EXP: Stack Height Test")
public class StackTest extends LinearOpMode {
    private StackDetector detector;

    @Override
    public void runOpMode(){
        detector = new StackDetector(this);

        waitForStart();
        detector.start();

        while(opModeIsActive()){
            telemetry.addData("Frame Count", detector.getFrameCount());
            telemetry.addData("FPS", detector.getFPS());
            telemetry.addData("W/H Ratio", detector.getRawResult()[2]);
            telemetry.addData("Result", detector.getResult());
            telemetry.addData("Mode Result", detector.getModeResult());
            telemetry.update();
        }

        detector.stop();
    }


}
