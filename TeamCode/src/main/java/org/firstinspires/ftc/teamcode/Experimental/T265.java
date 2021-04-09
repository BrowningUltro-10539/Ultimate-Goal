package org.firstinspires.ftc.teamcode.Experimental;

import java.io.File;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.spartronics4915.lib.T265Camera;



import static java.lang.Math.PI;

public class T265 {
    private T265Camera t265Camera;

    public final double ODOMETRY_COVARIANCE = 0.1;
    private final double INCH_TO_METER = 0.0254;
    private final double xOffset = -9;
    private final double yOffset = 2;

    private double x;
    private double y;
    private double theta;

    public int confidence = 0;
    private final String mapPath = "/data/user/0/com.qualcomm.ftcrobotcontroller/cache/map.bin";
    public boolean isEmpty = false;
    private boolean exportingMap = true;

    public T265(LinearOpMode op, double startX, double startY, double startTheta){
        File file = new File(mapPath);
        if(!file.exists() || file.length() == 0){
            isEmpty = true;
        }

        Robot.log("isEmpty:" + isEmpty);

        if(!isEmpty){
            t265Camera = new T265Camera(new Transform2d(), ODOMETRY_COVARIANCE, mapPath, op.hardwareMap.appContext);
        } else {
            t265Camera = new T265Camera(new Transform2d(), ODOMETRY_COVARIANCE, mapPath, op.hardwareMap.appContext);
        }

        setCameraPose(startX, startY, startTheta);
    }

    public void startCam(){
        t265Camera.start();
    }


}
