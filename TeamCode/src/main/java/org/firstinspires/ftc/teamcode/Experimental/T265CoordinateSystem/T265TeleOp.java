//package org.firstinspires.ftc.teamcode.Experimental.T265CoordinateSystem;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
//import org.firstinspires.ftc.teamcode.Util.MecanumDrive;
//
//@TeleOp(name = "T265Test")
//public class T265TeleOp extends LinearOpMode {
//    MecanumDrive driver;
//    double startingX = 0;
//    double startingY = 0;
//
//    double startingAngle = 0;
//
//    DeviceMap map = null;
//
//    @Override
//    public void runOpMode(){
//
//        map = new DeviceMap();
//        driver = new MecanumDrive();
//        t265CoordinateSystem coords = new t265CoordinateSystem();
//        map.init(hardwareMap);
//
//        coords.intializeSystem(startingY, startingX, startingAngle, hardwareMap, this);
//
//        waitForStart();
//        while(opModeIsActive()){
//            double x = gamepad1.left_stick_x;
//            double y = -gamepad1.left_stick_y;
//            double right_stick_x = gamepad1.right_stick_x;
//            double multiplier  = gamepad1.left_trigger + 1;
//
//            driver.move(map,x/multiplier, y/multiplier, right_stick_x/multiplier);
//            coords.t265update(this);
//            if(isStopRequested()){
//                coords.stop();
//            }
//
//
//        }
//
//
//
//
//    }
//}
//
