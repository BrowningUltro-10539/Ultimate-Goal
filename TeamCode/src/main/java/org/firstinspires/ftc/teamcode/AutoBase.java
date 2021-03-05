//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//import org.firstinspires.ftc.teamcode.Auto.OdometryDrive;
//import org.firstinspires.ftc.teamcode.Auto.imuDrive;
//import org.firstinspires.ftc.teamcode.Auto.newCoordinateSystem;
//import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
//
//@Autonomous(name="AutoBase", group="Auto")
//@Disabled
//public abstract class AutoBase extends LinearOpMode {
//    protected String CAMERA_SYSTEM = "WEBCAM";
//    protected MecanumDrive driver;
//    protected OdometryDrive drive;
//    protected imuDrive gyro;
//    protected newCoordinateSystem robot;
//
//
//    public void preInit(){
//        DeviceMap map = new DeviceMap();
//
//
//        driver = new MecanumDrive();
//        drive = new OdometryDrive();
//        gyro = new imuDrive();
//        robot = new newCoordinateSystem();
//
//        map.init(hardwareMap);
//
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
//
//        map.getImu().initialize(parameters);
//
//
//        //FIX IMU FOR VERTICAL MOUNTING
//        byte AXIS_MAP_CONFIG_BYTE = 0x6; //This is what to write to the AXIS_MAP_CONFIG register to swap x and z axes
//        byte AXIS_MAP_SIGN_BYTE = 0x1; //This is what to write to the AXIS_MAP_SIGN register to negate the z axis
//        //Need to be in CONFIG mode to write to registers
//        map.getImu().write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.CONFIG.bVal & 0x0F);
//        sleep(100); //Changing modes requires a delay before doing anything else
//        //Write to the AXIS_MAP_CONFIG register
//        map.getImu().write8(BNO055IMU.Register.AXIS_MAP_CONFIG, AXIS_MAP_CONFIG_BYTE & 0x0F);
//        //Write to the AXIS_MAP_SIGN register
//        map.getImu().write8(BNO055IMU.Register.AXIS_MAP_SIGN, AXIS_MAP_SIGN_BYTE & 0x0F);
//        //Need to change back into the IMU mode to use the gyro
//        map.getImu().write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.IMU.bVal & 0x0F);
//        sleep(100); //Changing modes again requires a delay
//
//        telemetry.addData("", "ready");
//        telemetry.update();
//
//        resetEncoders(map);
//
//        waitForStart();
//
//        robot.initializeCoords(map, 0, 0);
//
//        telemetry.addLine("DriverMap and MecanumDrive setup");
//    }
//
//    public void afterStop(){
//        DeviceMap map = new DeviceMap();
//        resetEncoders(map);
//    }
//
//    @Override
//    public void runOpMode(){
//        preInit();
//
//
//    }
//
//    public abstract void beforeLoop();
//    public abstract void run();
//
//    private void resetEncoders(DeviceMap map){
//        map.getRightBottom().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        map.getLeftBottom().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        map.getRightTop().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        map.getLeftTop().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        sleep(100);
//        map.getRightBottom().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        map.getLeftBottom().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        map.getRightTop().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        map.getLeftTop().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//    }
//
//    private void stopAndReset(){
//        stop();
//
//        while(!isStarted())
//            beforeLoop();
//
//        waitForStart();
//
//        run();
//
//        afterStop();
//    }
//
//}
