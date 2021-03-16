package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Auto.imuDrive;
import org.firstinspires.ftc.teamcode.Auto.newCoordinateSystem;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

import java.util.List;

@Autonomous(name = "Washieu Is Why We Need This", group = "Emergency")
public class EmergencyAuto extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    private static final String VUFORIA_KEY =
            "AarEEQn/////AAABmTY7WIRMk0JfvS6zOFAH7hpR83bPgnanU0IaXelPm37J2UTuq1zA+9GKHfyUSvyW5D129EmfhQHZzj9HaLFIrLfgsVZVzn3UW/EVPsI04l+b4a/WVGND74ox6Q0AySr6Ew+bcHdDo6V/08+rrIaeRM0c+oXekVE9JOmXnixp9sK23o258rbvuUAwcixAXAkhJQMIPluwhMNFAXqTYmrdNriiRbeXbBcNSokBQ51Z6qIf1VfrshpPwtJYaUyg/MtVlMcx3UhZfvUQNioFxB6iXQCEr9fhtP2X6lLqKE66AUR9CdIMpFuZ9y8z8uFtUv81soa7vAssZWXCkp+L9xkJRv91mmFI25KeEoZUWv29XXDz";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public enum rings { FOUR, ONE, NONE };

    rings pos;


    @Override
    public void runOpMode() {
        DeviceMap map = new DeviceMap();
        imuDrive gyro = new imuDrive();
        newCoordinateSystem robot = new newCoordinateSystem();
        initVuforia();
        initTfod();

        map.init(hardwareMap);

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(2.5, 16.0 / 9.0);
        }

        if (!opModeIsActive()) {
            while (!opModeIsActive()) {
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());

                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());

                            if((String.format("(%d)", recognition.getLabel())) == "Quad"){
                                pos = rings.FOUR;
                            } else if((String.format("(%d)", recognition.getLabel())) == "Single") {
                                pos = rings.ONE;
                                // Below if-statement needed to changed
                            } else if((String.format("(%d)", recognition.getLabel())) == ""){
                                pos = rings.NONE;
                            }
                        }
                        telemetry.update();
                    }
                }


            }
        }



        byte AXIS_MAP_CONFIG_BYTE = 0x6; //This is what to write to the AXIS_MAP_CONFIG register to swap x and z axes
        byte AXIS_MAP_SIGN_BYTE = 0x1; //This is what to write to the AXIS_MAP_SIGN register to negate the z axis
        //Need to be in CONFIG mode to write to registers
        map.getImu().write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.CONFIG.bVal & 0x0F);
        sleep(100); //Changing modes requires a delay before doing anything else
        //Write to the AXIS_MAP_CONFIG register
        map.getImu().write8(BNO055IMU.Register.AXIS_MAP_CONFIG, AXIS_MAP_CONFIG_BYTE & 0x0F);
        //Write to the AXIS_MAP_SIGN register
        map.getImu().write8(BNO055IMU.Register.AXIS_MAP_SIGN, AXIS_MAP_SIGN_BYTE & 0x0F);
        //Need to change back into the IMU mode to use the gyro
        map.getImu().write8(BNO055IMU.Register.OPR_MODE, BNO055IMU.SensorMode.IMU.bVal & 0x0F);
        sleep(100); //Changing modes again requires a delay


        telemetry.addData("", "ready");
        telemetry.update();



        resetEncoders(map);


        waitForStart();

        robot.initializeCoords(map, -50, 0);

        telemetry.addData("X", robot.getXPos());
        telemetry.addData("Y", robot.getYPos());
        telemetry.addData("Angle", robot.getCurrentAngle());
        telemetry.update();

        switch(pos){
            case FOUR:
                /* robot.goToPosition(); */
            case ONE:
                /* robot.goToPosition(); */
            case NONE:
                /* robot.goToPosition(); */

        }



    }

    private void initVuforia(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod(){
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
    private void resetEncoders(DeviceMap map){
        map.getRightBottom().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.getLeftBottom().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.getRightTop().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.getLeftTop().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        map.getRightBottom().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.getLeftBottom().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.getRightTop().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        map.getLeftTop().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

}

