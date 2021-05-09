package org.firstinspires.ftc.teamcode.Experimental.T265CoordinateSystem;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

@Autonomous(name="t265 coordinates tester", group="Auto")
public class t265CoordinateSystemTester extends LinearOpMode {

    double startingX = 0;
    double startingY = 0;

    double startingAngle = 0;

    DeviceMap map = null;

    @Override
    public void runOpMode(){
        map = new DeviceMap();
        t265CoordinateSystem coords = new t265CoordinateSystem();
        map.init(hardwareMap);

        coords.intializeSystem(startingX, startingY, startingAngle, hardwareMap, this);

        waitForStart();
        while(opModeIsActive()){
            coords.t265update(this);
            sleep(50);
        }



    }
}
