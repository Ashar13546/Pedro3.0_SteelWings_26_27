package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TransferSubclass {
    private DcMotorEx transferMotor, transferMotor2;

    public void init(HardwareMap hardwareMap) {
        transferMotor = hardwareMap.get(DcMotorEx.class, "transfer1");
        transferMotor2 = hardwareMap.get(DcMotorEx.class, "transfer2");

        transferMotor.setPower(0);
        transferMotor2.setPower(0);
    }

    public void transferOn() {
        transferMotor.setPower(1.0);
        transferMotor2.setPower(1.0);
    }

    public void transferReverse() {
        transferMotor.setPower(-1.0);
        transferMotor2.setPower(-1.0);
    }

    public void transferOff() {
        transferMotor.setPower(0);
        transferMotor2.setPower(0);
    }


}