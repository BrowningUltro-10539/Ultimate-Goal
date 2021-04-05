package org.firstinspires.ftc.teamcode.Experimental.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Experimental.Vision.RingLocator.RingLocatorPipeline;
import org.firstinspires.ftc.teamcode.Experimental.Vision.Stack.StackPipeline;

public class Vision extends BasePipeline {
    private StackPipeline stackPipeline;
    private RingLocatorPipeline ringLocatorPipeline;

    public enum Pipeline {StackHeight, RingLocator}

    public Vision(LinearOpMode op){
        super(op);

        stackPipeline = new StackPipeline();
        ringLocatorPipeline = new RingLocatorPipeline();
    }

    public Vision(LinearOpMode op, Pipeline pipeline){
        super(op);

        stackPipeline = new StackPipeline();
        ringLocatorPipeline = new RingLocatorPipeline();

        setPipeline(pipeline);
    }

    public void setPipeline(Pipeline pipeline){
        if (pipeline == Pipeline.StackHeight){
            setPipeline(stackPipeline);
        } else if(pipeline == Pipeline.RingLocator){
            setPipeline(ringLocatorPipeline);
        }
    }

    public StackPipeline getStackPipe(){
        return stackPipeline;
    }

    public RingLocatorPipeline getRingPipe(){
        return ringLocatorPipeline;
    }
}
