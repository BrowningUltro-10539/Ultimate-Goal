package org.firstinspires.ftc.teamcode.OpModes;

import android.sax.StartElementListener;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.comm.RobotUsbDevicePretendModernRobotics;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto.OdometryDrive;
import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.Auto.newCoordinateSystem;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.firstinspires.ftc.teamcode.Vision.OpenCVBoxes;
import org.firstinspires.ftc.teamcode.Vision.Status;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name="DoubleWobble", group="Auto")
public class AutoOpModeDoubleWobbleGoal extends LinearOpMode {

    protected Status pos;
    protected OpenCVBoxes pipeline1;

    /* @ERIC After we record videos, I'm going to create a base class that allows us to init this  */

    DeviceMap map = null;
    imuDrive gyro = null;
    OdometryDrive drive = null;
    newCoordinateSystem robot = null;

    @Override
    public void runOpMode() {

        map = new DeviceMap();
        gyro = new imuDrive();
        drive = new OdometryDrive();
        robot = new newCoordinateSystem();
        map.init(hardwareMap);
        map.setUpDriveMotors(hardwareMap);
        map.setupOpenCV(hardwareMap);

        setup();

        while (!isStarted()) {
            Status status = pipeline1.rings();
            pos = status;
            telemetry.addData("Rings", pos);
            telemetry.update();
        }

        waitForStart();

        Status status = pipeline1.rings();
        pos = status;


        robot.initializeCoords(map, -50, 0);
        map.deactivateOpenCV();




        switch (pos) {
            case FOUR: // Zone C

                driveToZoneC();

                dropWobble();

                goToShootingPosC();

                shoot3();

                //pickUpRandomStackC();

                //shootRandomRings();

                parkC();
//                driveToWobble2();
//
//                grabWobble2();
//
//                driveToZoneC2();
//
//                dropWobble();

                //park();

                break;

            case ONE: // Zone B

                driveToZoneB();

                dropWobble();

                driveToShootingPosB();

                shoot3();

                driveToWobble2B();

                grabWobble2();

                driveToZoneB2();

                dropWobble();

                parkB();

                break;


            case NONE: // Zone A
                driveToZoneA();

                dropWobble();

                driveToShootingPos();

                shoot3();

                driveToWobble2();

                grabWobble2();

                driveToZoneA2();

                dropWobble();

                parkA();

                break;

        }
    }


    //FOUR RINGS
    private void driveToZoneC() {
        robot.goToPosition(-20, 1, map, 0.7, true, false, this);

        robot.goToPosition(-19, 255, map, 0.7, true, false, this);

    }

    //ONE RING
    private void driveToZoneB() {
        robot.goToPosition(-15, 1, map, 0.7, true, false, this);

        robot.goToPosition(-50, 175, map, 0.7, true, false, this);


    }

    //NO RINGS
    private void driveToZoneA() {
        robot.goToPosition(-20, 1, map, 0.7, true, false, this);

        robot.goToPosition(-19, 130, map, 0.7, true, false, this);

        robot.setyPos(131);

    }

    private void dropWobble() {
        map.getRingFlicker().setPosition(0.5);
        map.getArm().setPower(0.7);

        sleep(1000);

        map.getArm().setPower(0);

        map.getLeftClaw().setPosition(1);
        map.getRightClaw().setPosition(-1);

        sleep(500);

        map.getArm().setPower(-0.7);

        sleep(700);

        map.getArm().setPower(0);
    }

    private void driveToShootingPos() {
        map.getRingFlicker().setPosition(0);
        map.getBucket().setPosition(0.62);

        robot.goToPosition(-80, 130, map, 0.7, true, false, this);

        map.getFlyWheel().setPower(1);

        gyro.turn(85, 0.5, map);

        robot.updateAngle(map);


    }

    private void shoot3() {
        for (int i = 0; i < 4; i++) {

            map.getLaunchBlocker().setPosition(-0.7);
            map.getBucketPusher().setPosition(0);
            sleep(1000);

            map.getBucketPusher().setPosition(1);
            map.getLaunchBlocker().setPosition(1);
            map.getRingHolder().setPosition(-1);

            sleep(1000);
        }

        map.getFlyWheel().setPower(0);
        map.getBucket().setPosition(0.5);
        resetEncoders();

    }

    private void driveToWobble2() {
        robot.updateAngle(map);

        robot.goToPosition(-120, 40, map, 0.7, true, false, this);

        gyro.turn(180, 0.8, map);

        robot.updateAngle(map);

        robot.goToPosition(robot.getXPos() + 12, robot.getYPos() - 1, map, 0.5, true, false, this);

        robot.setxPos(-130);
        robot.setyPos(15);
    }

    private void grabWobble2() {

        map.getLeftClaw().setPosition(1);
        map.getRightClaw().setPosition(-1);

        map.getArm().setPower(1);

        sleep(500);

        map.getArm().setPower(0);

        map.getLeftClaw().setPosition(-1);
        map.getRightClaw().setPosition(1);

        sleep(1000);

        map.getArm().setPower(-1);

        sleep(800);

        map.getArm().setPower(0);

        resetEncoders();

    }

    private void parkA() {
        drive.moveUntil("Backward", 50, 1, map, true);
//        robot.goToPosition(, 120, map, 1, true, false, this);
    }

    private void parkB() {
        robot.goToPosition(robot.getXPos() - 1, 200, map, 1, true, false, this);
    }

    private void parkC(){
        drive.moveUntil("Forward", 10, 0.5, map, true);
    }


    private void resetEncoders() {
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

    private void setup() {
        map.getCamera().setPipeline(pipeline1 = new OpenCVBoxes());
        map.getCamera().startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);

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

        map.getLeftClaw().setPosition(-1);
        map.getRightClaw().setPosition(1);

        map.getRingFlicker().setPosition(0);
        map.getRingHolder().setPosition(-1);
        map.getBucketPusher().setPosition(1);
        map.getBucket().setPosition(0.5);

        telemetry.addData("", "ready");
        telemetry.update();

        resetEncoders();
    }

    private void driveToZoneC2() {
//        robot.goToPosition(-20, 10, map, 0.7, true, false, this);
        robot.goToPosition(-59, 270, map, 0.7, true, false, this);
    }

    private void driveToZoneB2() {
//        robot.goToPosition(-20, 10, map, 0.7, true, false, this);
        robot.goToPosition(-91, 220, map, 0.9, true, false, this);
    }

    private void driveToZoneA2() {
        // robot.goToPosition(-20, 10, map, 0.7, true, false, this);
        robot.goToPosition(-100, 160, map, 0.7, true, false, this);
    }

    /* Each ring situation drives to a separate position although its the same x and y  */
    private void driveToShootingPosB() {
        map.getRingFlicker().setPosition(0);
        map.getBucket().setPosition(0.61);

        robot.goToPosition(robot.getXPos() - 1, 145, map, 0.7, true, false, this);

        map.getFlyWheel().setPower(1);


        gyro.turn(94, 0.7, map);

        robot.updateAngle(map);


    }

    private void driveToWobble2B() {
        robot.updateAngle(map);

        robot.goToPosition(-90, 37, map, 0.8, true, false, this);

        gyro.turn(180, 0.8, map);

        robot.setxPos(-90);
        robot.setyPos(35);

        robot.goToPosition(-75, robot.getYPos() - 1, map, 0.5, true, false, this);

        robot.updateAngle(map);

        robot.setxPos(-90);
        robot.setyPos(35);

    }

    private void pickUpRandomStackC() {
        map.getRingFlicker().setPosition(0.0);
        gyro.turn(180, 0.8, map);

        robot.goToPosition(robot.getXPos() - 1, 120, map, 0.8, true, false, this);

        map.getIntake().setPower(-1);

        robot.goToPosition(robot.getXPos() - 1, 110, map, 0.7, true, false, this);
        sleep(1500);

        map.getRingFlicker().setPosition(0.5);
        sleep(500);
        map.getRingFlicker().setPosition(0.0);

        robot.goToPosition(robot.getXPos() - 1, 105, map, 0.5, true, false, this);

        sleep(1500);
        map.getRingFlicker().setPosition(0.5);

        gyro.turn(180, 1, map);

        robot.updateAngle(map);
        robot.setxPos(-30);
        robot.setyPos(110);

        robot.goToPosition(-29, 120, map, 0.8, true, false, this);
        map.getIntake().setPower(1);
        map.getFlyWheel().setPower(1);
        map.getRingFlicker().setPosition(0);
        map.getBucket().setPosition(0.62);

    }

    private void goToShootingPosC() {
        map.getRingFlicker().setPosition(0);
        map.getBucket().setPosition(0.61);

        robot.goToPosition(-48, 130, map, 0.7, true, false, this);
        map.getFlyWheel().setPower(1);


        gyro.turn(85, 0.7, map);

        robot.updateAngle(map);

        robot.setxPos(-60);
        robot.setyPos(130);
    }

    private void shootRandomRings() {

        for (int i = 0; i < 2; i++) {

            map.getLaunchBlocker().setPosition(-0.7);
            map.getBucketPusher().setPosition(0);
            sleep(1000);

            map.getIntake().setPower(0);

            map.getBucketPusher().setPosition(1);
            map.getLaunchBlocker().setPosition(1);
            map.getRingHolder().setPosition(-1);

            sleep(1000);
        }

        map.getFlyWheel().setPower(0);
        map.getBucket().setPosition(0.5);
        resetEncoders();
    }

}






