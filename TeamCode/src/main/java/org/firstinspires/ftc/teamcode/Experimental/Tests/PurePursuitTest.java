package org.firstinspires.ftc.teamcode.Experimental.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Path;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Target;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Waypoint;
import org.firstinspires.ftc.teamcode.Experimental.Util.Robot;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.PI;
@TeleOp(name = "PURE_PURSUIT TEST")
public class PurePursuitTest extends LinearOpMode {



    @Override
    public void runOpMode(){
        Robot robot = new Robot(this, 0, 0 , 0, true);
        robot.logger.startLogging(true);

        Waypoint[] goToRandomWayPoint = new Waypoint[] {
                new Waypoint(0, 0, PI/2,  30, 30, 0, 0),
                new Waypoint(30, 70, PI/2, 5, 30, 0, 2),
        };

        Path goToRandomPath = new Path(new ArrayList<>(Arrays.asList(goToRandomWayPoint)));

        ElapsedTime time = new ElapsedTime();

        while(opModeIsActive()){
//            robot.setTargetPoint(new Target(goToRandomPath.getRobotPose(Math.min(time.seconds(), 2))).thetaW0(PI/2).xKp(0.5).thetaKp(2.5));
        }
    }
}
