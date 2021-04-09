package org.firstinspires.ftc.teamcode.Experimental.Util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

public class Intake {

    private DeviceMap map;
    private DcMotor intake;
    private Servo bucket;
    private Servo bucketPusher;
    private Servo launchBlocker;
    private Servo ringFlicker;
    private Servo ringHolder;

    private double lastIntakePow = 0;
    private double lastLaunchBlocker = 0;
    private double lastBucket = 0;
    private double lastBucketPusher = 0;
    private double lastRingFlicker = 0;
    private double lastRingHolder = 0;
    private boolean on = true;
    private boolean reverse = false;
    private boolean forward = false;

    public Intake(LinearOpMode op, boolean isAuto){
        map = new DeviceMap();
        intake = map.getIntake();
        bucket = map.getBucket();
        bucketPusher = map.getBucketPusher();
        launchBlocker = map.getLaunchBlocker();
        ringFlicker = map.getRingFlicker();
        ringHolder = map.getRingHolder();

    }

    public void intakeOn(){
        setPower(1);
    }

    public void intakeOff(){
        setPower(0);
    }

    public void bucketUp(){
        setServoPosition(0.63, bucket, "bucket");
    }

    public void bucketDown(){
        setServoPosition(0.5, bucket, "bucket");
    }

    public void launchBlockerUp(){
        setServoPosition(1, launchBlocker, "launchBlocker");
    }

    public void launchBlockerDown(){
        setServoPosition(-0.7, launchBlocker, "launchBlocker");
    }

    public void bucketPusherUp(){
        setServoPosition(1, bucketPusher, "bucketPusher");
    }

    public void bucketPusherDown(){
        setServoPosition(0, bucketPusher, "bucketPusher");
    }

    public void ringFlickerUp(){
        setServoPosition(0.5, ringFlicker, "ringFlicker");
    }

    public void ringFlickerDown(){
        setServoPosition(0, ringFlicker, "ringFlicker");
    }

    public void ringHolderUp(){
        setServoPosition(1, ringHolder, "ringHolder");
    }

    public void ringHolderDown(){
        setServoPosition(-1, ringHolder, "ringHolder");
    }

    public void setPower(double power){
        if(power != lastIntakePow) {
            intake.setPower(power);
            on = power != 0;
            forward = power > 0;
            reverse = power < 0;
            lastIntakePow = power;
        }
    }




    public void setServoPosition(double position, Servo servo, String servoType){
         if(servoType.equals("bucket")) {
             if(position != lastBucket) {
                 servo.setPosition(position);
                 lastBucket = position;
             }

         }

        if(servoType.equals("bucketPusher")) {
            if(position != lastBucketPusher) {
                servo.setPosition(position);
                lastBucketPusher = position;
            }

        }

        if(servoType.equals("launchBlocker")) {
            if(position != lastLaunchBlocker) {
                servo.setPosition(position);
                lastLaunchBlocker = position;
            }

        }

        if(servoType.equals("ringFlicker")) {
            if(position != lastRingFlicker) {
                servo.setPosition(position);
                lastRingFlicker = position;
            }

        }

        if(servoType.equals("ringHolder")) {
            if(position != lastRingHolder) {
                servo.setPosition(position);
                lastRingHolder = position;
            }

        }
    }




}
