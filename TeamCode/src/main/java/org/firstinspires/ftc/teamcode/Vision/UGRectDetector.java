package org.firstinspires.ftc.teamcode.Vision;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

public class UGRectDetector {

    private OpenCvCamera camera;
    private boolean isUsingWebcam;
    private String webcamName;
    private HardwareMap hardwareMap;
    private OpenCVGenRect pipeline;
    private Status status;

    //The constructor is overloaded to allow the use of webcam instead of the phone camera
    public UGRectDetector(HardwareMap hMap) {
        hardwareMap = hMap;
    }

    public UGRectDetector(HardwareMap hMap, String webcamName) {
        hardwareMap = hMap;
        isUsingWebcam = true;
        this.webcamName = webcamName;
    }

    public void init() {
        //This will instantiate an OpenCvCamera object for the camera we'll be using
        if (isUsingWebcam) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        } else {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        }

        //Set the pipeline the camera should use and start streaming
        camera.setPipeline(pipeline = new OpenCVGenRect());
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
        });
    }

    public void setTopRectangle(double topRectHeightPercentage, double topRectWidthPercentage) {
        pipeline.setTopRectHeightPercentage(topRectHeightPercentage);
        pipeline.setTopRectWidthPercentage(topRectWidthPercentage);
    }

    public void setBottomRectangle(double bottomRectHeightPercentage, double bottomRectWidthPercentage) {
        pipeline.setBottomRectHeightPercentage(bottomRectHeightPercentage);
        pipeline.setBottomRectWidthPercentage(bottomRectWidthPercentage);
    }

    public void setRectangleSize(int rectangleWidth, int rectangleHeight){
        pipeline.setRectangleHeight(rectangleHeight);
        pipeline.setRectangleWidth(rectangleWidth);
    }

    public Status getStack() {
        if (Math.abs(pipeline.getTopAverage() - pipeline.getBottomAverage()) < pipeline.getThreshold() && (pipeline.getTopAverage() <= 100 && pipeline.getBottomAverage() <= 100)) {
            return status.FOUR;
        } else if (Math.abs(pipeline.getTopAverage() - pipeline.getBottomAverage()) < pipeline.getThreshold() && (pipeline.getTopAverage() >= 100 && pipeline.getBottomAverage() >= 100)) {
            return status.NONE;
        } else {
            return status.ONE;
        }
    }

    public void setThreshold(int threshold) {
        pipeline.setThreshold(threshold);
    }

    public double getTopAverage() {
        return pipeline.getTopAverage();
    }

    public double getBottomAverage() {
        return pipeline.getBottomAverage();
    }



}

