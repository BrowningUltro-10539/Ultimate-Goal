package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.firstinspires.ftc.teamcode.Vision.ObjectIdentification;
import org.firstinspires.ftc.teamcode.Vision.RingPipeline;
import org.firstinspires.ftc.teamcode.Vision.Status;

@TeleOp(name="Pipeline Tester", group="Testing")
public class PipelineTester extends LinearOpMode {
    protected ObjectIdentification searchableTarget = null;
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String [] ASSET_NAMES = {"Quad", "Single"};
    private static final String TARGET_NAME = "";
    protected Status pos;

    public void runOpMode(){
        DeviceMap map = new DeviceMap();
        searchableTarget = new RingPipeline(hardwareMap, telemetry, TFOD_MODEL_ASSET, ASSET_NAMES, TARGET_NAME);

        while (!isStarted()) {
            searchableTarget.find();
            pos = searchableTarget.getStatus();
            telemetry.addData("detected", pos);
            telemetry.update();

        }

        waitForStart();
    }

}
