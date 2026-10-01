package org.firstinspires.ftc.teamcode.pedro.examples.teleop;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.IntakeSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;

@TeleOp(name = "BlueField W/Subsystems")
public class BlueFieldSubsystems extends OpMode {

    private Follower follower;

    private IntakeSubclass intake;
    private ShooterSubclass shooter;
    private TransferSubclass transfer;
    private ServoSubclass servo;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);

        intake = new IntakeSubclass();
        shooter = new ShooterSubclass();
        transfer = new TransferSubclass();
        servo = new ServoSubclass();

        intake.init(hardwareMap);
        shooter.init(hardwareMap);
        transfer.init(hardwareMap);
        servo.init(hardwareMap);
    }

    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();

// Intake
        if (gamepad1.right_bumper) {
            intake.intakeOn();
        } else if (gamepad1.left_bumper) {
            intake.intakeReverse();
        } else {
            intake.intakeOff();
        }

// Shooter
        if (gamepad1.y) {
            shooter.shooterOn();
        } else if (gamepad1.a) {
            shooter.shooterOff();
        }

// Transfer
        if (gamepad1.right_trigger > 0.1) {
            transfer.transferOn();
        } else if (gamepad1.left_trigger > 0.1) {
            transfer.transferReverse();
        } else {
            transfer.transferOff();
        }

        if (gamepad1.dpad_up) {
            servo.servoIncrementUp();
        }

        if (gamepad1.dpad_down) {
            servo.servoIncrementDown();
        }

    }
}
