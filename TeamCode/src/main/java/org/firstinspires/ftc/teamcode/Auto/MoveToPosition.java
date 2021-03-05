package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;


@Autonomous(name = "Move to position test in line")
public class MoveToPosition extends LinearOpMode {

    private static double wheelDiameter = 6;
    private static double countsPerRevolution = 8192;

    private static double wheelCircumference = wheelDiameter * Math.PI;
    private static double distancePerCount = wheelCircumference / countsPerRevolution;

    @Override
    public void runOpMode(){
        DeviceMap map = new DeviceMap();
        OdometryDrive drive = new OdometryDrive();

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

        waitForStart();

        //Y is for strafing, X is for movement in line with wheels
        DcMotor encY = map.getLeftTop();
        DcMotor encX = map.getRightBottom();
        // dont try over 0.5 speed
        drive.moveUntil("Forward",  50, 0.7, map, true);

        drive.moveUntil("Backward", 50, 0.7, map, true);

    }
}
