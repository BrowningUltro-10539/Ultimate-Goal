package org.firstinspires.ftc.teamcode.Experimental.Vision;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

public class BasePipeline {
    DeviceMap map = new DeviceMap();

    private OpenCvCamera camera;

    public BasePipeline(LinearOpMode op){
        Context appContext = op.hardwareMap.appContext;
        int cameraMonitorViewId = appContext.getResources().getIdentifier("cameraMonitorViewId", "id", appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
    }

    public void start() {
        camera.openCameraDevice();
        camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
    }


    public void stop(){
        camera.stopStreaming();
        camera.closeCameraDevice();
    }

    public void setPipeline(OpenCvPipeline pipeline){
        camera.setPipeline(pipeline);
    }

    /* Debugging Purposes (if Needed) */
    public int getFrameCount(){
        return camera.getFrameCount();
    }

    public double getFPS(){
        return camera.getFps();
    }

    public int getTotalFrameTimeMS(){
        return camera.getTotalFrameTimeMs();
    }

    public int getPipelineTimeMS(){
        return camera.getPipelineTimeMs();
    }

    public int getOverheadTimeMS(){
        return camera.getOverheadTimeMs();
    }

    public int getCurrentPipelineMaxFPS(){
        return camera.getCurrentPipelineMaxFps();
    }
}

