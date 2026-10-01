package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
public class ShooterSubclass {

    private DcMotorEx shooterMotor;

    public static double P = 80.0;
    public static double I = 0.0;
    public static double D = 8.0;
    public static double F = 13.5;

    public static double TARGET_VELOCITY = 1800;

    public void init(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class,"shooter");
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        updatePIDF();
    }

    public void updatePIDF() {
        PIDFCoefficients pidf = new PIDFCoefficients(P,I,D,F);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
    }

    public void shooterOn() {
        shooterMotor.setVelocity(TARGET_VELOCITY);
    }

    public void shooterOff() {
        shooterMotor.setVelocity(0);
    }

    public double getVelocity() {
        return shooterMotor.getVelocity();
    }

    public double getPower() {
        return shooterMotor.getPower();
    }
}
