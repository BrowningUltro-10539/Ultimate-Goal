package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

public class coordinateSystem {

    OdometryDrive drive = new OdometryDrive();

    double x;
    double y;

    double wheelDiameter = 6;
    double countsPerRevolution = 8192;

    double wheelCircumference = wheelDiameter * Math.PI;

    double beforeX;
    double beforeY;

    //1 - 4 north, east, south, west
    int initialDirection;
    int currentDirection;

    public coordinateSystem(){
        x = 0;
        y = 0;
    }
    public void initalizePosition (double startingX, double startingY){
        x = startingX;
        y = startingY;
    }

    public void initializeDirection(int startingDirection){
        currentDirection = startingDirection;
        initialDirection = startingDirection;

    }

    public void storePosition(DeviceMap map){
        beforeX = map.getRightBottom().getCurrentPosition();
        beforeY = map.getLeftTop().getCurrentPosition();
    }
    //CHECK BASED ON ANGLE AS WELL
    public void updatePosition(DeviceMap map){
        //X is for strafing, Y is in line with wheels
        DcMotor encX = map.getRightBottom();
        DcMotor encY = map.getLeftTop();
        if(currentDirection - initialDirection == 0){

            double currentXPos = encX.getCurrentPosition();
            double adjustX = -currentXPos;
            double deltaX = adjustX - beforeX;
            double deltaXCm = countsToCm(deltaX);
            x += deltaXCm;

            double currentYPos = encY.getCurrentPosition();
            double adjustedY = -currentYPos;
            double deltaY = adjustedY - beforeY;
            double deltaYCm = countsToCm(deltaY);
            y += deltaYCm;
        }else if (currentDirection - initialDirection == 1){

            //THIS IS THE Y DIRECTION NOW
            double currentXPos = encX.getCurrentPosition();
            double adjustX = -currentXPos;
            double deltaX = adjustX - beforeX;
            double deltaXCm = countsToCm(deltaX);
            y+=deltaXCm;

            //THIS IS THE X DIRECTION NOW
            double currentYPos = encY.getCurrentPosition();
            double adjustY = -currentYPos;
            double deltaY = adjustY - beforeY;
            double deltaYCm = countsToCm(deltaY);
            x += deltaYCm;

        }else if (currentDirection - initialDirection == 2) {
            double currentXPos = encX.getCurrentPosition();
            double deltaX = currentXPos - beforeX;
            double deltaXCm = countsToCm(deltaX);
            x += deltaXCm;

            double currentYPos = encY.getCurrentPosition();
            double deltaY = currentYPos - beforeY;
            double deltaYCm = countsToCm(deltaY);
            y += deltaYCm;
        }else if (currentDirection - initialDirection == 3){
            //THIS IS THE Y DIRECTION NOW
            double currentXPos = encX.getCurrentPosition();
            double deltaX = currentXPos - beforeX;
            double deltaXCm = countsToCm(deltaX);
            y+=deltaXCm;

            //THIS IS THE X DIRECTION NOW
            double currentYPos = encY.getCurrentPosition();
            double deltaY = currentYPos - beforeY;
            double deltaYCm = countsToCm(deltaY);
            x += deltaYCm;
        }
    }
    /*
    Direction Code
    1 - North
    2 - East
    3 - South
    4 - West
    The red odometry wheel indicates the front
     */
    public void updateDirection(DeviceMap map){
        Orientation currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
        double currentAngle = currentOrientation.firstAngle;

        if(currentAngle<45 && currentAngle> -45){
            currentDirection=initialDirection;
        }else if (currentAngle<135 && currentAngle>45){
            currentDirection=initialDirection+3;
        }else if (currentAngle>135 || currentAngle<-135){
            currentDirection=initialDirection+2;
        }else if(currentAngle>-135 && currentAngle<-45){
            currentDirection=initialDirection+1;
        }else{
            currentDirection=0;
        }

        if(currentDirection>4){
            currentDirection = currentDirection-4;
        }

    }

    public void goTo(double targetX, double targetY, double power, boolean gyroAssist, DeviceMap map){
        double xDistance = targetX - x;
        double yDistance = targetY - y;

        boolean ySlow = false;
        boolean xSlow = false;

        double slowSpeed = 0.15;

        if (yDistance<7){
            ySlow = true;
        }

        if(xDistance<7){
            xSlow = true;
        }

        if(currentDirection == 1) {
            //Y direction movement
            if (yDistance > 0) {
                storePosition(map);
                if(ySlow) {
                    drive.moveUntil("Forward", yDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Forward", yDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
            } else if (yDistance < 0) {
                yDistance=Math.abs(yDistance);
                storePosition(map);
                if(ySlow) {
                    drive.moveUntil("Backward", yDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Backward", yDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
            }
            //X direction movement
            if (xDistance > 0) {
                storePosition(map);
                if(xSlow) {
                    drive.moveUntil("Right", xDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Right", xDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
                return;
            }
            else if (xDistance < 0) {
                xDistance=Math.abs(xDistance);
                storePosition(map);
                if(xSlow) {
                    drive.moveUntil("Left", xDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Left", xDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
                return;
            }
        }
        else if(currentDirection == 2){
            //Y Direction movement
            if (yDistance > 0) {
                storePosition(map);
                if(ySlow) {
                    drive.moveUntil("Left", yDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Left", yDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
            } else if (yDistance < 0) {
                yDistance=Math.abs(yDistance);
                storePosition(map);
                if(ySlow) {
                    drive.moveUntil("Right", yDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Right", yDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
            }
            //X direction movement
            if (xDistance > 0) {
                storePosition(map);
                if(xSlow) {
                    drive.moveUntil("Backward", xDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Backward", xDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
                return;
            }
            else if (xDistance < 0) {
                xDistance=Math.abs(xDistance);
                storePosition(map);
                if(xSlow) {
                    drive.moveUntil("Forward", xDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Forward", xDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
                return;
            }
        }
        else if(currentDirection == 3){
            //Y Direction movement
            if (yDistance > 0) {
                storePosition(map);
                if(ySlow) {
                    drive.moveUntil("Backward", yDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Backward", yDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
            } else if (yDistance < 0) {
                yDistance=Math.abs(yDistance);
                storePosition(map);
                if(ySlow) {
                    drive.moveUntil("Forward", yDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Forward", yDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
            }
            //X direction movement
            if (xDistance > 0) {
                storePosition(map);
                if(xSlow) {
                    drive.moveUntil("Left", xDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Left", xDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
                return;
            }
            else if (xDistance < 0) {
                xDistance=Math.abs(xDistance);
                storePosition(map);
                if(xSlow) {
                    drive.moveUntil("Right", xDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Right", xDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
                return;
            }

        }else if (currentDirection == 4){
            //Y Direction movement
            if (yDistance > 0) {
                storePosition(map);
                if(ySlow) {
                    drive.moveUntil("Right", yDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Right", yDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
            } else if (yDistance < 0) {
                yDistance=Math.abs(yDistance);
                storePosition(map);
                if(ySlow) {
                    drive.moveUntil("Left", yDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Left", yDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
            }
            //X direction movement
            if (xDistance > 0) {
                storePosition(map);
                if(xSlow) {
                    drive.moveUntil("Forward", xDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Forward", xDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
                return;
            }
            else if (xDistance < 0) {
                xDistance=Math.abs(xDistance);
                storePosition(map);
                if(xSlow) {
                    drive.moveUntil("Backward", xDistance, slowSpeed, map, gyroAssist);
                }else{
                    drive.moveUntil("Backward", xDistance, power, map, gyroAssist);
                }
                updatePosition(map);
                updateDirection(map);
                return;
            }
        }

    }

    public int getCurrentDirection(){
        return currentDirection;
    }

    public double getCurrentX() {
        return x;
    }

    public double getCurrentY(){
        return y;
    }

    public double getStoredX(){
        return countsToCm(beforeX);
    }
    public double getStoredY(){
        return countsToCm(beforeY);
    }

    public double countsToCm(double counts){
        double revs = counts / countsPerRevolution;
        return revs * wheelCircumference;
    }

}
