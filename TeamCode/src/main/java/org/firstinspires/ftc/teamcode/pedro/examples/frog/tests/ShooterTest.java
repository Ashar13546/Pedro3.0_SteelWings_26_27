package org.firstinspires.ftc.teamcode.pedro.examples.frog.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ShooterTest1;

@TeleOp(name = "Shooter Tuning")
public class ShooterTest extends LinearOpMode {

    private ShooterTest1 shooter = new ShooterTest1();

    @Override
    public void runOpMode() throws InterruptedException {
        shooter.init(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                shooter.shooterOn();
            } else if (gamepad1.b) {
                shooter.shooterOff();
            }

            shooter.update();

            telemetry.addData("Target RPM", ShooterTest1.targetRPM);
            telemetry.addData("Current Filtered RPM", "%.1f", shooter.getRPM());
            telemetry.addData("RPM Error", "%.1f", ShooterTest1.targetRPM - shooter.getRPM());

            telemetry.addLine("\nINDIVIDUAL MOTOR SPEEDS");
            telemetry.addData("shooter RPM", "%.1f", shooter.getRPM1());
            telemetry.addData("shooter2 RPM", "%.1f", shooter.getRPM2());

            telemetry.addLine("\n");
            telemetry.addData("Applied Motor Power", "%.3f", shooter.getPower());

            telemetry.update();
        }
    }
}
