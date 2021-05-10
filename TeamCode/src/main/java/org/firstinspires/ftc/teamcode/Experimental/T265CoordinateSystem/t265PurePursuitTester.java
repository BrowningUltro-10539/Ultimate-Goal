package org.firstinspires.ftc.teamcode.Experimental.T265CoordinateSystem;


import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.SimpleMotor;
import com.arcrobotics.ftclib.purepursuit.Path;
import com.arcrobotics.ftclib.purepursuit.waypoints.EndWaypoint;
import com.arcrobotics.ftclib.purepursuit.waypoints.GeneralWaypoint;
import com.arcrobotics.ftclib.purepursuit.waypoints.PointTurnWaypoint;
import com.arcrobotics.ftclib.purepursuit.waypoints.StartWaypoint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Waypoint;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

import java.util.ArrayList;

@Autonomous(name="t265 coordinates tester", group="Auto")
public class t265PurePursuitTester extends LinearOpMode {


    /**
     * This works by using the back left corner of the field as (0,0).
     * REMEMBER: Pure pursuit works with inches, but the T265 Camera works with meters.
     * THEREFORE: Make sure you are using the correct units everywhere. This class's init variables should be INCHES.
     */

    double startingX = 20;
    double startingY = 0;

    double startingAngle = 0;

    DeviceMap map = null;

    private static final double INCHES_TO_CM = 2.54;

    private static final double CM_TO_INCHES = 1/2.54;

    private Motor leftFront = null;
    private Motor rightFront = null;
    private Motor leftBack = null;
    private Motor rightBack = null;



    @Override
    public void runOpMode(){
        map = new DeviceMap();
        t265CoordinateSystem coords = new t265CoordinateSystem();
        map.ftcLibInit(hardwareMap);

        //For Auto Only
        leftFront = new SimpleMotor("LT", hardwareMap);
        leftBack = new SimpleMotor("LB", hardwareMap);
        rightFront = new SimpleMotor("RT", hardwareMap);
        rightBack = new SimpleMotor("RB", hardwareMap);

        MecanumDrive drive = new MecanumDrive(leftFront, rightFront, leftBack, rightBack);

        coords.intializeSystem(startingX * INCHES_TO_CM, startingY * INCHES_TO_CM, startingAngle, hardwareMap, this);

        waitForStart();

        StartWaypoint p1 = new StartWaypoint(coords.getxPos(), coords.getyPos());

        EndWaypoint p2 = new EndWaypoint(40, 40, Math.PI/2, 0.5, 0.5, 30,0.8, 1);

        Path path = new Path(p1, p2);

        path.init();

        while(!path.isFinished()){

            double speeds[] = path.loop(coords.getxPos() * CM_TO_INCHES, coords.getyPos() * CM_TO_INCHES, coords.getCurrentAngleRadians());

            drive.driveRobotCentric(speeds[0], speeds[1], speeds[2]);

            coords.t265update(this);
        }
        drive.stop();



    }


}
