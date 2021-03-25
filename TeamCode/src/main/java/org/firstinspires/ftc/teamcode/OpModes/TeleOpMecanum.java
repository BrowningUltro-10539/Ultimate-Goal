package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

@TeleOp(name = "TeleOpMecanum", group = "MecanumDrive")
public class TeleOpMecanum extends OpMode {
    MecanumDrive driver;
    private ElapsedTime runtime = new ElapsedTime();
    DeviceMap map = new DeviceMap();
    double lastRuntime = 0;
    double timePass = getRuntime();
    boolean switchDirection = true;

    @Override
    public void init(){
        map.init(hardwareMap);
        telemetry.addData("Status:", " Initialized");

        driver = new MecanumDrive();


    }

    @Override
    public void init_loop(){
    }

    @Override
    public void start(){
        map.getRingFlicker().setPosition(0);
        map.getRingHolder().setPosition(-1);
        map.getBucketPusher().setPosition(1);
        map.getBucket().setPosition(0.5);
        runtime.reset();
    }


    @Override
    public void loop(){
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double right_stick_x = gamepad1.right_stick_x;
        double multiplier  = gamepad1.left_trigger + 1;

        //Music comes later

        if(gamepad1.left_trigger>0){
            x=x/2;
            y=y/2;
        }

        if(gamepad1.left_bumper){
            switchDirection = true;
        }

        if(gamepad1.right_bumper){
            switchDirection = false;
        }

        if(switchDirection){
            driver.move(map, y/multiplier, x/multiplier, right_stick_x/multiplier);
        }else{
            driver.move(map,x/multiplier, y/multiplier, right_stick_x/multiplier);

        }
        //This may have to be changed


        map.getIntake().setPower(-gamepad2.left_stick_y);

        // Flywheel speed up and Down
        if(gamepad2.right_trigger > 0){
            double flyPower = map.getFlyWheel().getPower();
            timePass = getRuntime();
            map.getBucket().setPosition(0.63);


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
            map.getRingHolder().setPosition(1);
            double startingTime = getRuntime();
            while(Math.abs(startingTime - getRuntime()) < 0.3){
                boolean waiting = true;
            }
            map.getBucketPusher().setPosition(0);
            map.getLaunchBlocker().setPosition(-0.6);
        }
        else if(gamepad2.b ){
            map.getRingHolder().setPosition(-1);
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


/*
        //Automatic Flywheel Shooting
        if(gamepad2.left_bumper){
            timePass = getRuntime();

            //Launch #1
            if(!shootingSequenceStarted){
                map.getLaunchBlocker().setPosition(-1);
                shootingSequenceStarted=true;
                timePass=lastRuntime;
            }
            if(Math.abs(timePass-lastRuntime)>0.5 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<1){
                map.getBucketPusher().setPosition(0);
            }
            if(Math.abs(timePass-lastRuntime)>0.7 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<1.5){
                map.getLaunchBlocker().setPosition(1);
            }
            if(Math.abs(timePass-lastRuntime)>1 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<2){
                map.getBucketPusher().setPosition(1);
            }

            //Launch #2
            if(Math.abs(timePass-lastRuntime)>2.5 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<3){
                map.getLaunchBlocker().setPosition(-1);
            }
            if(Math.abs(timePass-lastRuntime)>3 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<3.5){
                map.getBucketPusher().setPosition(0);
            }
            if(Math.abs(timePass-lastRuntime)>3.2 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<4){
                map.getLaunchBlocker().setPosition(1);
            }
            if(Math.abs(timePass-lastRuntime)>3.5 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<4.5){
                map.getBucketPusher().setPosition(1);
            }

            //Launch #3
            if(Math.abs(timePass-lastRuntime)>5 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<5.5){
                map.getLaunchBlocker().setPosition(-1);
            }
            if(Math.abs(timePass-lastRuntime)>5.5 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<6){
                map.getBucketPusher().setPosition(0);
            }
            if(Math.abs(timePass-lastRuntime)>5.7 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<6.5){
                map.getLaunchBlocker().setPosition(1);
            }
            if(Math.abs(timePass-lastRuntime)>6 && shootingSequenceStarted && Math.abs(timePass-lastRuntime)<7.5){
                map.getBucketPusher().setPosition(1);
                shootingSequenceStarted = false;
            }
        }

        //Reset for emergencies
        if(gamepad2.x){
            shootingSequenceStarted = false;
            map.getBucketPusher().setPosition(1);
            map.getLaunchBlocker().setPosition(1);
        }
*/


    }


}


