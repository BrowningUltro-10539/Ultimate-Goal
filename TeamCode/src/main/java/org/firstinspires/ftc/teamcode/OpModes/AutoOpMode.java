package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.Auto.newCoordinateSystem;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.firstinspires.ftc.teamcode.Vision.ObjectIdentification;
import org.firstinspires.ftc.teamcode.Vision.RingPipeline;
import org.firstinspires.ftc.teamcode.Vision.Status;

@Autonomous(name="AutoOpModeOdometry", group="Auto")
public class AutoOpMode extends LinearOpMode {

    protected ObjectIdentification searchableTarget = null;
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String [] ASSET_NAMES = {"Quad", "Single"};
    private static final String TARGET_NAME = "";
    protected Status pos;

    /* @ERIC After we record videos, I'm going to create a base class that allows us to init this  */


    @Override
    public void runOpMode() {

        DeviceMap map = new DeviceMap();
        imuDrive gyro = new imuDrive();
        newCoordinateSystem robot = new newCoordinateSystem();
        map.init(hardwareMap);
        searchableTarget = new RingPipeline(hardwareMap, telemetry, TFOD_MODEL_ASSET, ASSET_NAMES, TARGET_NAME);


        if(!opModeIsActive()) {
            while (!opModeIsActive()) {
                searchableTarget.find();
            }
        }

        pos = searchableTarget.getStatus();

        /* Add these into a preliminary base class that this OpMode will extend */
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

        robot.initializeCoords(map, -50, 0);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();

        switch(pos){
            case FOUR:
                /* robot.goToPosition(); */
            case ONE:
                /* robot.goToPosition(); */
            case NONE:
                /* robot.goToPosition(); */
        }
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

