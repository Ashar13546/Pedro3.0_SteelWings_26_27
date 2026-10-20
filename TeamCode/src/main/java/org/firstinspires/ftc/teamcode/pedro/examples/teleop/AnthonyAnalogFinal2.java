package org.firstinspires.ftc.teamcode.pedro.examples.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;

@TeleOp(name = "Anthony's Analog Final2")
public class AnthonyAnalogFinal2 extends OpMode {

    private Follower follower;

    private ShooterSubclass shooter;
    private TransferSubclass transfer;
    private ServoSubclass servo;

    private boolean holdServoToggled = false;
    private boolean lastRB = false;
    private boolean rampServoToggled = false;
    private boolean lastDpadUp = false;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);

        shooter = new ShooterSubclass();
        transfer = new TransferSubclass();
        servo = new ServoSubclass();

        shooter.init(hardwareMap);
        transfer.init(hardwareMap);
        servo.init(hardwareMap);
    }

    @Override
    public void loop() {
        follower.manual(
                Math.signum(-gamepad1.left_stick_y) * Math.pow(Math.abs(gamepad1.left_stick_y),1.25),
                Math.signum(-gamepad1.left_stick_x) * Math.pow(Math.abs(gamepad1.left_stick_x),1.25),
                Math.signum(-gamepad1.right_stick_x) * Math.pow(Math.abs(gamepad1.right_stick_x),1.25)
        );

        follower.update();


// Intake + Transfer
        if (gamepad1.left_trigger > 0.05) {
            transfer.setTransferPower(gamepad1.left_trigger);
        } else if (gamepad1.left_bumper) {
            transfer.transferReverse();
        } else {
            transfer.transferOff();
        }

// Shooter
        if (gamepad1.right_trigger > 0.05) {
            shooter.shooterOn();
        } else {
            shooter.shooterOff();
        }
        shooter.update();

// Hold Servo
        if (gamepad1.right_bumper && !lastRB) {
            holdServoToggled = !holdServoToggled;
        }
        lastRB = gamepad1.right_bumper;
        if (holdServoToggled) {
            servo.servoShoot();
            transfer.transferShoot();
        }
        else {
            servo.servoBlock();
        }
// Ramp Servo
        if (gamepad1.dpad_up && !lastDpadUp) {
            rampServoToggled = !rampServoToggled;
        }
        lastDpadUp = gamepad1.dpad_up;
        if (rampServoToggled) {
            servo.servoUp();
        }
        else {
            servo.servoDown();
        }

        telemetry.addData("Target RPM", ShooterSubclass.targetRPM);
        telemetry.addData("Actual RPM", shooter.getRPM());
        telemetry.addData("RPM Error",ShooterSubclass.targetRPM - shooter.getRPM());

//        telemetry.addData("P", ShooterSubclass.P);
//        telemetry.addData("I", ShooterSubclass.I);
//        telemetry.addData("D", ShooterSubclass.D);
//        telemetry.addData("F", ShooterSubclass.F);

        telemetry.addData("Servo Pos", servo.getHoldPosition());

        telemetry.update();

    }
}