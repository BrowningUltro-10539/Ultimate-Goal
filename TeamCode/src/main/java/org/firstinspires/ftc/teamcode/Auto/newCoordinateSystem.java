package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

public class newCoordinateSystem {
    OdometryDrive drive = new OdometryDrive();
    imuDrive gyro = new imuDrive();
    vectorizedTurns vectorTurn = new vectorizedTurns();

    double xPos;
    double yPos;

    double currentAngle;
    double initialAngle;

    double wheelDiameter = 6;
    double countsPerRevolution = 8192;

    double wheelCircumference = wheelDiameter * Math.PI;

    double radiansToDegrees = 180/Math.PI;

    double degreesToRadians = Math.PI/180;

    DcMotor encX;
    DcMotor encY;

    DcMotor forwardsBackwardsEnc;
    DcMotor leftRightEnc;

    public void initializeCoords(DeviceMap map, double startingX, double startingY){
        Orientation currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
        currentAngle = currentOrientation.firstAngle;
        initialAngle = currentOrientation.firstAngle;

        encX = map.getRightBottom();
        encY = map.getLeftTop();

        xPos = startingX;
        yPos = startingY;


    }


    public void updateAngle(DeviceMap map){
        Orientation currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
        currentAngle = currentOrientation.firstAngle;
    }
    public void goToPosition(double targetX, double targetY, DeviceMap map, double power, boolean keepInitialAngle, boolean vectorized, OpMode opMode){

        Orientation currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
        currentAngle = currentOrientation.firstAngle;

        if(vectorized){

            //Calc target length for each turn
            double deltaX = targetX - xPos;
            double deltaY = targetY - yPos;
            double distanceToDrive = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
            double angleToDrive = (180 / Math.PI) * (Math.atan(Math.abs(deltaX) / Math.abs(deltaY)));
            double startingAngle = currentAngle;

            vectorTurn.vectorizedTurn(angleToDrive, power, targetX-(deltaX/2), targetY-(deltaY/2), currentAngle, map, this, drive);
            vectorTurn.vectorizedTurn(startingAngle, power, targetX, targetY, currentAngle, map, this, drive);

        }else{


            //set a primary, secondary, tertiary and fourth direction, based on angle before movement.
            // then use those instead of "forward" etc.

            String forwardsDirection = null;
            String backwardsDirection = null;
            String leftDirection = null;
            String rightDirection = null;


            //Set directions to drive based on direction facing
            //Initial Direction
            if (currentAngle < 45 && currentAngle > -45) {
                forwardsDirection = "Forward";
                backwardsDirection = "Backward";
                leftDirection = "Left";
                rightDirection = "Right";
                forwardsBackwardsEnc = encY;
                leftRightEnc = encX;
            }
            //90 degrees to the left of initial direction
            else if (currentAngle > 45 && currentAngle < 135) {
                forwardsDirection = "Right";
                backwardsDirection = "Left";
                leftDirection = "Forward";
                rightDirection = "Backward";
                forwardsBackwardsEnc = encX;
                leftRightEnc = encY;
            }
            //90 degrees to the right of initial direction
            else if (currentAngle < -45 && currentAngle > -135) {
                forwardsDirection = "Left";
                backwardsDirection = "Right";
                leftDirection = "Backward";
                rightDirection = "Forward";
                forwardsBackwardsEnc = encX;
                leftRightEnc = encY;
            }
            //180 degrees from initial direction
            else if (currentAngle < -135 || currentAngle > 135){
                forwardsDirection = "Backward";
                backwardsDirection = "Forward";
                leftDirection = "Right";
                rightDirection = "Left";
                forwardsBackwardsEnc = encY;
                leftRightEnc = encX;
            }

            opMode.telemetry.addData("angle", currentAngle);
            opMode.telemetry.addData("x:", xPos);
            opMode.telemetry.addData("y:", yPos);
            opMode.telemetry.addData("backwards:", backwardsDirection);
            opMode.telemetry.update();

            //Quadrant 1
            if (targetX > xPos && targetY > yPos) {
                double deltaX = targetX - xPos;
                double deltaY = targetY - yPos;
                double distanceToDrive = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                double angleToDrive = (180 / Math.PI) * (Math.atan(Math.abs(deltaX) / Math.abs(deltaY)));

                if (deltaX < 5 && deltaX > -5) { //Y direction
                    double startingDistance = -forwardsBackwardsEnc.getCurrentPosition();
                    drive.moveUntil(forwardsDirection, Math.abs(deltaY), power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition();
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    yPos += distanceDriven;

                } else if (deltaY < 5 && deltaY > -5) { //X direction
                    double startingDistance = leftRightEnc.getCurrentPosition();
                    drive.moveUntil(rightDirection, Math.abs(deltaX), power, map, true);

                    double distanceDriven = countsToCm(leftRightEnc.getCurrentPosition() - startingDistance);
                    xPos += distanceDriven;


                } else if (angleToDrive < 60) {
                    gyro.turn(-angleToDrive, power, map);
                    updateAngle(map);

                    double startingDistance = -forwardsBackwardsEnc.getCurrentPosition();
                    drive.moveUntil(forwardsDirection, distanceToDrive, power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition();
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    updateAngle(map);
                    xPos += Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;
                    yPos += Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;


                    if (keepInitialAngle) {
                        gyro.turn(angleToDrive, power, map);
                        updateAngle(map);
                    }

                } else {

                    double strafeAngle = 90 - angleToDrive;
                    gyro.turn(strafeAngle, power, map);
                    updateAngle(map);

                    double startingDistance = leftRightEnc.getCurrentPosition();
                    drive.moveUntil(rightDirection, distanceToDrive, power, map, true);

                    double distanceDriven = countsToCm(leftRightEnc.getCurrentPosition() - startingDistance);
                    updateAngle(map);
                    xPos += Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;
                    yPos += Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;

                    if (keepInitialAngle) {
                        gyro.turn(-strafeAngle, power, map);
                        updateAngle(map);

                    }
                }

            }
            //Quadrant 2
            else if (targetX < xPos && targetY > yPos) {
                double deltaX = targetX - xPos;
                double deltaY = targetY - yPos;
                double distanceToDrive = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                double angleToDrive = (180 / Math.PI) * (Math.atan(Math.abs(deltaX) / Math.abs(deltaY)));

                if (deltaX < 5 && deltaX > -5) {
                    double startingDistance = -forwardsBackwardsEnc.getCurrentPosition();
                    drive.moveUntil(forwardsDirection, Math.abs(deltaY), power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition();
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    yPos += distanceDriven;

                } else if (deltaY < 5 && deltaY > -5) {
                    double startingDistance = leftRightEnc.getCurrentPosition();
                    drive.moveUntil(leftDirection, Math.abs(deltaX), power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition();
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    xPos -= distanceDriven;

                } else if (angleToDrive < 60) {
                    gyro.turn(angleToDrive, power, map);
                    updateAngle(map);

                    double startingDistance = -forwardsBackwardsEnc.getCurrentPosition();
                    drive.moveUntil(forwardsDirection, distanceToDrive, power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition();
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    updateAngle(map);
                    xPos -= Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;
                    yPos += Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;

                    if (keepInitialAngle) {
                        gyro.turn(-angleToDrive, power, map);
                        updateAngle(map);

                    }
                } else {

                    double strafeAngle = 90 - angleToDrive;
                    gyro.turn(-strafeAngle, power, map);
                    updateAngle(map);

                    double startingDistance = leftRightEnc.getCurrentPosition();
                    drive.moveUntil(leftDirection, distanceToDrive, power, map, true);

                    double distanceDriven = Math.abs(countsToCm(leftRightEnc.getCurrentPosition() - startingDistance));
                    updateAngle(map);
                    xPos -= Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;
                    yPos += Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;

                    if (keepInitialAngle) {
                        gyro.turn(strafeAngle, power, map);
                        updateAngle(map);

                    }
                }

            }
            // Quadrant 3
            else if (targetX < xPos && targetY < yPos) {
                double deltaX = targetX - xPos;
                double deltaY = targetY - yPos;
                double distanceToDrive = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                double angleToDrive = (180 / Math.PI) * (Math.atan(Math.abs(deltaX) / Math.abs(deltaY)));

                if (deltaX < 5 && deltaX > -5) {
                    double startingDistance = -forwardsBackwardsEnc.getCurrentPosition();
                    drive.moveUntil(backwardsDirection, Math.abs(deltaY), power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition(); //Negative here may be a problem
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    yPos -= distanceDriven;

                } else if (deltaY < 5 && deltaY > -5) {

                    double startingDistance = leftRightEnc.getCurrentPosition();
                    drive.moveUntil(leftDirection, Math.abs(deltaX), power, map, true);

                    double distanceDriven = Math.abs(countsToCm(leftRightEnc.getCurrentPosition() - startingDistance));
                    xPos -= distanceDriven;

                } else if (angleToDrive < 60) {
                    gyro.turn(-angleToDrive, power, map);
                    updateAngle(map);

                    double startingDistance = -forwardsBackwardsEnc.getCurrentPosition(); //Negative here may be a problem
                    drive.moveUntil(backwardsDirection, distanceToDrive, power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition();
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    updateAngle(map);
                    xPos -= Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;
                    yPos -= Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;

                    if (keepInitialAngle) {
                        gyro.turn(angleToDrive, power, map);
                        updateAngle(map);

                    }
                } else {

                    double strafeAngle = 90 - angleToDrive;
                    gyro.turn(strafeAngle, power, map);
                    updateAngle(map);

                    double startingDistance = leftRightEnc.getCurrentPosition();
                    drive.moveUntil(leftDirection, distanceToDrive, power, map, true);

                    double distanceDriven = Math.abs(countsToCm(leftRightEnc.getCurrentPosition() - startingDistance));
                    updateAngle(map);
                    xPos -= Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;
                    yPos -= Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;

                    if (keepInitialAngle) {
                        gyro.turn(-strafeAngle, power, map);
                        updateAngle(map);

                    }
                }
            }
            //Quadrant 4
            else if (targetX > xPos && targetY < yPos) {
                double deltaX = targetX - xPos;
                double deltaY = targetY - yPos;
                double distanceToDrive = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                double angleToDrive = (180 / Math.PI) * (Math.atan(Math.abs(deltaX) / Math.abs(deltaY)));

                if (deltaX < 5 && deltaX > -5) {

                    double startingDistance = -forwardsBackwardsEnc.getCurrentPosition(); //Negative here may be a problem
                    drive.moveUntil(backwardsDirection, Math.abs(deltaY), power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition();
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    yPos -= distanceDriven;

                } else if (deltaY < 5 && deltaY > -5) {
                    double startingDistance = leftRightEnc.getCurrentPosition();
                    drive.moveUntil(rightDirection, Math.abs(deltaX), power, map, true);

                    double distanceDriven = Math.abs(countsToCm(leftRightEnc.getCurrentPosition() - startingDistance));
                    xPos += distanceDriven;

                } else if (angleToDrive < 60) {
                    gyro.turn(angleToDrive, power, map);
                    updateAngle(map);

                    double startingDistance = -forwardsBackwardsEnc.getCurrentPosition(); //Negative here may be a problem
                    drive.moveUntil(backwardsDirection, distanceToDrive, power, map, true);

                    double currentPosition = -forwardsBackwardsEnc.getCurrentPosition();
                    double distanceDriven = Math.abs(countsToCm(currentPosition - startingDistance));
                    updateAngle(map);
                    xPos += Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;
                    yPos -= Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;

                    if (keepInitialAngle) {
                        gyro.turn(-angleToDrive, power, map);

                        updateAngle(map);

                    }
                } else {

                    double strafeAngle = 90 - angleToDrive;
                    gyro.turn(-strafeAngle, power, map);
                    updateAngle(map);

                    double startingDistance = leftRightEnc.getCurrentPosition();
                    drive.moveUntil(rightDirection, distanceToDrive, power, map, true);

                    double distanceDriven = Math.abs(countsToCm(leftRightEnc.getCurrentPosition() - startingDistance));
                    updateAngle(map);
                    xPos += Math.abs(Math.sin(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;
                    yPos -= Math.abs(Math.cos(Math.abs(currentAngle) * degreesToRadians)) * distanceDriven;

                    if (keepInitialAngle) {
                        gyro.turn(strafeAngle, power, map);
                        updateAngle(map);

                    }
                }
            }
        }
    }

    public double countsToCm(double counts){
        double revs = counts / countsPerRevolution;
        return revs * wheelCircumference;
    }

    public double getXPos(){
        return xPos;
    }

    public double getYPos(){
        return yPos;
    }

    public double getCurrentAngle(){
        return currentAngle;
    }

    public void setxPos(double newXPos){
        xPos = newXPos;
    }

    public void setyPos(double newYPos){
        yPos = newYPos;
    }
}
