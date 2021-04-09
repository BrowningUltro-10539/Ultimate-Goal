package org.firstinspires.ftc.teamcode.Auto.AutoTesting;



import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

@Disabled
@Autonomous(name = "Gyro turn test")
public class gyroTurnTest extends LinearOpMode {

    @Override
    public void runOpMode(){
        DeviceMap map = new DeviceMap();
        map.init(hardwareMap);
        imuDrive drive = new imuDrive();

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
        double firstAngle = checkOrientation(map.getImu());
        telemetry.addData("first angle",firstAngle);
        telemetry.update();

        waitForStart();
        drive.turn(90,0.3, map);
        sleep(1000);
        drive.turn(-90, 0.3, map);
        sleep(1000);
        drive.turn(180,0.3,map);
        sleep(1000);
        drive.turn(90,0.3, map);
        sleep(1000);
        drive.turn(-180, 0.3, map);
        sleep(1000);
        drive.turn(-90,0.3, map);

    }



    private double checkOrientation(BNO055IMU imu){
        Orientation current = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
        return current.firstAngle;
    }

    private void stop(DeviceMap map){
        map.getLeftTop().setPower(0);
        map.getRightTop().setPower(0);
        map.getLeftBottom().setPower(0);
        map.getRightBottom().setPower(0);
    }
}
