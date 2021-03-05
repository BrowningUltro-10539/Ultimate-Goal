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
    public void start(){ runtime.reset();}

    @Override
    public void loop(){
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double right_stick_x = -gamepad1.right_stick_x;
        double multiplier  = gamepad1.left_trigger + 1;

        //Mecanum Drive
        driver.move(x/multiplier, y/multiplier, right_stick_x/multiplier);


        map.getIntake().setPower(-gamepad2.left_stick_y);

        if(gamepad2.right_trigger > 0){
            double flyPower = map.getFlyWheel().getPower();
            timePass = getRuntime();

            map.getBucketPusher().setPosition(-1);

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

            map.getBucketPusher().setPosition(1);


            if (Math.abs(timePass - lastRuntime) > 0.05){
                if (flyPower > 0){
                    flyPower-= 0.05;
                    map.getFlyWheel().setPower((flyPower));
                    lastRuntime = timePass;
                }
            }
        }


        if(gamepad2.a){
            map.getBucket().setPosition(0);
        }
        if(gamepad2.b){
            map.getBucket().setPosition(0.25);
        }

        //ADD MORE SERVOS OR MOTORS BASED ON WHAT I AM TOLD

    }

}


