package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class ShooterSubclass {
    DcMotorEx shooterMotor;
    public static double P = 70.0;
    public static double I = 0.0;
    public static double D = 8.0;
    public static double F = 13.5;

    public static double TARGET_VELOCITY = 1500.0;

    public void init(HardwareMap hardwareMap) {

        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");

        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients newPidf = new PIDFCoefficients(P, I, D, F);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, newPidf);
    }

    public void shooterOn() {
        shooterMotor.setVelocity(TARGET_VELOCITY);
    }

    public void shooterOff() {
        shooterMotor.setVelocity(0);
    }
}