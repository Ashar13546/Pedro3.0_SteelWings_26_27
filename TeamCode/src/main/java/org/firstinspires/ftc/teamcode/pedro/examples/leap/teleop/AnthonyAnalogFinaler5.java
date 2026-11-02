package org.firstinspires.ftc.teamcode.pedro.examples.leap.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.leap.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.leap.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.leap.subclasses.TransferSubclass;

import static com.pedropathing.api.Paths.line;

@TeleOp(name = "Main TeleOp 2 Saved Pos")
public class AnthonyAnalogFinaler5 extends OpMode {

    private Follower follower;

    private ShooterSubclass shooter;
    private TransferSubclass transfer;
    private ServoSubclass servo;

    private boolean holdServoToggled = false;
    private boolean lastRB = false;
    private boolean rampServoToggled = false;
    private boolean lastYButtonPressed = false;

    private boolean lastDpadUp = false;
    private boolean lastDpadDown = false;
    private boolean lastA = false;
    private boolean lastB = false;

    private Pose savedPose = null;
    private Pose savedPose2 = null;
    private boolean weaverDriving = false;
    private boolean hasRumbled = false;

    private boolean lastUp = false;
    private boolean lastDown = false;
    private boolean lastDpadLeft = false;
    private boolean lastDpadRight = false;

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
        if (gamepad1.dpad_left && !lastDpadLeft) {
            savedPose = follower.pose();
        }
        lastDpadLeft = gamepad1.dpad_left;

        if (gamepad1.dpad_up && !lastUp) {
            savedPose2 = follower.pose();
        }
        lastUp = gamepad1.dpad_up;

        if (gamepad1.dpad_right && !lastDpadRight && savedPose != null) {
            Pose currentPose = follower.pose();

            Path path = line(currentPose, savedPose)
                    .linear(currentPose, savedPose);

            follower.follow(path);
            weaverDriving = true;
        }
        lastDpadRight = gamepad1.dpad_right;

        if (gamepad1.dpad_down && !lastDown && savedPose2 != null) {
            Pose currentPose = follower.pose();

            Path path = line(currentPose, savedPose2)
                    .linear(currentPose, savedPose2);

            follower.follow(path);
            weaverDriving = true;
        }
        lastDown = gamepad1.dpad_down;

        if (weaverDriving) {
            if (!follower.isBusy() || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
                weaverDriving = false;
            }
        }

        if (!weaverDriving) {
            follower.manual(
                    Math.signum(-gamepad1.left_stick_y) * Math.pow(Math.abs(gamepad1.left_stick_y), 1.25),
                    Math.signum(-gamepad1.left_stick_x) * Math.pow(Math.abs(gamepad1.left_stick_x), 1.25),
                    Math.signum(-gamepad1.right_stick_x) * Math.pow(Math.abs(gamepad1.right_stick_x), 1.25)
            );
        }

        follower.update();

        if (gamepad1.x) {
            transfer.transferShoot();
        } else if (gamepad1.left_trigger > 0.05) {
            transfer.setTransferPower(gamepad1.left_trigger);
        } else if (gamepad1.left_bumper) {
            transfer.transferReverse();
        } else {
            transfer.transferOff();
        }
        if (gamepad1.right_trigger > 0.05) {
            shooter.shooterOn(ShooterSubclass.targetRPM);
            if (shooter.isAtVelocity(ShooterSubclass.targetRPM) && !hasRumbled) {
                gamepad1.rumble(500);
                hasRumbled = true;
            }
        } else {
            shooter.shooterOff();
            hasRumbled = false;
        }
        shooter.update();

        if (gamepad2.start) {
            if (gamepad2.dpad_up && !lastDpadUp) {
                ShooterSubclass.adjustTargetRPM(5);
            }
            if (gamepad2.dpad_down && !lastDpadDown) {
                ShooterSubclass.adjustTargetRPM(-5);
            }
        }
        lastDpadUp = gamepad2.dpad_up;
        lastDpadDown = gamepad2.dpad_down;

        if (gamepad1.right_bumper && !lastRB) {
            holdServoToggled = !holdServoToggled;
        }
        lastRB = gamepad1.right_bumper;

        if (holdServoToggled) {
            servo.servoShoot();
        } else {
            servo.servoBlock();
        }

        if (gamepad1.y && !lastYButtonPressed) {
            rampServoToggled = !rampServoToggled;
        }
        lastYButtonPressed = gamepad1.y;

        if (rampServoToggled) {
            servo.servoUp();
        } else {
            servo.servoDown();
        }

        if (gamepad2.a && !lastA) {
            ShooterSubclass.setTargetRPM(3300);
        }
        if (gamepad2.b && !lastB) {
            ShooterSubclass.setTargetRPM(3500);
        }
        lastA = gamepad2.a;
        lastB = gamepad2.b;

        telemetry.addData("Target RPM", ShooterSubclass.targetRPM);
        telemetry.addData("Actual RPM", shooter.getRPM());
        telemetry.addData("RPM Error",
                ShooterSubclass.targetRPM - shooter.getRPM());
        telemetry.addData("Servo Pos", servo.getHoldPosition());
        telemetry.update();
    }
}