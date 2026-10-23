package org.firstinspires.ftc.teamcode.pedro.examples.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;

import static com.pedropathing.api.Paths.line;

@TeleOp(name = "Anthony's Analog Finaler4")
public class AnthonyAnalogFinaler4 extends OpMode {

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
    private boolean weaverDriving = false;
    private boolean hasRumbled = false;

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

        if (gamepad1.dpad_right && !lastDpadRight && savedPose != null) {
            Pose currentPose = follower.pose();

            Path path = line(currentPose, savedPose)
                    .linear(currentPose, savedPose);

            follower.follow(path);
            weaverDriving = true;
        }
        lastDpadRight = gamepad1.dpad_right;

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
            if (shooter.isAtVelocity() && !hasRumbled) {
                gamepad1.rumble(500);
                hasRumbled = true;
            }
        } else {
            shooter.shooterOff();
            hasRumbled = false;
        }
        shooter.update();

        if (gamepad1.back) {
            if (gamepad1.dpad_up && !lastDpadUp) {
                ShooterSubclass.adjustTargetRPM(5);
            }
            if (gamepad1.dpad_down && !lastDpadDown) {
                ShooterSubclass.adjustTargetRPM(-5);
            }
        }
        lastDpadUp = gamepad1.dpad_up;
        lastDpadDown = gamepad1.dpad_down;

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

        if (gamepad1.a && !lastA) {
            ShooterSubclass.setTargetRPM(3300);
        }
        if (gamepad1.b && !lastB) {
            ShooterSubclass.setTargetRPM(3500);
        }
        lastA = gamepad1.a;
        lastB = gamepad1.b;

        telemetry.addData("Target RPM", ShooterSubclass.targetRPM);
        telemetry.addData("Actual RPM", shooter.getRPM());
        telemetry.addData("RPM Error",
                ShooterSubclass.targetRPM - shooter.getRPM());
        telemetry.addData("Servo Pos", servo.getHoldPosition());
        telemetry.update();
    }
}