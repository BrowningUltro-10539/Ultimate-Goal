package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import org.firstinspires.ftc.teamcode.Vision.Status;

import java.util.List;

public class RingPipeline implements ObjectIdentification{
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";


    private VuforiaLocalizer vuforia;

    private HardwareMap hardwareMap = null;
    private Telemetry telemetry = null;

    /*For future use uncomment this */
    private WebcamName webcamName = null;

    private static final String VUFORIA_KEY =
            "AarEEQn/////AAABmTY7WIRMk0JfvS6zOFAH7hpR83bPgnanU0IaXelPm37J2UTuq1zA+9GKHfyUSvyW5D129EmfhQHZzj9HaLFIrLfgsVZVzn3UW/EVPsI04l+b4a/WVGND74ox6Q0AySr6Ew+bcHdDo6V/08+rrIaeRM0c+oXekVE9JOmXnixp9sK23o258rbvuUAwcixAXAkhJQMIPluwhMNFAXqTYmrdNriiRbeXbBcNSokBQ51Z6qIf1VfrshpPwtJYaUyg/MtVlMcx3UhZfvUQNioFxB6iXQCEr9fhtP2X6lLqKE66AUR9CdIMpFuZ9y8z8uFtUv81soa7vAssZWXCkp+L9xkJRv91mmFI25KeEoZUWv29XXDz";

    private TFObjectDetector tfod;
    private String targetName = null;

    protected List<Recognition> lastUpdatedRecognitions = null;

    private String foundTargetName                 = "None";

    private Status numberOfRings = Status.FOUR;

    public RingPipeline(HardwareMap hardwareMap, Telemetry telemetry, String modelAssetName, String[] assetNames, String targetName){
        this.hardwareMap    = hardwareMap;
        this.telemetry      = telemetry;
        this.targetName     = targetName;

        initVuforia();
        initTfod(modelAssetName, assetNames);

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            /* The TensorFlow software will scale the input images from the camera to a lower resolution.
                This can result in lower detection accuracy at longer distances (> 55cm or 22").
                If your target is at distance greater than 50 cm (20") you can adjust the magnification value
                to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
                should be set to the value of the images used to create the TensorFlow Object Detection model
                (typically 1.78 or 16/9). Change the following line if you want to adjust the magnification and/or the aspect ratio of the input images.
            */
            tfod.setZoom(2.5, 1.78);
        }

    }


    public String getTargetLabel() { return foundTargetName; }

    public Status getStatus(){
        return numberOfRings;
    }
    public void find(){
        if (tfod == null) {
            return;
        }
        List<Recognition> recognitions = tfod.getRecognitions();
        telemetry.addData("# Object Detected", recognitions.size());
        // step through the list of recognitions and display boundary info.
        int i = 0;
        // there might be multiple objects to recognize here.  If targetName is provided, it means we are only looking for target name
        for (Recognition recognition : recognitions) {

            if (targetName != "" && recognition.getLabel() != targetName) {
                continue;
            }
            foundTargetName = recognition.getLabel();
        }

        if(foundTargetName == "Quad"){
            numberOfRings = Status.FOUR;
        } else if (foundTargetName == "Single") {
            numberOfRings = Status.ONE;
        } else {
            numberOfRings = Status.NONE;
        }
    }
    public void stop()
    {
        if (tfod != null) {
            tfod.shutdown();
        }
    }


    private void initVuforia(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;


        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod(String modelAssetName, String[] assetsLabel) {

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        tfod.activate();

    }

}
