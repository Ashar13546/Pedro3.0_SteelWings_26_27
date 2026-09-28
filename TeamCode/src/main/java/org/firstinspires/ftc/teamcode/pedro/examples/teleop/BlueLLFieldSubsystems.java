package org.firstinspires.ftc.teamcode.pedro.examples.teleop;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.IntakeSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.LimeLightAprilTag;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;

@TeleOp(name = "Blue Limelight + Field + Subsystems")
public class BlueLLFieldSubsystems extends LinearOpMode {

    private Follower follower;
    private IntakeSubclass intake;
    private ShooterSubclass shooter;
    private TransferSubclass transfer;

    private Limelight3A limelight;

    private LimeLightAprilTag oppositeTracker;
    private LimeLightAprilTag audienceTracker;

    private static final double ALIGN_P = 0.04;

    @Override
    public void runOpMode() throws InterruptedException {
        follower = Constants.create(hardwareMap);

        intake = new IntakeSubclass();
        shooter = new ShooterSubclass();
        transfer = new TransferSubclass();

        intake.init(hardwareMap);
        shooter.init(hardwareMap);
        transfer.init(hardwareMap);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        oppositeTracker = new LimeLightAprilTag(limelight, LimeLightAprilTag.Cluster.BLUE_OPPOSITE);
        audienceTracker = new LimeLightAprilTag(limelight, LimeLightAprilTag.Cluster.BLUE_AUDIENCE);

        limelight.pipelineSwitch(0);
        limelight.start();

        waitForStart();

        while (opModeIsActive()) {
            oppositeTracker.update();
            audienceTracker.update();

            double drive = -gamepad1.left_stick_y;
            double strafe = -gamepad1.left_stick_x;
            double turn = -gamepad1.right_stick_x;

            LimeLightAprilTag activeTracker = null;
            if (gamepad1.dpad_up) {
                activeTracker = oppositeTracker;
            } else if (gamepad1.dpad_down) {
                activeTracker = audienceTracker;
            }

            if (activeTracker != null && activeTracker.hasCluster()) {
                double errorX = activeTracker.getCenterX();
                double deadzone = 1.0;

                if(Math.abs(errorX) > deadzone) {
                    turn = errorX * ALIGN_P;
                    turn = Math.max(-0.5, Math.min(0.5, turn));
                }
                else {
                    turn = 0.0;
                }
            }

            DrivePowers powers = ManualDrive.fieldCentric(
                    drive,
                    strafe,
                    turn,
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
        }

        if (limelight != null) {
            limelight.stop();
        }
    }
}
