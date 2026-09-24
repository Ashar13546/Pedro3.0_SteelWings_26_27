package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TransferSubclass {
    private DcMotorEx transferMotor;

    public void init(HardwareMap hardwareMap) {
        transferMotor = hardwareMap.get(DcMotorEx.class, "intake");

        transferMotor.setPower(0);
    }

    public void transferOn() {
        transferMotor.setPower(1.0);
    }

    public void transferOff() {
        transferMotor.setPower(0);
    }


}