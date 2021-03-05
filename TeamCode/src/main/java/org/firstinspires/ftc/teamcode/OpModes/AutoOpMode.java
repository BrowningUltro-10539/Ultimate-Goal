package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto.OdometryDrive;
import org.firstinspires.ftc.teamcode.Auto.RingDeterminationPipeline;
import org.firstinspires.ftc.teamcode.Auto.Status;
import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.Auto.newCoordinateSystem;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="AutoOpModeOdometry", group="Auto")
public class AutoOpMode extends LinearOpMode {
    OpenCvInternalCamera phoneCam;
    RingDeterminationPipeline pipeline;

    protected Status pos;

    @Override
    public void runOpMode() {
        DeviceMap map = new DeviceMap();

        Status status =


        imuDrive gyro = new imuDrive();
        newCoordinateSystem robot = new newCoordinateSystem();

        map.init(hardwareMap);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        map.getImu().initialize(parameters);


        //FIX IMU FOR VERTICAL MOUNTING
        byte AXIS_MAP_CONFIG_BYTE = 0x6; //This is what to write to the AXIS_MAP_CONFIG register to swap x and z axes
        byte AXIS_MAP_SIGN_BYTE = 0x1; //This is what to write to the AXIS_MAP_SIGN register to negate the z axis
        //Need to be in CONFIG mode to write to registers
        map.getImu().write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.CONFIG.bVal & 0x0F);
        sleep(100); //Changing modes requires a delay before doing anything else
        //Write to the AXIS_MAP_CONFIG register
        map.getImu().write8(BNO055IMU.Register.AXIS_MAP_CONFIG, AXIS_MAP_CONFIG_BYTE & 0x0F);
        //Write to the AXIS_MAP_SIGN register
        map.getImu().write8(BNO055IMU.Register.AXIS_MAP_SIGN, AXIS_MAP_SIGN_BYTE & 0x0F);
        //Need to change back into the IMU mode to use the gyro
        map.getImu().write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.IMU.bVal & 0x0F);
        sleep(100); //Changing modes again requires a delay

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        pipeline = new RingDeterminationPipeline();
        phoneCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Edit @width and @height to fit your camera's resolution
                phoneCam.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }
        });

        telemetry.addData("", "ready");
        telemetry.update();



        resetEncoders(map);

        waitForStart();

        robot.initializeCoords(map, 0, 0);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();
        sleep(1000);




    }
    private void resetEncoders(DeviceMap map){
        map.getRightBottom().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.getLeftBottom().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.getRightTop().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.getLeftTop().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        map.getRightBottom().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.getLeftBottom().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.getRightTop().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.getLeftTop().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
