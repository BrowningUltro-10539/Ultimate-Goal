package org.firstinspires.ftc.teamcode.Auto.AutoTesting;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto.OdometryDrive;
import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

@Disabled
@Autonomous(name = "TestingAuto")
public class TestingAuto extends LinearOpMode {


    @Override
    public void runOpMode(){
        DeviceMap map = new DeviceMap();
        OdometryDrive drive = new OdometryDrive();
        imuDrive gyro = new imuDrive();
        coordinateSystem robot = new coordinateSystem();

        map.init(hardwareMap);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        map.getImu().initialize(parameters);


        //FIX IMU FOR VERTICAL MOUNTING
        byte AXIS_MAP_CONFIG_BYTE = 0x6; //This is what to write to the AXIS_MAP_CONFIG register to swap x and z axes
        byte AXIS_MAP_SIGN_BYTE = 0x1; //This is what to write to the AXIS_MAP_SIGN register to negate the z axis
        //Need to be in CONFIG mode to write to registers
        map.getImu().write8(BNO055IMU.Register.OPR_MODE,BNO055IMU.SensorMode.CONFIG.bVal & 0x0F);
        sleep(100); //Changing modes requires a delay before doing anything else
        //Write to the AXIS_MAP_CONFIG register
        map.getImu().write8(BNO055IMU.Register.AXIS_MAP_CONFIG,AXIS_MAP_CONFIG_BYTE & 0x0F);
        //Write to the AXIS_MAP_SIGN register
        map.getImu().write8(BNO055IMU.Register.AXIS_MAP_SIGN,AXIS_MAP_SIGN_BYTE & 0x0F);
        //Need to change back into the IMU mode to use the gyro
        map.getImu().write8(BNO055IMU.Register.OPR_MODE,BNO055IMU.SensorMode.IMU.bVal & 0x0F);
        sleep(100); //Changing modes again requires a delay

        telemetry.addData("","ready");
        telemetry.update();

        resetEncoders(map);

        waitForStart();

        robot.initalizePosition(0,0);
        robot.initializeDirection(1);

        telemetry.addData("X", robot.getCurrentX());
        telemetry.addData("Y", robot.getCurrentY());
        telemetry.update();
        sleep(1000);

        //Move forwards

        robot.goTo(0,50,0.5,true, map);

        telemetry.addData("X", robot.getCurrentX());
        telemetry.addData("Y", robot.getCurrentY());
        telemetry.addData("Direction", robot.getCurrentDirection());
        telemetry.update();
        sleep (1000);

        //Turn left 90
        gyro.turn(90,0.3, map);
        robot.updateDirection(map);

        telemetry.addData("X", robot.getCurrentX());
        telemetry.addData("Y", robot.getCurrentY());
        telemetry.addData("Direction", robot.getCurrentDirection());
        telemetry.update();

        sleep(1000);
        //Move left 30 cm
        robot.goTo(0,20,0.5,true, map);

        telemetry.addData("X", robot.getCurrentX());
        telemetry.addData("Y", robot.getCurrentY());
        telemetry.addData("Direction", robot.getCurrentDirection());
        telemetry.update();

        sleep(1000);

        robot.goTo(0,50,0.5,true, map);
        telemetry.addData("X", robot.getCurrentX());
        telemetry.addData("Y", robot.getCurrentY());
        telemetry.addData("Direction", robot.getCurrentDirection());
        telemetry.update();

        sleep(1000);

        gyro.turn(80,0.5, map);
        robot.updateDirection(map);

        resetEncoders(map);

        telemetry.addData("X", robot.getCurrentX());
        telemetry.addData("Y", robot.getCurrentY());
        telemetry.addData("Direction", robot.getCurrentDirection());
        telemetry.update();
        sleep(1000);
        // GYRO ASSIST CANNOT BE USED WHEN DRIVING FACING DIRECTION 3
        //I'm not sure how to fix this
        robot.goTo(0,0,0.3, false, map);

        telemetry.addData("X", robot.getCurrentX());
        telemetry.addData("Y", robot.getCurrentY());
        telemetry.addData("Direction", robot.getCurrentDirection());
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
