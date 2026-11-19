package org.firstinspires.ftc.teamcode.pedro.examples.frog.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command; // Import the Command interface/class used by Ivy
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.TransferSubclass;

import static com.pedropathing.api.Paths.line;
import static org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ShootSequence.rapidFire4;

@TeleOp(name = "2nd Firing Mech E Robot TeleOp 2 Saved Pos")
public class AnthonyAnalogFinalest3 extends OpMode {

    private Follower follower;
    private ShooterSubclass shooter;
    private TransferSubclass transfer;
    private ServoSubclass servo;

    private Command activeShootCommand = null;
    private boolean lastShootButton = false;

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
        Scheduler.reset();
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
        if (gamepad1.dpad_left && !lastDpadLeft) savedPose = follower.pose();
        lastDpadLeft = gamepad1.dpad_left;

        if (gamepad1.dpad_up && !lastUp) savedPose2 = follower.pose();
        lastUp = gamepad1.dpad_up;

        if (gamepad1.dpad_right && !lastDpadRight && savedPose != null) {
            Pose currentPose = follower.pose();
            Path path = line(currentPose, savedPose).linear(currentPose, savedPose);
            follower.follow(path);
            weaverDriving = true;
        }
        lastDpadRight = gamepad1.dpad_right;

        if (gamepad1.dpad_down && !lastDown && savedPose2 != null) {
            Pose currentPose = follower.pose();
            Path path = line(currentPose, savedPose2).linear(currentPose, savedPose2);
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
            if (!gamepad1.a) {
                follower.manual(
                        Math.signum(-gamepad1.left_stick_y) * Math.pow(Math.abs(gamepad1.left_stick_y), 1.25),
                        Math.signum(-gamepad1.left_stick_x) * Math.pow(Math.abs(gamepad1.left_stick_x), 1.25),
                        Math.signum(-gamepad1.right_stick_x) * Math.pow(Math.abs(gamepad1.right_stick_x), 1.25)
                );
            } else {
                follower.manual(
                        (-gamepad1.left_stick_y * 0.25),
                        (-gamepad1.left_stick_x * 0.25),
                        (-gamepad1.right_stick_x * 0.2)
                );
            }
        }
        follower.update();


        boolean isShootingSequenceRunning = (activeShootCommand != null && activeShootCommand.isScheduled());

        // Trigger sequence on Gamepad 2 X button (rising edge)
        boolean shootButtonPressed = gamepad2.x;
        if (shootButtonPressed && !lastShootButton && !isShootingSequenceRunning) {
            // Optional: pass ShooterSubclass.targetRPM instead of hardcoding 3150
            activeShootCommand = rapidFire4(3150, shooter, transfer, servo);
            Scheduler.schedule(activeShootCommand);
            isShootingSequenceRunning = true;
        }
        lastShootButton = shootButtonPressed;

        // Emergency Cancel: Gamepad 2 Y button stops the sequence immediately
        if (gamepad2.y && isShootingSequenceRunning) {
            Scheduler.cancel(activeShootCommand);
            isShootingSequenceRunning = false;
        }

        // --- MANUAL SUBSYSTEM CONTROLS ---
        // Only allow manual overrides/off-states if the sequence is NOT running
        if (!isShootingSequenceRunning) {
            // Transfer control
            if (gamepad1.x) {
                transfer.transferShoot();
            } else if (gamepad1.left_trigger > 0.05) {
                transfer.setTransferPower(gamepad1.left_trigger);
            } else if (gamepad1.left_bumper) {
                transfer.transferReverse();
            } else {
                transfer.transferOff();
            }

            // Shooter control
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

            // Hold Servo control
            if (gamepad1.right_bumper && !lastRB) {
                holdServoToggled = !holdServoToggled;
            }
            if (holdServoToggled) {
                servo.servoShoot();
            } else {
                servo.servoBlock();
            }

            // Ramp Servo control
            if (gamepad1.y && !lastYButtonPressed) {
                rampServoToggled = !rampServoToggled;
            }
            if (rampServoToggled) {
                servo.servoUp();
            } else {
                servo.servoDown();
            }
        }

        // Always update toggle tracking variables so they don't misfire when sequence ends
        lastRB = gamepad1.right_bumper;
        lastYButtonPressed = gamepad1.y;

        // Always update shooter PID/velocity loop
        shooter.update();

        // --- RPM PRESETS & ADJUSTMENTS ---
        if (gamepad2.start) {
            if (gamepad2.dpad_up && !lastDpadUp) ShooterSubclass.adjustTargetRPM(5);
            if (gamepad2.dpad_down && !lastDpadDown) ShooterSubclass.adjustTargetRPM(-5);
        }
        lastDpadUp = gamepad2.dpad_up;
        lastDpadDown = gamepad2.dpad_down;

        if (gamepad2.a && !lastA) ShooterSubclass.setTargetRPM(3300);
        if (gamepad2.b && !lastB) ShooterSubclass.setTargetRPM(3500);
        lastA = gamepad2.a;
        lastB = gamepad2.b;

        // --- RUN SCHEDULER ---
        Scheduler.execute();

        // --- TELEMETRY ---
        double shooterAmps = shooter.getAmps();
        telemetry.addData("Sequence Active", isShootingSequenceRunning);
        telemetry.addData("Shooter Amps", shooterAmps);
        RobotLog.ii("AmpLog", "RPM: %.1f | Amps: %.2f", shooter.getRPM(), shooterAmps);

        telemetry.addData("Target RPM", ShooterSubclass.targetRPM);
        telemetry.addData("Actual RPM", shooter.getRPM());
        telemetry.addData("RPM Error", ShooterSubclass.targetRPM - shooter.getRPM());
        telemetry.addData("Servo Pos", servo.getHoldPosition());
        telemetry.addData("x", follower.pose().x());
        telemetry.addData("y", follower.pose().y());
        telemetry.addData("heading", follower.pose().heading());
        telemetry.update();
    }
}