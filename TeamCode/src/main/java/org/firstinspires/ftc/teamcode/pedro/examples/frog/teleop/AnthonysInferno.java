package org.firstinspires.ftc.teamcode.pedro.examples.frog.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.skeletonarmy.marrow.weaver.PathConfig;
import com.skeletonarmy.marrow.weaver.PathResult;
import com.skeletonarmy.marrow.weaver.PedroPathingConverter;
import com.skeletonarmy.marrow.weaver.Weaver;
import com.skeletonarmy.marrow.zones.Point;

import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.TransferSubclass;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Random;

@TeleOp(name = "Anthony's Inferno")
public class AnthonysInferno extends OpMode {

    private Follower follower;
    private ShooterSubclass shooter;
    private TransferSubclass transfer;
    private ServoSubclass servo;

    private boolean holdServoToggled = false;
    private boolean lastRB = false;
    private boolean rampServoToggled = false;
    private boolean lastYButtonPressed = false;
    private boolean transferShooting = false;
    private boolean lastXButtonPressed = false;
    private boolean lastDpadUp = false;
    private boolean lastDpadDown = false;
    private boolean lastA = false;
    private boolean lastB = false;

    private Pose savedPose = null;
    private boolean weaverDriving = false;
    private boolean hasRumbled = false;

    private ElapsedTime timer = new ElapsedTime();
    private Random random = new Random();
    private int preset = 0;

    @Override
    public void init() {
        Weaver.setConfig(new PathConfig().intakeWidth(7.0).robotWidth(18.0).robotHeight(18.0));
        follower = Constants.create(hardwareMap);
        shooter = new ShooterSubclass();
        transfer = new TransferSubclass();
        servo = new ServoSubclass();

        shooter.init(hardwareMap);
        transfer.init(hardwareMap);
        servo.init(hardwareMap);
        timer.reset();
    }

    @Override
    public void loop() {
        if (timer.seconds() >= 30.0) {
            preset = random.nextInt(3);
            timer.reset();
            gamepad1.rumbleBlips(3);
        }

        double ly = 0;
        double lx = 0;
        double rx = 0;

        boolean shootPressed = false;
        boolean transferPressed = false;
        double intakePower = 0;

        if (preset == 0) {
            ly = gamepad1.left_stick_y;
            lx = gamepad1.left_stick_x;
            rx = gamepad1.right_stick_x;
            shootPressed = (gamepad1.right_trigger > 0.05);
            transferPressed = gamepad1.x;
            intakePower = gamepad1.left_trigger;
        } else if (preset == 1) {
            ly = gamepad1.right_stick_y;
            lx = gamepad1.right_stick_x;
            rx = gamepad1.left_stick_x;
            shootPressed = gamepad1.a;
            transferPressed = gamepad1.y;
            intakePower = gamepad1.right_trigger;
        } else if (preset == 2) {
            ly = -gamepad1.left_stick_x;
            lx = gamepad1.left_stick_y;
            rx = gamepad1.right_stick_x;
            shootPressed = gamepad1.left_bumper;
            transferPressed = gamepad1.b;
            intakePower = gamepad1.left_trigger;
        }

        if (gamepad1.dpad_left) {
            savedPose = follower.pose();
        }

        if (gamepad1.dpad_right && savedPose != null) {
            PathResult result = Weaver.builder()
                    .start(new Point(follower.pose().x(), follower.pose().y(), follower.pose().heading()))
                    .end(new Point(savedPose.x(), savedPose.y(), savedPose.heading()))
                    .build();
            follower.follow(PedroPathingConverter.toPath(result));
            weaverDriving = true;
        }

        if (weaverDriving) {
            if (!follower.isBusy() || Math.abs(ly) > 0.1 || Math.abs(lx) > 0.1 || Math.abs(rx) > 0.1) {
                weaverDriving = false;
            }
        } else {
            follower.manual(
                    Math.signum(-ly) * Math.pow(Math.abs(ly), 1.25),
                    Math.signum(-lx) * Math.pow(Math.abs(lx), 1.25),
                    Math.signum(-rx) * Math.pow(Math.abs(rx), 1.25)
            );
        }

        follower.update();

        if (transferPressed && !lastXButtonPressed) {
            transferShooting = !transferShooting;
        }
        lastXButtonPressed = transferPressed;

        if (transferShooting) {
            transfer.transferShoot();
        } else if (intakePower > 0.05) {
            transfer.setTransferPower(intakePower);
        } else if (gamepad1.left_bumper && preset != 2) {
            transfer.transferReverse();
        } else {
            transfer.transferOff();
        }

        if (shootPressed) {
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

        if (gamepad1.y && preset != 1 && !lastYButtonPressed) {
            rampServoToggled = !rampServoToggled;
        }
        lastYButtonPressed = (gamepad1.y && preset != 1);
        if (rampServoToggled) {
            servo.servoUp();
        } else {
            servo.servoDown();
        }

        if (gamepad1.a && preset != 1 && !lastA) {
            ShooterSubclass.setTargetRPM(3300);
        }
        if (gamepad1.b && preset != 2 && !lastB) {
            ShooterSubclass.setTargetRPM(3500);
        }
        lastA = (gamepad1.a && preset != 1);
        lastB = (gamepad1.b && preset != 2);

        telemetry.addData("Target RPM", ShooterSubclass.targetRPM);
        telemetry.addData("Actual RPM", shooter.getRPM());
        telemetry.update();
    }
}