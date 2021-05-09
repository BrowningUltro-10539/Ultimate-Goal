package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto.OdometryDrive;
import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.Auto.newCoordinateSystem;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.firstinspires.ftc.teamcode.Vision.OpenCVBoxes;
import org.firstinspires.ftc.teamcode.Vision.Status;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name="AutoOpModeOdometry", group="Auto")
public class AutoOpMode extends LinearOpMode {

    protected Status pos;
    protected OpenCVBoxes pipeline1;

    /* @ERIC After we record videos, I'm going to create a base class that allows us to init this  */


    @Override
    public void runOpMode() {

        DeviceMap map = new DeviceMap();
        imuDrive gyro = new imuDrive();
        OdometryDrive drive = new OdometryDrive();
        newCoordinateSystem robot = new newCoordinateSystem();
        map.init(hardwareMap);
        map.setupOpenCV(hardwareMap);
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



        resetEncoders(map);

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


        switch(pos){
            case FOUR: // Zone C

                map.deactivateOpenCV();

                robot.initializeCoords(map, -50,0);
                sleep(1000);

                robot.goToPosition(-20,1, map,0.5,true, false);

                sleep(1000);

                robot.goToPosition(-19,270, map, 0.5, true, false);

                map.getArm().setPower(1);

                sleep(1000);

                map.getArm().setPower(0);

                map.getLeftClaw().setPosition(1);
                map.getRightClaw().setPosition(-1);

                sleep(500);

                map.getArm().setPower(-1);

                sleep(500);

                map.getArm().setPower(0);
                robot.goToPosition(-90,120, map, 0.5, true, false);

                drive.moveUntil("Backward", 60, 0.5, map, true);

                gyro.turn(84, 0.5, map);

                map.getFlyWheel().setPower(-0.3);

                map.getRingFlicker().setPosition(0.5);
                sleep(500);

                map.getLaunchBlocker().setPosition(1);

                map.getRingFlicker().setPosition(0);
                sleep(500);

                map.getBucket().setPosition(0.63);

                sleep(100);

                map.getFlyWheel().setPower(1);
                sleep(500);


                for(int i=0; i<3; i++){
                    map.getRingHolder().setPosition(1);
                    sleep(300);
                    map.getLaunchBlocker().setPosition(-0.7);
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

                drive.moveUntil("Right", 110, 0.5, map, true );



                break;

            case ONE: // Zone B

                map.deactivateOpenCV();

                robot.initializeCoords(map, -50,0);
                sleep(1000);

                robot.goToPosition(-20,1, map,0.5,true, false);

                sleep(1000);

                robot.goToPosition(-60,180, map, 0.5, true, false);

                map.getArm().setPower(1);

                sleep(1000);

                map.getArm().setPower(0);

                map.getLeftClaw().setPosition(1);
                map.getRightClaw().setPosition(-1);

                sleep(500);

                map.getArm().setPower(-1);

                sleep(500);

                map.getArm().setPower(0);

                robot.goToPosition(-90, 120, map, 0.5, true, false);

                drive.moveUntil("Backward", 60, 0.5, map, true);

                gyro.turn(85, 0.5, map);


                map.getFlyWheel().setPower(-0.3);

                map.getRingFlicker().setPosition(0.5);
                sleep(500);

                map.getLaunchBlocker().setPosition(1);
                map.getRingFlicker().setPosition(0);
                sleep(500);


                map.getFlyWheel().setPower(1);
                sleep(500);



                map.getBucket().setPosition(0.63);

                sleep(1000);


                for(int i=0; i<3; i++){
                    map.getRingHolder().setPosition(1);
                    sleep(300);
                    map.getLaunchBlocker().setPosition(-0.7);
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

                drive.moveUntil("Right", 110, 0.5, map, true );



                break;


            case NONE: // Zone A
                map.deactivateOpenCV();

                robot.initializeCoords(map, -50,0);
                sleep(1000);

                robot.goToPosition(-20,1, map,0.5,true, false);

                sleep(1000);

                robot.goToPosition(-19,130, map, 0.5, true, false);

                map.getArm().setPower(1);

                sleep(1000);

                map.getArm().setPower(0);

                map.getLeftClaw().setPosition(1);
                map.getRightClaw().setPosition(-1);

                sleep(500);

                map.getArm().setPower(-1);

                sleep(500);

                map.getArm().setPower(0);

                robot.goToPosition(-90,60, map, 0.5, true, false);

                gyro.turn(84, 0.5, map);

                map.getFlyWheel().setPower(-0.3);


                robot.updateAngle(map);

                sleep(500);

                map.getRingFlicker().setPosition(0.5);
                sleep(500);

                map.getLaunchBlocker().setPosition(1);
                map.getRingFlicker().setPosition(0);
                sleep(500);

                map.getFlyWheel().setPower(1);

                sleep(500);



                map.getBucket().setPosition(0.63);

                sleep(2000);

                for(int i=0; i<3; i++){
                    map.getRingHolder().setPosition(1);
                    sleep(300);
                    map.getLaunchBlocker().setPosition(-0.7);
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

                drive.moveUntil("Right", 100, 0.5, map, true );

                break;

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

