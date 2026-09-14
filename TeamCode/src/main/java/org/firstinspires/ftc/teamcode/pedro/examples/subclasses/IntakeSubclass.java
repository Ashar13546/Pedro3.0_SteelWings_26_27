package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeSubclass {
    private DcMotorEx intakeMotor;
    private DcMotorEx shooter;

    public void init(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");

        intakeMotor.setPower(0);
    }

    public void intakeOn() {
        intakeMotor.setPower(1.0);
    }

    public void intakeOff() {
        intakeMotor.setPower(0);
    }


}
