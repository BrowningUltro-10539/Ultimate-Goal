package org.firstinspires.ftc.teamcode.RobotInfo;


public class Constants {
    /* Class that contains all of the constant variables that we may need for our OpModes.
       Helps minimize confusion when changing the value of a servo position or how far
       we want our robot to go to if we are performing the same type of operations for
       any number of situations. */

    // Init: Intake
    private int INIT_LEFT_CLAW_POSITION = -1;
    private int INIT_RIGHT_CLAW_POSITION = 1;
    private int INIT_RING_FLICKER_POSITION = 0;
    private int INIT_RING_HOLDER_POSITION = -1;
    private int INIT_BUCKET_PUSHER_POSITION = 1;
    private double INIT_BUCKET_POSITION = 0.5;


    // Started: Intake
    private double START_FLYWHEEL_POSITION = -0.3;
    private double START_RF_POSITION = 0.5;
    private int START_LAUNCH_BLOCKER_POSITION = 1;
    private double START_BUCKET_POSITION = 0.63;

    // Shooting: Intake
    private int SHOOT_RH_POS = 1;
    private double SHOOT_LB_POS = -0.7;
    private int SHOOT_BP_POS = 0;

    // After-Shoot Intake:
    private double AS_BUCKET_POS = 0.5;


}
