
//package org.firstinspires.ftc.teamcode;
//
//
//import android.bluetooth.BluetoothClass;
//
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//
//import org.firstinspires.ftc.teamcode.Auto.RingDeterminationPipeline;
//import org.firstinspires.ftc.teamcode.Auto.RingPosition;
//import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
//import org.openftc.easyopencv.OpenCvCameraFactory;
//import org.openftc.easyopencv.OpenCvInternalCamera;
//
//@Disabled
//public abstract class Auto1 extends AutoBase {
//    protected RingPosition pos;
//    protected DeviceMap map;
//    protected RingDeterminationPipeline pipeline;
//    protected OpenCvInternalCamera phoneCam;
//
//
//    @Override
//    public void setup(DeviceMap map){
//        map.init(hardwareMap);
//
//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
//        pipeline = new RingDeterminationPipeline();
//        phoneCam.setPipeline(pipeline);
//
//        this.map = map;
//
//    }
//
//    @Override
//    public void beforeLoop(){
//        RingPosition position = new rings();
//        pos = position;
//
//    }
//
//    protected RingPosition rings(){
//        return pipeline.position;
//    }
//
//
//    }
//
//
//
//
