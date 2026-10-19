package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ShooterSubclass {

    private DcMotorEx shooterMotor, shooterMotor2;

    public static double targetRPM = 3650;
    public static double tolerance = 50;
    public static final double ticksPerRev = 28.0;

    private boolean on = false;
    private boolean motorPowered = false;

    private double lowerThreshold = 0.0; //adjust later
    private double upperThreshold = 0.0; //adjust later

    public void init(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter2");

        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMotor2.setDirection(DcMotorSimple.Direction.FORWARD);

        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setPower(0.0);

        shooterMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor2.setPower(0.0);
    }

    public boolean isAtVelocity() {
        return isAtVelocity(targetRPM);
    }

    public boolean isAtVelocity(double customRPM) {
        return on && Math.abs(getRPM()- customRPM) <= tolerance;
    }

    public void shooterOn() {
        shooterOn(targetRPM);
    }

    public void shooterOn(double customRPM) {
        targetRPM = customRPM;
        on = true;
        motorPowered = true;
    }

    public void shooterOff() {
        on = false;
        motorPowered = false;
        shooterMotor.setPower(0.0);
        shooterMotor2.setPower(0.0);
    }

    public double getPower() {
        return motorPowered ? 1.0 : 0.0;
    }
    public double getRPM1() {
        return shooterMotor.getVelocity() * 60.0 / ticksPerRev;
    }
    public double getRPM2() {
        return shooterMotor2.getVelocity() * 60.0 / ticksPerRev;
    }

    public double getRPM() {
        return (getRPM1() + getRPM2() / 2.0);
    }
    public void update() {
        if(!on){
            return;
        }
        double rpm = getRPM();

        if (rpm < targetRPM - lowerThreshold) {
            motorPowered = true;
        } else if (rpm > targetRPM + upperThreshold) {
            motorPowered = false;
        }

        double power = motorPowered ? 1.0 : 0.0;

        shooterMotor.setPower(power);
        shooterMotor2.setPower(power);
    }

}