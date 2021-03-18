package org.firstinspires.ftc.teamcode.Vision;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class OpenCVBoxes extends OpenCvPipeline {
    private double topAverage;
    private double bottomAverage;

    private final double ORANGE_THRESHOLD = 110;
    private Mat topRectangle = new Mat();
    private Mat bottomRectangle = new Mat();

    private Mat convertedInput = new Mat(); // Working copy; keeps submats out of input buffer

    private Mat topSample = new Mat();      // For submat crops
    private Mat bottomSample = new Mat();   // For submat crops

    private Rect bottomRect = new Rect(     // Dimensions and locations for sampling
            400,
            300,
            100,
            20
    );
    private Rect topRect = new Rect(        // Dimensions and locations for sampling
            400,
            240,
            100,
            20
    );

    public OpenCVBoxes(){}

    public Mat processFrame(Mat input){
        Imgproc.cvtColor(input, convertedInput, Imgproc.COLOR_RGB2YCrCb);    // Convert color space to working copy

        topSample = convertedInput.submat(topRect);         // Shouldn't be necessary according to the docs
        bottomSample = convertedInput.submat(bottomRect);   // Shouldn't be necessary according to the docs

        Imgproc.rectangle(input, topRect, new Scalar(0, 255, 0), 2);        // Draw rectangles on input buffer
        Imgproc.rectangle(input, bottomRect, new Scalar(0, 255, 0), 2);     // for drive team feedback

        Core.extractChannel(bottomSample, bottomRectangle, 2);
        Core.extractChannel(topSample, topRectangle, 2);

        bottomAverage = Core.mean(bottomRectangle).val[0];
        topAverage = Core.mean(topRectangle).val[0];

        return input;
    }

    public Status rings(){
        if(topAverage < ORANGE_THRESHOLD && bottomAverage < ORANGE_THRESHOLD){
            return Status.FOUR;
        } else if(topAverage > ORANGE_THRESHOLD && bottomAverage < ORANGE_THRESHOLD){
            return Status.ONE;
        } else {
            return Status.NONE;
        }
    }


}
