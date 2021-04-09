package org.firstinspires.ftc.teamcode.Experimental.Vision.Stack;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Experimental.Vision.RingProcessor;
import org.firstinspires.ftc.teamcode.Vision.Status;


import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StackPipeline extends OpenCvPipeline {
    public Status status;

    public static int HEIGHT_MIN = 10;
    public static int WIDTH_MIN = 15;
    public static int HEIGHT_MAX = 60;
    public static int WIDTH_MAX = 60;
    public static double ONE_MIN = 2.3;
    public static double ONE_MAX = 2.8;
    public static double FOUR_MIN = 0.5;
    public static double FOUR_AREA = 1000;

    private double[] result = new double[3];
    private Status ringCase = Status.NONE;
    private Status[] results = new Status[5];
    private int cycles = 0;

    private RingProcessor processor;
    private Mat processed = new Mat();

    public StackPipeline(){
        processor = new RingProcessor("height");
        Arrays.fill(results, Status.FOUR);
    }

    @Override
    public Mat processFrame(Mat input) {
        // Process Image
        processor.saveMatToDisk("raw.jpg", input);
        /* We must fix this issue */
        input = new Mat(input, new Rect(140, 30, 90, 90));
        processed = processor.processFrame(input)[0];

        // Find Contours
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(processed, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Loop Through Contours
        int i = 0;
        for (MatOfPoint contour : contours) {
            MatOfPoint2f areaPoints = new MatOfPoint2f(contour.toArray());
            RotatedRect boundingRect = Imgproc.minAreaRect(areaPoints);

            // Reject Small Contours
            if (HEIGHT_MIN < boundingRect.size.height && boundingRect.size.height < HEIGHT_MAX && WIDTH_MIN < boundingRect.size.width && boundingRect.size.width < WIDTH_MAX) {

                // Get Detection Size
                Imgproc.rectangle(input, boundingRect.boundingRect(), new Scalar(0, 255, 0), 4);
                i++;

                double width = boundingRect.size.width;
                double height = boundingRect.size.height;
                double wh_ratio = width/height;
                log("Loop(" + i + "): " + width + " " + height + " " + wh_ratio + " " + boundingRect.size.area());

                result = new double[] {width, height, wh_ratio};

                // Checking WH ratio because heights were inconsistent in testing images
                // This works better at a higher camera angle but comparing the height would be better for a lower camera angle.
                if (FOUR_MIN <= wh_ratio && wh_ratio <= ONE_MIN && boundingRect.size.area() >= FOUR_AREA) {
                    ringCase = Status.FOUR;
                } else if (ONE_MIN <= wh_ratio && wh_ratio <= ONE_MAX) {
                    ringCase = Status.ONE;
                }
            }
        }
        processor.saveMatToDisk("rect.jpg", input);

        log("Total: " + contours.size() + " Passed threshold: " + i);

        // No Contours Detected
        if (i == 0) {
            result = new double[] {0,0,0};
            ringCase = Status.ONE;
            log("No Contours Detected");
        }

        log("Result: " + Arrays.toString(result));
        log("Case: " + ringCase.name());

        results[cycles % 5] = ringCase;
        cycles++;

        return input;
    }


    public double[] getRawResult() {
        return result;
    }

    public Status getResult() {
        return ringCase;
    }

    public Status getModeResult() {
        List<Status> list = Arrays.asList(results);
        int zero = Collections.frequency(list, Status.NONE);
        int one = Collections.frequency(list, Status.ONE);
        int four = Collections.frequency(list, Status.FOUR);
        log("zero: " + zero + ", one: " + one + ", four: " + four);
        if (one > zero && one > four) {
            return Status.ONE;
        } else if (four > zero && four > one) {
            return Status.FOUR;
        } else {
            return Status.NONE;
        }
    }

    private void log(String message) {
        Log.w("stack-height-pipe", message);
    }



}
