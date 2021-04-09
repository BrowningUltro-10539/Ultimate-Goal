package org.firstinspires.ftc.teamcode.Experimental.Vision;

import android.annotation.SuppressLint;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

@SuppressLint("Path")
public class RingProcessor {
    public static double FILTER_MIN = 80;
    public static double FILTER_MAX = 110;

    private Mat yCrCb = new Mat();
    private Mat cb = new Mat();
    private Mat processed = new Mat();
    private Mat mask = new Mat();
    private Mat save;

    private String path = "EasyOpenCV/";

    public RingProcessor(String prefix){
        path += prefix + '_';
    }

    public Mat[] processFrame(Mat input){
        Imgproc.cvtColor(input, yCrCb, Imgproc.COLOR_RGB2YCrCb);

        Core.extractChannel(yCrCb, cb, 2);


        Core.inRange(cb, new Scalar(FILTER_MIN), new Scalar(FILTER_MAX), processed);

        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, new Mat());

        input.copyTo(mask, processed);

        saveMatToDisk("input.jpg", input);
        saveMatToDisk("ycrcb.jpg", yCrCb);
        saveMatToDisk("cb.jpg", cb);
        saveMatToDisk("processed.jpg", processed);
        saveMatToDisk("mask.jpg", mask);

        return new Mat[] { processed, mask };


    }

    public void saveMatToDisk(String name, Mat mat){
        save = mat.clone();

        Imgproc.cvtColor(mat, save, Imgproc.COLOR_BGR2RGB);
        Imgcodecs.imwrite(path + name, save);
    }
}
