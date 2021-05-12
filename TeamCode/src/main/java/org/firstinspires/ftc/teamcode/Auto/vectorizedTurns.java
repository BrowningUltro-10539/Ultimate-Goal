package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

public class vectorizedTurns {
    OdometryDrive drive = new OdometryDrive();
    imuDrive gyro = new imuDrive();

    double xPos;
    double yPos;

    double wheelDiameter = 6;
    double countsPerRevolution = 8192;

    double wheelCircumference = wheelDiameter * Math.PI;

    double radiansToDegrees = 180/Math.PI;

    double degreesToRadians = Math.PI/180;

    DcMotor encX;
    DcMotor encY;

    DcMotor forwardsBackwardsEnc;
    DcMotor leftRightEnc;

    public void vectorizedTurn(double angle, double power, double targetX, double targetY, double initialAngle, DeviceMap map, newCoordinateSystem coords, OdometryDrive drive){
            boolean driving = false;
            xPos = coords.getXPos();
            yPos = coords.getYPos();

            encX = map.getRightBottom();
            encY = map.getLeftTop();

            String forwardsDirection = "Forward";
            String backwardsDirection = "Backward";
            String leftDirection = "Left";
            String rightDirection = "Right";


            if (initialAngle < 45 && initialAngle > -45) {
                forwardsDirection = "Forward";
                backwardsDirection = "Backward";
                leftDirection = "Left";
                rightDirection = "Right";
                forwardsBackwardsEnc = encY;
                leftRightEnc = encX;
            }
            //90 degrees to the left of initial direction
            else if (initialAngle > 45 && initialAngle < 135) {
                forwardsDirection = "Right";
                backwardsDirection = "Left";
                leftDirection = "Forward";
                rightDirection = "Backward";
                forwardsBackwardsEnc = encX;
                leftRightEnc = encY;
            }
            //90 degrees to the right of initial direction
            else if (initialAngle < -45 && initialAngle > 135) {
                forwardsDirection = "Left";
                backwardsDirection = "Right";
                leftDirection = "Backward";
                rightDirection = "Forward";
                forwardsBackwardsEnc = encX;
                leftRightEnc = encY;
            }
            //180 degrees from initial direction
            else {
                forwardsDirection = "Backward";
                backwardsDirection = "Forward";
                leftDirection = "Right";
                rightDirection = "Left";
                forwardsBackwardsEnc = encY;
                leftRightEnc = encX;
            }

            double oldDeltaTheta = 0;
            double oldEncPos = -forwardsBackwardsEnc.getCurrentPosition();
            double currentEncPos;
            double currentDeltaTheta;


            if(targetX>xPos && targetY>yPos){
                driving = true;
                while(driving){
                    Orientation currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
                    double currentAngle = currentOrientation.firstAngle;

                    double currentDeltaX = Math.abs(targetX-xPos);
                    double currentDeltaY = Math.abs(targetY-yPos);

//These formulas are questionable
                    double outerPower = (currentDeltaX/(currentDeltaX+currentDeltaY)) * (currentAngle - angle)/angle + power;
                    double innerPower = power - (currentDeltaY/(currentDeltaX+currentDeltaY)) * (currentAngle - angle)/angle;

//Set motor powers based on the values from the questionable formulas
                    map.getLeftTop().setPower(outerPower);
                    map.getLeftBottom().setPower(outerPower);
                    map.getRightTop().setPower(innerPower);
                    map.getRightBottom().setPower(innerPower);

                    //Measure Distance Travelled every time the angle changes by 1 degree

                    currentEncPos = -forwardsBackwardsEnc.getCurrentPosition();
                    currentDeltaTheta = Math.abs(currentAngle - initialAngle);

                    if(Math.abs(currentDeltaTheta-oldDeltaTheta) > 1){
                        double currentDistance = Math.abs(coords.countsToCm(currentEncPos-oldEncPos));
                        yPos += Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * currentDistance;
                        xPos += Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * currentDistance;
                        oldDeltaTheta = currentDeltaTheta;
                        oldEncPos=currentEncPos;
                    }

                    if(currentDeltaX < 5 && currentDeltaY < 5){
                        drive.stop(map);
                        driving = false;
                        coords.setxPos(xPos);
                        coords.setyPos(yPos);
                        return;
                    }
                }
            }
            else if(targetX<xPos && targetY>yPos){
                driving = true;
                while(driving){
                    Orientation currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
                    double currentAngle = currentOrientation.firstAngle;

                    double currentDeltaX = Math.abs(targetX-xPos);
                    double currentDeltaY = Math.abs(targetY-yPos);

//These formulas are questionable
                    double outerPower = (currentDeltaX/(currentDeltaX+currentDeltaY)) * (currentAngle - angle)/angle + power;
                    double innerPower = power - (currentDeltaY/(currentDeltaX+currentDeltaY)) * (currentAngle - angle)/angle;

//Set motor powers based on the values from the questionable formulas
                    map.getLeftTop().setPower(innerPower);
                    map.getLeftBottom().setPower(innerPower);
                    map.getRightTop().setPower(outerPower);
                    map.getRightBottom().setPower(outerPower);

                    //Measure Distance Travelled every time the angle changes by 1 degree

                    currentEncPos = -forwardsBackwardsEnc.getCurrentPosition();
                    currentDeltaTheta = Math.abs(currentAngle - initialAngle);

                    if(Math.abs(currentDeltaTheta-oldDeltaTheta) > 1){
                        double currentDistance = Math.abs(coords.countsToCm(currentEncPos-oldEncPos));
                        yPos += Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * currentDistance;
                        xPos -= Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * currentDistance;
                        oldDeltaTheta = currentDeltaTheta;
                        oldEncPos=currentEncPos;
                    }

                    if(currentDeltaX < 5 && currentDeltaY < 5){
                        drive.stop(map);
                        driving = false;
                        coords.setxPos(xPos);
                        coords.setyPos(yPos);
                        return;
                    }
                }
            }


    }

}
