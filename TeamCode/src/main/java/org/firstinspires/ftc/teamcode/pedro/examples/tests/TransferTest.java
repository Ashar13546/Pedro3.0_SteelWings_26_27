package org.firstinspires.ftc.teamcode.pedro.examples.tests;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.IntakeSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;

@TeleOp(name = "TransferTest")
public class TransferTest extends OpMode {
    private TransferSubclass transfer;

    @Override
    public void init() {
        transfer = new TransferSubclass();
        transfer.init(hardwareMap);
    }

    @Override
    public void loop() {
// Transfer
        if (gamepad1.right_trigger > 0.1) {
            transfer.transferOn();
        } else if (gamepad1.left_trigger > 0.1) {
            transfer.transferReverse();
        } else {
            transfer.transferOff();
        }
    }
}
