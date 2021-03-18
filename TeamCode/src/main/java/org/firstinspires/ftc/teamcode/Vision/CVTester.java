package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.opencv.core.CvType;
import org.openftc.easyopencv.OpenCvCameraRotation;

@TeleOp(name="CV Tester", group = "Vision")
public class CVTester extends LinearOpMode {

    /* Change the value of the string to test different pipelines ("pipeline1," "pipeline2," and "pipeline3")
    Pipeline 1: Bounding Boxes Matrices,
    Pipeline 2: Region Matrix,
    Pipeline 3: Generated Rectangle
     */

    DeviceMap map = new DeviceMap();
    protected Status pos;
    protected OpenCVBoxes pipeline1;
    protected OpenCVRegion pipeline2;
    protected OpenCVGenRect pipeline3;
    private String CvChoice = "pipeline1";

    @Override
    public void runOpMode(){
        //When using UGRect Detector
        map.setupOpenCV(hardwareMap);
        //CHANGE THE PIPELINE VARIABLE from like pipeline1 to pipeline# (1-3) AND INSTANTIATE THE PIPELINE
        map.getCamera().setPipeline(pipeline1 = new OpenCVBoxes());
        map.getCamera().startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);


        while(!opModeIsActive()){
            if(CvChoice == "pipeline1") {
                Status status = pipeline1.rings();
                pos = status;
                telemetry.addData("Rings", pos);
                telemetry.update();
            } else if(CvChoice == "pipeline2") {
                Status status = pipeline2.rings();
                pos = status;
                telemetry.addData("Rings", pos);
                telemetry.update();
            } else if(CvChoice == "pipeline3"){
                Status status = pipeline3.getStack();
                pos = status;
                telemetry.addData("Rings", pos);
                telemetry.update();
            }




        }

    }
}
