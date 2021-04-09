package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;
import java.util.Locale;


public class imuDrive {

    public void turn( double angle, double power, DeviceMap map) {


        //Gyro stuff
        double initialAngle = checkOrientation(map.getImu());
        double currentAngle = checkOrientation(map.getImu());
        double targetAngle = initialAngle + angle;

        if(targetAngle == 180){
            targetAngle = targetAngle - 2;
        }

        if (targetAngle > 180){
            targetAngle=targetAngle-360;
            map.getLeftTop().setPower(-power);
            map.getLeftBottom().setPower(-power);
            map.getRightTop().setPower(power);
            map.getRightBottom().setPower(power);

            while (currentAngle < targetAngle || currentAngle > 0){
                currentAngle = checkOrientation(map.getImu());
                double currentDifference = Math.abs(currentAngle - targetAngle);
                double ratio = currentDifference/Math.abs(angle);
                double adjustPower = Math.abs(ratio*power) +0.15;

                if(adjustPower>power){
                    adjustPower=power;
                }

                map.getLeftTop().setPower(-adjustPower);
                map.getLeftBottom().setPower(-adjustPower);
                map.getRightTop().setPower(adjustPower);
                map.getRightBottom().setPower(adjustPower);

            }

            stop(map);
        }else if(targetAngle<-180){
            targetAngle=targetAngle+360;
            map.getLeftTop().setPower(power);
            map.getLeftBottom().setPower(power);
            map.getRightTop().setPower(-power);
            map.getRightBottom().setPower(-power);

            while (currentAngle > targetAngle || currentAngle < 0){
                currentAngle = checkOrientation(map.getImu());
                double currentDifference = Math.abs(currentAngle - targetAngle);
                double ratio = currentDifference/Math.abs(angle);
                double adjustPower = Math.abs(ratio*power) +0.15;

                if(adjustPower>power){
                    adjustPower=power;
                }

                map.getLeftTop().setPower(adjustPower);
                map.getLeftBottom().setPower(adjustPower);
                map.getRightTop().setPower(-adjustPower);
                map.getRightBottom().setPower(-adjustPower);
            }

            stop(map);


        }

        else if (angle>0){
            map.getLeftTop().setPower(-power);
            map.getLeftBottom().setPower(-power);
            map.getRightTop().setPower(power);
            map.getRightBottom().setPower(power);

            while (currentAngle < targetAngle){
                currentAngle = checkOrientation(map.getImu());
                double currentDifference = Math.abs(currentAngle - targetAngle);
                double ratio = currentDifference/Math.abs(angle);
                double adjustPower = Math.abs(ratio*power) + 0.15;

                if(adjustPower>power){
                    adjustPower=power;
                }

                map.getLeftTop().setPower(-adjustPower);
                map.getLeftBottom().setPower(-adjustPower);
                map.getRightTop().setPower(adjustPower);
                map.getRightBottom().setPower(adjustPower);
            }


            stop(map);



        }else if (angle<0){
            map.getLeftTop().setPower(power);
            map.getLeftBottom().setPower(power);
            map.getRightTop().setPower(-power);
            map.getRightBottom().setPower(-power);

            while (currentAngle > targetAngle) {
                currentAngle = checkOrientation(map.getImu());
                double currentDifference = Math.abs(currentAngle - targetAngle);
                double ratio = currentDifference / Math.abs(angle);
                double adjustPower = Math.abs(ratio * power) + 0.15;

                if (adjustPower > power) {
                    adjustPower = power;
                }

                map.getLeftTop().setPower(adjustPower);
                map.getLeftBottom().setPower(adjustPower);
                map.getRightTop().setPower(-adjustPower);
                map.getRightBottom().setPower(-adjustPower);

            }
            stop(map);

        }


    }
    private void stop(DeviceMap map){
        map.getLeftTop().setPower(0);
        map.getRightTop().setPower(0);
        map.getLeftBottom().setPower(0);
        map.getRightBottom().setPower(0);
    }

    private double checkOrientation(BNO055IMU imu){
        Orientation current = imu.getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZXY, AngleUnit.DEGREES);
        return current.firstAngle;
    }


}
