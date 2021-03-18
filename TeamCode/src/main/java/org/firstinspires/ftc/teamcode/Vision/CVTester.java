package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.openftc.easyopencv.OpenCvCameraRotation;

@TeleOp(name="CV Tester", group = "Vision")
public class CVTester extends LinearOpMode {

    /* Change the value of the string to test different pipelines ("pipeline1," "pipeline2," "pipeline3," and "pipeline4)
    Pipeline 1: Bounding Boxes Matrices,
    Pipeline 2: Region Matrix,
    Pipeline 3: Pixels,
    Pipeline 4: TFOD
     */

    DeviceMap map = new DeviceMap();
    protected Status pos;
    protected OpenCVBoxes pipeline1;
    private String CvChoice = "pipeline1";

    @Override
    public void runOpMode(){
        map.setupOpenCV(hardwareMap);
        map.getCamera().setPipeline(pipeline1 = new OpenCVBoxes());
        map.getCamera().startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);

        while(!opModeIsActive()){
            if(CvChoice == "pipeline1") {
                Status status = pipeline1.rings();
                pos = status;
                telemetry.addData("Rings", pos);
                telemetry.update();
            }

            if(CvChoice == "pipeline2") {
            }

            if(CvChoice == "pipeline3") {
            }
        }

    }
}
