package org.firstinspires.ftc.teamcode.pedro.examples.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;

@TeleOp(name = "ShooterPIDTuning")
public class ShooterPIDF extends OpMode {

    private ShooterSubclass shooter;

    @Override
    public void init() {
        shooter = new ShooterSubclass();
        shooter.init(hardwareMap);
    }

    @Override
    public void loop() {

// Shooter
        if (gamepad1.right_trigger > 0.05) {
            shooter.shooterOn();
        } else {
            shooter.shooterOff();
        }

        telemetry.addData("Target RPM", ShooterSubclass.targetRPM);
        telemetry.addData("Actual RPM", shooter.getRPM());
        telemetry.addData("RPM Error",ShooterSubclass.targetRPM - shooter.getRPM());

        telemetry.update();

    }
}