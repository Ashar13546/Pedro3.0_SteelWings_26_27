package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.acmerobotics.dashboard.DashboardCore;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class ShooterSubclass {

    private DcMotorEx shooterMotor;

    public static double P = 14.043428; // 0.0002
    public static double I = 14.043428; // 0.0002
    public static double D = 0.0; // 0.0
    public static double F = 11.72626; // 0.000167

    public static double targetRPM = 3250;
    public static final double ticksPerRev = 28.0;

    public void init(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void updatePIDF() {
        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
    }

    public void shooterOn() {
        shooterMotor.setVelocity((ticksPerRev * targetRPM)/60.0);
    }

    // overloaded incase different rpm needed for diff points on field
    public void shooterOn(double customRPM) {
        shooterMotor.setVelocity((customRPM * ticksPerRev)/60.0);
    }

    public void shooterOff() {
        shooterMotor.setVelocity(0);
    }

    public double getRPM() {
        return (shooterMotor.getVelocity() / ticksPerRev) * 60.0;
    }
}