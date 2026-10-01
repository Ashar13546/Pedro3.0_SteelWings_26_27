package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DualShooterSubclass {
    private DcMotorEx dualshooterMotor1, dualShooterMotor2;

    public void init(HardwareMap hardwareMap) {
        dualshooterMotor1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        dualShooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter2");

        dualshooterMotor1.setPower(0);
        dualShooterMotor2.setPower(0);
    }

    public void dualShooterOn() {
        dualshooterMotor1.setPower(1.0);
        dualShooterMotor2.setPower(-1.0);
    }

    public void dualShooterReverse() {
        dualshooterMotor1.setPower(-1.0);
        dualShooterMotor2.setPower(1.0);
    }

    public void dualShooterOff() {
        dualshooterMotor1.setPower(0);
        dualShooterMotor2.setPower(0);
    }


}