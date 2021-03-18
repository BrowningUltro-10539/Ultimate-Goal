package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

@Autonomous(name = "Flywheel Testing")
public class FlywheelTimingTesting extends LinearOpMode {


    @Override
    public void runOpMode() {
        DeviceMap map = new DeviceMap();

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
        map.getLeftClaw().setPosition(-1);
        map.getRightClaw().setPosition(1);

        telemetry.addData("", "ready");
        telemetry.update();


        resetEncoders(map);

        waitForStart();


        map.getFlyWheel().setPower(1);

        map.getBucket().setPosition(0.63);

        sleep(2000);



        for(int i=0; i<3; i++){
            map.getRingHolder().setPosition(1);
            sleep(300);
            map.getLaunchBlocker().setPosition(-0.6);
            map.getBucketPusher().setPosition(0);
            sleep(500);

            map.getBucketPusher().setPosition(1);
            map.getLaunchBlocker().setPosition(1);
            map.getRingHolder().setPosition(-1);

            sleep(1000);
        }
        sleep(1000);
        map.getFlyWheel().setPower(0);

        map.getBucket().setPosition(0.5);

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
