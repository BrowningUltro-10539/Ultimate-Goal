package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RobotInfo.DeviceMap;

public class OdometryDrive {

    public void moveUntil(String direction, double cm, double power, DeviceMap map, boolean gyroAssist) {

        // ALL UNITS IN CM
        double wheelDiameter = 6;
        double countsPerRevolution = 8192;

        double wheelCircumference = wheelDiameter * Math.PI;


        //X is for strafing, Y is in line with wheels
        DcMotor encX = map.getRightBottom();
        DcMotor encY = map.getLeftTop();


        double adjustCm;
        double revolutions;
        double encCount;

        double startingEncCount;

        //Gyro assist stuff

        Orientation initialOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
        Orientation currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
        //Constants to change
        double assistorThreshold = 1;
        double assistorRatio = 90; //360 is base speed, lower is faster correction

        double newLeftPower = power;
        double newRightPower = power;
        double leftChange = 0;
        double rightChange = 0;
        double angleChange = 0;

        double forwardsChange = 0;
        double backwardsChange = 0;
        double newBackPower = power;
        double newFrontPower = power;


        if (gyroAssist){
            //Execute code based on direction
            //Forward Movement
            switch (direction){

                case "Forward":
                    //MATH
                    //Y direction is reversed - forward is negative
                    adjustCm = -cm;
                    revolutions = adjustCm/wheelCircumference;
                    encCount = revolutions*countsPerRevolution;

                    startingEncCount=encY.getCurrentPosition();

                    //Start Movement
                    map.getLeftTop().setPower(power);
                    map.getRightTop().setPower(power);
                    map.getLeftBottom().setPower(power);
                    map.getRightBottom().setPower(power);
                    //Gyro assist
                    while (encY.getCurrentPosition()-startingEncCount>encCount){
                        //Get angles
                        currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZXY,AngleUnit.DEGREES);
                        //Only do this if error is over threshold degrees
                        angleChange = currentOrientation.firstAngle - initialOrientation.firstAngle;
                        if(Math.abs(angleChange) > assistorThreshold){
                            // ratio - the change in power is based on how much the ange differs

                            leftChange = angleChange / assistorRatio;
                            rightChange = -leftChange;

                            newLeftPower=power+leftChange;
                            newRightPower=power+rightChange;
                        }
                        //Set new powers
                        map.getLeftTop().setPower(newLeftPower);
                        map.getRightTop().setPower(newRightPower);
                        map.getLeftBottom().setPower(newLeftPower);
                        map.getRightBottom().setPower(newRightPower);
                    }

                    //Stop once robot gets there
                    stop(map);
                    break;

                case "Backward" :
                    double adjustPower = -power;
                    //MATH
                    //Y direction is reversed - backward is positive
                    revolutions = cm/wheelCircumference;
                    encCount = revolutions*countsPerRevolution;

                    startingEncCount=encY.getCurrentPosition();

                    //Start Movement
                    map.getLeftTop().setPower(adjustPower);
                    map.getRightTop().setPower(adjustPower);
                    map.getLeftBottom().setPower(adjustPower);
                    map.getRightBottom().setPower(adjustPower);

                    //Wait until the robot gets there
                    while (encY.getCurrentPosition()-startingEncCount<encCount) {
                        currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZXY,AngleUnit.DEGREES);
                        //Only do this if error is over threshold degrees
                        angleChange = currentOrientation.firstAngle - initialOrientation.firstAngle;
                        if(Math.abs(angleChange) > assistorThreshold) {
                            // ratio - the change in power is based on how much the ange differs - change the constant it is divided by to change the speed of correction
                            //360 is base speed - 360 degrees in a full circle - lower is faster

                            rightChange = angleChange / assistorRatio;
                            leftChange = -rightChange;

                            newLeftPower = adjustPower - leftChange;
                            newRightPower = adjustPower - rightChange;

                            //Set new powers
                            map.getLeftTop().setPower(newLeftPower);
                            map.getRightTop().setPower(newRightPower);
                            map.getLeftBottom().setPower(newLeftPower);
                            map.getRightBottom().setPower(newRightPower);
                        }

                    }

                    //Stop once robot gets there
                    stop(map);
                    break;

                case "Left":
                    //MATH
                    //Check X direction
                    adjustCm = -cm;
                    revolutions = adjustCm/wheelCircumference;
                    encCount = revolutions*countsPerRevolution;

                    startingEncCount=encX.getCurrentPosition();

                    //Start Movement
                    map.getLeftTop().setPower(-power);
                    map.getRightTop().setPower(power);
                    map.getLeftBottom().setPower(power);
                    map.getRightBottom().setPower(-power);

                    //Wait until the robot gets there
                    while (encX.getCurrentPosition()-startingEncCount>encCount) {
                        currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZXY,AngleUnit.DEGREES);
                        //Only do this if error is over threshold degrees
                        angleChange = currentOrientation.firstAngle - initialOrientation.firstAngle;
                        if(Math.abs(angleChange) > assistorThreshold) {
                            // ratio - the change in power is based on how much the ange differs - change the constant it is divided by to change the speed of correction
                            //360 is base speed - 360 degrees in a full circle - lower is faster

                            backwardsChange= angleChange / assistorRatio;
                            forwardsChange = -backwardsChange;

                            newBackPower = power + backwardsChange;
                            newFrontPower = power + forwardsChange;
                        }
                        //Set new powers
                        map.getLeftTop().setPower(-newFrontPower);
                        map.getRightTop().setPower(newFrontPower);
                        map.getLeftBottom().setPower(newBackPower);
                        map.getRightBottom().setPower(-newBackPower);
                    }

                    //Stop once robot gets there
                    stop(map);
                    break;

                case "Right":
                    //MATH
                    //Check X direction
                    revolutions = cm/wheelCircumference;
                    encCount = revolutions*countsPerRevolution;

                    startingEncCount = encX.getCurrentPosition();

                    //Start Movement
                    map.getLeftTop().setPower(power);
                    map.getRightTop().setPower(-power);
                    map.getLeftBottom().setPower(-power);
                    map.getRightBottom().setPower(power);

                    //Wait until the robot gets there
                    while (encX.getCurrentPosition()-startingEncCount<encCount) {
                        currentOrientation = map.getImu().getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZXY,AngleUnit.DEGREES);
                        //Only do this if error is over threshold degrees
                        angleChange = currentOrientation.firstAngle - initialOrientation.firstAngle;
                        if(Math.abs(angleChange) > assistorThreshold) {
                            // ratio - the change in power is based on how much the ange differs - change the constant it is divided by to change the speed of correction
                            //360 is base speed - 360 degrees in a full circle - lower is faster

                            forwardsChange= angleChange / assistorRatio;
                            backwardsChange= -forwardsChange;

                            newBackPower = power + backwardsChange;
                            newFrontPower = power + forwardsChange;

                            //Set new powers
                            map.getLeftTop().setPower(newFrontPower);
                            map.getRightTop().setPower(-newFrontPower);
                            map.getLeftBottom().setPower(-newBackPower);
                            map.getRightBottom().setPower(newBackPower);
                        }

                    }

                    //Stop once robot gets there
                    stop(map);
                    break;

            }
        }
        else{
            //Execute code based on direction
            //Forward Movement
            switch (direction){

                case "Forward":
                    //MATH
                    //Y direction is reversed - forward is negative
                    adjustCm = -cm;
                    revolutions = adjustCm/wheelCircumference;
                    encCount = revolutions*countsPerRevolution;

                    startingEncCount = encY.getCurrentPosition();
                    //Start Movement
                    map.getLeftTop().setPower(power);
                    map.getRightTop().setPower(power);
                    map.getLeftBottom().setPower(power);
                    map.getRightBottom().setPower(power);
                    //Wait until the robot gets there
                    while (encY.getCurrentPosition()-startingEncCount>encCount){
                        //running = true;
                    }

                    //Stop once robot gets there
                    stop(map);
                    break;

                case "Backward" :
                    //MATH
                    //Y direction is reversed - backward is positive
                    revolutions = cm/wheelCircumference;
                    encCount = revolutions*countsPerRevolution;
                    startingEncCount = encY.getCurrentPosition();

                    //Start Movement
                    map.getLeftTop().setPower(-power);
                    map.getRightTop().setPower(-power);
                    map.getLeftBottom().setPower(-power);
                    map.getRightBottom().setPower(-power);

                    //Wait until the robot gets there
                    while (encY.getCurrentPosition()-startingEncCount<encCount) { }

                    //Stop once robot gets there
                    stop(map);
                    break;

                case "Left":
                    //MATH
                    //Check X direction
                    adjustCm = -cm;
                    revolutions = adjustCm/wheelCircumference;
                    encCount = revolutions*countsPerRevolution;

                    startingEncCount = encX.getCurrentPosition();

                    //Start Movement
                    map.getLeftTop().setPower(-power);
                    map.getRightTop().setPower(power);
                    map.getLeftBottom().setPower(power);
                    map.getRightBottom().setPower(-power);

                    //Wait until the robot gets there
                    while (encX.getCurrentPosition()-startingEncCount>encCount) { }

                    //Stop once robot gets there
                    stop(map);
                    break;

                case "Right":
                    //MATH
                    //Check X direction
                    revolutions = cm/wheelCircumference;
                    encCount = revolutions*countsPerRevolution;

                    startingEncCount = encX.getCurrentPosition();

                    //Start Movement
                    map.getLeftTop().setPower(power);
                    map.getRightTop().setPower(-power);
                    map.getLeftBottom().setPower(-power);
                    map.getRightBottom().setPower(power);

                    //Wait until the robot gets there
                    while (encX.getCurrentPosition()-startingEncCount<encCount) { }

                    //Stop once robot gets there
                    stop(map);
                    break;

            }

        }





    }

    private void stop(DeviceMap map){
        map.getLeftTop().setPower(0);
        map.getRightTop().setPower(0);
        map.getLeftBottom().setPower(0);
        map.getRightBottom().setPower(0);
    }
}
