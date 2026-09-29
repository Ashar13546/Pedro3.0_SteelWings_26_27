package org.firstinspires.ftc.teamcode.pedro.examples.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;

@TeleOp(name = "Shooter PIDF + Servo Test")
public class ShooterMechanismTest extends OpMode {

    private ShooterSubclass shooter;
    private ServoSubclass servo;

    @Override
    public void init() {

        shooter = new ShooterSubclass();
        servo = new ServoSubclass();

        shooter.init(hardwareMap);
        servo.init(hardwareMap);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        shooter.updatePIDF();
        servo.update();

        // Shooter
        if (gamepad1.y) {
            shooter.shooterOn();
        } else {
            shooter.shooterOff();
        }


        // Servo
        if (gamepad1.dpad_up) {
            ServoSubclass.servoPos += 0.01;
        }

        if (gamepad1.dpad_down) {
            ServoSubclass.servoPos -= 0.01;
        }

        ServoSubclass.servoPos = Math.max(0.0, Math.min(1.0, ServoSubclass.servoPos));


        telemetry.addData("Target Velocity", ShooterSubclass.TARGET_VELOCITY);
        telemetry.addData("Actual Velocity", shooter.getVelocity());
        telemetry.addData("V Error",ShooterSubclass.TARGET_VELOCITY - shooter.getVelocity());

        telemetry.addData("P", ShooterSubclass.P);
        telemetry.addData("I", ShooterSubclass.I);
        telemetry.addData("D", ShooterSubclass.D);
        telemetry.addData("F", ShooterSubclass.F);

        telemetry.addData("Servo Pos", servo.getPosition());

        telemetry.update();
    }
}
