package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import org.firstinspires.ftc.teamcode.Util.MecanumDriveTrain;

import static java.lang.Math.PI;

@TeleOp(name = "Champs TeleOp")
public class TeleOp_Champs extends LinearOpMode {

    MecanumDriveTrain mdt;
    private ElapsedTime runtime = new ElapsedTime();
    DeviceMap map = new DeviceMap();
    double lastRuntime = 0;
    double timePass = getRuntime();


    @Override
    public void runOpMode(){
        map.init(hardwareMap);
        telemetry.addData("Status:", " Initialized");
        mdt = new MecanumDriveTrain(this, 0, 0, PI/2);

        map.getRingFlicker().setPosition(0);
        map.getRingHolder().setPosition(-1);
        map.getBucketPusher().setPosition(1);
        map.getBucket().setPosition(0.5);
        runtime.reset();

        waitForStart();

        while(opModeIsActive()){
//            double x = gamepad1.left_stick_x;
//            double y = -gamepad1.left_stick_y;
//            double right_stick_x = gamepad1.right_stick_x;
//            double multiplier  = gamepad1.left_trigger + 1;



            mdt.setControls(gamepad1.left_stick_y, gamepad1.left_stick_x, -gamepad1.right_stick_x);
//            if(gamepad1.left_bumper){
//                switchDirection = true;
//            }
//
//            if(gamepad1.right_bumper){
//                switchDirection = false;
//            }



            }
            //This may have to be changed


            map.getIntake().setPower(-gamepad2.left_stick_y);

            // Flywheel speed up and Down
            if(gamepad2.right_trigger > 0){
                double flyPower = map.getFlyWheel().getPower();
                timePass = getRuntime();
                map.getBucket().setPosition(0.61);


                if (Math.abs(timePass - lastRuntime) > 0.05){
                    if (flyPower < 1){
                        flyPower+= 0.05;
                        map.getFlyWheel().setPower((flyPower));
                        lastRuntime = timePass;
                    }
                }
            }
            if(gamepad2.left_trigger > 0){
                double flyPower = map.getFlyWheel().getPower();
                timePass = getRuntime();


                map.getBucket().setPosition(0.5);


                if (Math.abs(timePass - lastRuntime) > 0.05){
                    if (flyPower > 0){
                        flyPower-= 0.05;
                        map.getFlyWheel().setPower((flyPower));
                        lastRuntime = timePass;
                    }
                }
            }

            //Shooting Controls
            if(gamepad2.a ){
                map.getBucketPusher().setPosition(0);
                map.getLaunchBlocker().setPosition(-0.6);
            }else{
                map.getBucketPusher().setPosition(1);
                map.getLaunchBlocker().setPosition(1);
            }

            //Intake Flicker
            if(gamepad2.y){
                map.getRingFlicker().setPosition(0.5);
            }else{
                map.getRingFlicker().setPosition(0);
            }

            //Wobble Goal Arm
            if(gamepad2.dpad_down){
                map.getArm().setPower(0.5);
            }else if (gamepad2.dpad_up){
                map.getArm().setPower(-0.5);
            }else{
                map.getArm().setPower(0);
            }

            if(gamepad2.dpad_left){
                map.getLeftClaw().setPosition(-1);
                map.getRightClaw().setPosition(1);
            }

            if(gamepad2.dpad_right){
                map.getLeftClaw().setPosition(1);
                map.getRightClaw().setPosition(-1);
            }

            if(gamepad2.right_bumper) {
                map.getFlyWheel().setPower(-0.3);
            }

            if(gamepad2.left_bumper){
                map.getFlyWheel().setPower(0.0);
            }


        }




    }

