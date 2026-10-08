package org.firstinspires.ftc.teamcode.pedro.examples.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.IntakeSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;

@TeleOp(name = "Anthony's Analog 3")
public class AnthonyAnalog3 extends OpMode {

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
        follower.manual(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        follower.update();


// Intake + Transfer
        if (gamepad1.left_trigger > 0.05) {
            intake.setIntakePower(gamepad1.left_trigger);
            transfer.setTransferPower(gamepad1.left_trigger);
        } else if (gamepad1.left_bumper) {
            intake.intakeReverse();
            transfer.transferReverse();
        } else {
            intake.intakeOff();
            transfer.transferOff();
        }

// Shooter
        if (gamepad1.right_trigger > 0.05) {
            shooter.shooterOn();
            if (gamepad1.right_bumper) {
                servo.servoShoot();
            }
        } else {
            shooter.shooterOff();
            servo.servoBlock();
        }

        telemetry.addData("Target RPM", ShooterSubclass.targetRPM);
        telemetry.addData("Actual RPM", shooter.getRPM());
        telemetry.addData("RPM Error",ShooterSubclass.targetRPM - shooter.getRPM());

        telemetry.addData("P", ShooterSubclass.P);
        telemetry.addData("I", ShooterSubclass.I);
        telemetry.addData("D", ShooterSubclass.D);
        telemetry.addData("F", ShooterSubclass.F);

        telemetry.addData("Servo Pos", servo.getPosition());

        telemetry.update();

    }
}