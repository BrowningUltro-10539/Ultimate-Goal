package org.firstinspires.ftc.teamcode.Experimental.Vision.RingLocator;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Experimental.Vision.Ring;

import java.util.ArrayList;

import static java.lang.Math.PI;

@TeleOp(name = "EXP: Ring Locator Test")

public class RingLocatorTest extends LinearOpMode {
    private RingLocator detector;
    private ArrayList<Ring> rings;

    public static double x = -50;
    public static double y = 63;
    public static double theta = PI/2;

    @Override
    public void runOpMode(){
        detector = new RingLocator(this);
        detector.start();

        waitForStart();

        while(opModeIsActive()){
            rings = detector.getRings(x, y, theta);
            for (int i = 0; i < rings.size(); i++) {
                if (i == 0) {
                    telemetry.addData("Ring Position", rings.get(i));
                } else if (i == 1) {
                    telemetry.addData("Ring Position", rings.get(i));
                } else if (i == 2) {
                    telemetry.addData("Ring Position", rings.get(i));
                }

                telemetry.update();
            }

        }

        detector.stop();
    }
}
