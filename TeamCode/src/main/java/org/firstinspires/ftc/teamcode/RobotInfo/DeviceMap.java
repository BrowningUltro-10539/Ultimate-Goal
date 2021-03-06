package org.firstinspires.ftc.teamcode.RobotInfo;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.openftc.easyopencv.OpenCvCamera;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class DeviceMap {

    private DcMotor leftTop = null;
    private DcMotor rightTop = null;
    private DcMotor leftBottom = null;
    private DcMotor rightBottom = null;
    private DcMotor flyWheel = null;
    private DcMotor intake = null;
    private DcMotor arm = null;

    private Servo bucket = null;
    private Servo bucketPusher = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;
    private Servo launchBlocker = null;
    private Servo ringFlicker = null;
    private BNO055IMU imu;



    /* Add servos on build day */

    /* Local OpMode Members*/
    HardwareMap deviceMap = null;
    private ElapsedTime period = new ElapsedTime();

    public DeviceMap(){
    }

    public void init(HardwareMap hwMap) {
        deviceMap = hwMap;

        leftTop = deviceMap.get(DcMotor.class, "LT");
        rightTop = deviceMap.get(DcMotor.class, "RT");
        leftBottom = deviceMap.get(DcMotor.class, "LB");
        rightBottom = deviceMap.get(DcMotor.class, "RB");

        flyWheel = deviceMap.get(DcMotor.class, "flywheel");

        intake = deviceMap.get(DcMotor.class, "intake");

        arm = deviceMap.get(DcMotor.class,"arm");


        bucket = deviceMap.get(Servo.class, "bucket");
        bucketPusher = deviceMap.get(Servo.class,"bucketpusher");
        leftClaw  = deviceMap.get(Servo.class,"leftclaw");
        rightClaw = deviceMap.get(Servo.class,"rightclaw");
        launchBlocker = deviceMap.get(Servo.class, "launchblocker");
        ringFlicker = deviceMap.get(Servo.class,"ringflicker");

        /* Might have to change this depending on the motors */
        leftTop.setDirection(DcMotor.Direction.REVERSE);
        leftBottom.setDirection(DcMotor.Direction.REVERSE);
        rightTop.setDirection(DcMotor.Direction.FORWARD);
        rightBottom.setDirection(DcMotor.Direction.FORWARD);

        flyWheel.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setDirection(DcMotorSimple.Direction.FORWARD);


        leftTop.setPower(0);
        leftBottom.setPower(0);
        rightTop.setPower(0);
        rightBottom.setPower(0);
        flyWheel.setPower(0);

        intake.setPower(0);

        leftTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        leftTop.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBottom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBottom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightTop.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flyWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        imu = deviceMap.get(BNO055IMU.class, "imu");


    }

    public DcMotor getLeftTop(){
        return leftTop;
    }

    public DcMotor getRightTop(){
        return rightTop;
    }

    public DcMotor getLeftBottom(){
        return leftBottom;
    }

    public DcMotor getRightBottom(){
        return rightBottom;
    }

    public DcMotor getFlyWheel() {return flyWheel;}

    public DcMotor getArm(){return arm;}

    public BNO055IMU getImu() {
        return imu;
    }

    public DcMotor getIntake(){
        return intake;
    }

    public Servo getBucket(){return bucket; }

    public Servo getBucketPusher(){return bucketPusher;}

    public Servo getLeftClaw(){return leftClaw;}

    public Servo getRightClaw(){return rightClaw;}

    public Servo getLaunchBlocker(){return  launchBlocker;}

    public Servo getRingFlicker(){return ringFlicker;}

}
