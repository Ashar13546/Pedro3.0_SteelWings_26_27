package org.firstinspires.ftc.teamcode.pedro.examples.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.DualShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;

@TeleOp(name = "DualShooterTest")
public class DualShooterTest extends OpMode {
    private DualShooterSubclass dualShooter;

    @Override
    public void init() {
        dualShooter = new DualShooterSubclass();
        dualShooter.init(hardwareMap);
    }

    @Override
    public void loop() {
// Transfer
        if (gamepad1.right_trigger > 0.1) {
            dualShooter.dualShooterOn();
        } else if (gamepad1.left_trigger > 0.1) {
            dualShooter.dualShooterReverse();
        } else {
            dualShooter.dualShooterOff();
        }
    }
}
