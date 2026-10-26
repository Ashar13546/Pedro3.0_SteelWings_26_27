package org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ServoSubclass {

    private Servo holdServo, rampServo;
    public static double holdServoPos = 0.25;
    public static double rampServoPos = 0.0;


    public void init(HardwareMap hardwareMap) {
        holdServo = hardwareMap.get(Servo.class,"holdServo");
        rampServo = hardwareMap.get(Servo.class, "rampServo");
        holdServo.setPosition(holdServoPos);
        rampServo.setPosition(rampServoPos);
    }

    public void update() {
        holdServoPos = Math.max(0.0, Math.min(1.0, holdServoPos));
        holdServo.setPosition(holdServoPos);
    }

    public void servoBlock() {
        holdServoPos = 0.30; //0.75
        holdServo.setPosition(holdServoPos);
    }

    public void servoShoot() {
        holdServoPos = 0.53; //0.5
        holdServo.setPosition(holdServoPos);
    }

    public void servoUp() {
        rampServoPos = 1.0;
        rampServo.setPosition(rampServoPos);
    }

    public void servoDown() {
        rampServoPos = 0.0;
        rampServo.setPosition(rampServoPos);
    }

    public void holdServoIncrementUp() {
        holdServoPos = Math.min(1.0, holdServoPos + 0.01);

        holdServo.setPosition(holdServoPos);
    }

    public void holdServoIncrementDown() {
        holdServoPos = Math.max(0.0, holdServoPos - 0.01);

        holdServo.setPosition(holdServoPos);
    }

    public void rampServoIncrementUp() {
        rampServoPos = Math.min(1.0, rampServoPos + 0.01);

        rampServo.setPosition(rampServoPos);
    }

    public void rampServoIncrementDown() {
        rampServoPos = Math.max(0.0, rampServoPos - 0.01);

        rampServo.setPosition(rampServoPos);
    }

    public double getHoldPosition() {
        return holdServo.getPosition();
    }
    public double getRampServoPos() {
        return rampServo.getPosition();
    }
}
