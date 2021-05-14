package org.firstinspires.ftc.teamcode.Auto.AutoTesting;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto.OdometryDrive;
import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.Auto.newCoordinateSystem;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

@Autonomous(name = "Testing Auto Vectorized")
public class CoordinatesTestingAuto extends LinearOpMode {


    @Override
    public void runOpMode() {
        DeviceMap map = new DeviceMap();

        OdometryDrive drive = new OdometryDrive();
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

        telemetry.addData("", "ready");
        telemetry.update();

        resetEncoders(map);

        waitForStart();

        robot.initializeCoords(map, 0,0);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();
        sleep(1000);

        //Move forwards

        robot.goToPosition(-50, 50, map, 0.3, true, true, this);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();
        sleep(1000);
/*
        //Turn left 90
        gyro.turn(90, 0.3, map);
        robot.updateAngle(map);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();

        sleep (1000);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Direction", robot.getCurrentAngle());
        telemetry.update();

        robot.goToPosition(100, 30, map, 0.3, true, false);
        sleep(1000);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();

        gyro.turn(-90, 0.3, map);
        sleep(1000);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();

        robot.updateAngle(map);

        //robot.goToPosition(0,0, map, 0.3, true, false);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();

        sleep(1000);

*/
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
