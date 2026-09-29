package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ServoSubclass {

    private Servo servo;
    public static double servoPos = 0.5;

    public void init(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class,"servo");
        servo.setPosition(servoPos);
    }

    public void update() {
        servoPos = Math.max(0.0, Math.min(1.0, servoPos));
        servo.setPosition(servoPos);
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

        servoPos = Math.min(1.0, servoPos);

        servo.setPosition(servoPos);
    }

    public void servoIncrementDown() {
        servoPos -= 0.01;

        servoPos = Math.max(0.0,servoPos);

        servo.setPosition(servoPos);
    }

    public double getPosition() {
        return servo.getPosition();
    }
}
