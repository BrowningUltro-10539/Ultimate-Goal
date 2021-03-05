
package org.firstinspires.ftc.teamcode;


import org.firstinspires.ftc.teamcode.Auto.RingDeterminationPipeline;
import org.firstinspires.ftc.teamcode.Auto.Status;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvInternalCamera;


public abstract class Auto1 extends AutoBase {
    protected Status pos;
    protected DeviceMap map;
    protected RingDeterminationPipeline pipeline;
    protected OpenCvInternalCamera phoneCam;


    public void setup(DeviceMap map){
        map.init(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        pipeline = new RingDeterminationPipeline();
        phoneCam.setPipeline(pipeline);

        this.map = map;

    }

    public void beforeLoop(){
        Status status = ringPosition();
        telemetry.addData("Status", status.name());
        pos = status;

    }

    protected Status ringPosition(){
        return pipeline.getStatus();
    }



    }




