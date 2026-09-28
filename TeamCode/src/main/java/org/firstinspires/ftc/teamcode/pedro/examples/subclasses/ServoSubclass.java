package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoSubclass {
    private Servo servo;
    private double servoPos = 0.5;

    public void init(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "servo");
        servo.setPosition(0.5);
    }

    public void servo1() {
        servoPos = 1.0;
        servo.setPosition(servoPos);
    }

    public void servo0() {
        servoPos = 0.0;
        servo.setPosition(servoPos);
    }

    public void servoOther() {
        servoPos = 0.9;
        servo.setPosition(servoPos);
    }

    public void servoIncrementUp() {
        servoPos += 0.01;
        servo.setPosition(servoPos);
    }

    public void servoIncrementDown() {
        servoPos -= 0.01;
        servo.setPosition(servoPos);
    }


}