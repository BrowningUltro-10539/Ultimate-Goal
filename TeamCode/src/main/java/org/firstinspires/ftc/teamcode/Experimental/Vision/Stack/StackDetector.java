package org.firstinspires.ftc.teamcode.Experimental.Vision.Stack;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Experimental.Vision.BasePipeline;
import org.firstinspires.ftc.teamcode.Vision.Status;

public class StackDetector extends BasePipeline {
    private StackPipeline pipeline;

    public StackDetector(LinearOpMode op){
        super(op);
        pipeline = new StackPipeline();
        setPipeline(pipeline);
    }

    public Status getResult(){
        return pipeline.getResult();
    }

    public Status getModeResult(){
        return pipeline.getModeResult();
    }

    public double[] getRawResult(){
        return pipeline.getRawResult();
    }
}
