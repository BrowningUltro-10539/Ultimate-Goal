package org.firstinspires.ftc.teamcode.Experimental;

import android.annotation.SuppressLint;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Experimental.Debugging.Logger;
import org.firstinspires.ftc.teamcode.Experimental.Vision.Ring;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Path;
import org.firstinspires.ftc.teamcode.Experimental.Pathing.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.PI;

public class Robot {
    public MecanumDriveTrain driveTrain;
    public Logger logger;

    private ElapsedTime profiler;
    private VoltageSensor battery;
    private boolean startVoltTooLow = false;

    private final int loggerUpdatePeriod = 2;
    private final int sensorUpdatePeriod = 15;
    private final double xyTolerance = 1;
    private final double thetaTolerance = PI/35;
    private double odoCovariance = 1;


    public static void log(String message) {
        Log.w("robot-log", message);
    }

}

