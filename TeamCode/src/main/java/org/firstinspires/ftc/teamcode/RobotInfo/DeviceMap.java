package org.firstinspires.ftc.teamcode.RobotInfo;

import android.content.Context;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.openftc.easyopencv.OpenCvCamera;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvInternalCamera;


//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public final class DeviceMap {


    private DcMotor leftTop = null;
    private DcMotor rightTop = null;
    private DcMotor leftBottom = null;
    private DcMotor rightBottom = null;
    private DcMotor flyWheel = null;
    private DcMotor intake = null;
    private DcMotor arm = null;

    private DcMotor[] driveMotors;
    private DcMotor[] intakeMotors;
    private DcMotor[] allMotors;

    private Servo[] servos;

    private Servo bucket = null;
    private Servo bucketPusher = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;
    private Servo launchBlocker = null;
    private Servo ringFlicker = null;
    private Servo ringHolder = null;

    private BNO055IMU imu;

    /* Computer Vision */
    private TFObjectDetector tfod;
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    private static final String VUFORIA_KEY =
            "AarEEQn/////AAABmTY7WIRMk0JfvS6zOFAH7hpR83bPgnanU0IaXelPm37J2UTuq1zA+9GKHfyUSvyW5D129EmfhQHZzj9HaLFIrLfgsVZVzn3UW/EVPsI04l+b4a/WVGND74ox6Q0AySr6Ew+bcHdDo6V/08+rrIaeRM0c+oXekVE9JOmXnixp9sK23o258rbvuUAwcixAXAkhJQMIPluwhMNFAXqTYmrdNriiRbeXbBcNSokBQ51Z6qIf1VfrshpPwtJYaUyg/MtVlMcx3UhZfvUQNioFxB6iXQCEr9fhtP2X6lLqKE66AUR9CdIMpFuZ9y8z8uFtUv81soa7vAssZWXCkp+L9xkJRv91mmFI25KeEoZUWv29XXDz";
    private VuforiaLocalizer vuforia;

    private OpenCvCamera camera;


    /* Local OpMode Members*/
    HardwareMap map = null;
    private ElapsedTime period = new ElapsedTime();

    public DeviceMap(){
    }

    public void init(HardwareMap hardwareMap){
        setUpMotors(hardwareMap);
        setUpDriveMotors(hardwareMap);
        setUpImu(hardwareMap);
        setupServos(hardwareMap);
        setUpImu(hardwareMap);
        //initTfod(hardwareMap);
        //initVuforia();
    }


    public void setUpDriveMotors(HardwareMap map){
        //telemetry.addLine("Setting Up Drive Motors");
        leftTop = map.get(DcMotor.class, "LT");
        rightTop = map.get(DcMotor.class, "RT");
        leftBottom = map.get(DcMotor.class, "LB");
        rightBottom = map.get(DcMotor.class, "RB");

        this.driveMotors = new DcMotor[]{leftTop, rightTop, leftBottom, rightBottom};

        leftTop.setDirection(DcMotor.Direction.REVERSE);
        leftBottom.setDirection(DcMotor.Direction.REVERSE);
        rightTop.setDirection(DcMotor.Direction.FORWARD);
        rightBottom.setDirection(DcMotor.Direction.FORWARD);

        for(DcMotor motor: this.driveMotors){
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setPower(0);
        }
    }

    public void setUpMotors(HardwareMap map){
        flyWheel = map.get(DcMotor.class, "flywheel");
        intake = map.get(DcMotor.class, "intake");
        arm = map.get(DcMotor.class,"arm");

        /* Add arm */
        flyWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.intakeMotors = new DcMotor[]{flyWheel, intake, arm};

        for(DcMotor motor : this.intakeMotors){
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void setupServos(HardwareMap map){
        //telemetry.addLine("Setting Servos Up");
        //telemetry.update();

        bucket = map.get(Servo.class, "bucket");
        bucketPusher = map.get(Servo.class,"bucketpusher");
        leftClaw  = map.get(Servo.class,"leftclaw");
        rightClaw = map.get(Servo.class,"rightclaw");
        launchBlocker = map.get(Servo.class, "launchblocker");
        ringFlicker = map.get(Servo.class,"ringflicker");
        ringHolder = map.get(Servo.class, "ringholder");

        this.servos = new Servo[]{bucket, bucketPusher, leftClaw, rightClaw, launchBlocker, ringFlicker, ringHolder};

    }

    public void setUpImu(HardwareMap map){
        imu = map.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);
    }

    public void initVuforia(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;


        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public void initTfod(HardwareMap map) {

        int tfodMonitorViewId = map.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", map.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        tfod.activate();

    }

    public void setupOpenCV(HardwareMap map){
        Context appContext = map.appContext;
        int cameraMonitorViewId = appContext.getResources().getIdentifier("cameraMonitorViewId", "id", appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        camera.openCameraDevice();
    }

    public void deactivateOpenCV(){
        if(camera != null){
            camera.stopStreaming();
            camera.closeCameraDevice();
        }
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

    public Servo getRingHolder(){return ringHolder;}

    public TFObjectDetector getTfod(){
        return tfod;
    }

    public OpenCvCamera getCamera(){ return camera; }
}
