package org.firstinspires.ftc.teamcode.Auto.AutoTesting;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

@Disabled
@TeleOp(name = "OdometryTesting", group = "Iterative Opmode")
public class EncoderTesting extends OpMode {
    // Declare OpMode members.
    DeviceMap map = new DeviceMap();
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        map.init(hardwareMap);

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        map.getLeftTop().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.getRightBottom().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.getLeftTop().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.getRightBottom().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double encX = map.getRightBottom().getCurrentPosition();
        double encY = map.getLeftTop().getCurrentPosition();
        telemetry.addData("Encoders:", "x:" + encX + "y:" + encY);
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        //TO BE TESTED
        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
        double rightX = gamepad1.right_stick_x;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        map.getLeftTop().setPower(v1);
        map.getRightTop().setPower(v2);
        map.getLeftBottom().setPower(v3);
        map.getRightBottom().setPower(v4);

        double encX = map.getLeftTop().getCurrentPosition();
        double encY = map.getRightBottom().getCurrentPosition();
        telemetry.addData("Encoders:", "x:" + encX + "y:" + encY);

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
