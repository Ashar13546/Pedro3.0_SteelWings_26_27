package org.firstinspires.ftc.teamcode.pedro.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "test1", group = "TeleOp")
public class test extends LinearOpMode {

    private DcMotorEx shooter;
    private Servo servo;

    private double servoPosition = 0.95;

    private static double SHOOTER_VELOCITY = 1620;

    private static final double P = 90.0;
    private static final double I = 0.0;
    private static final double D = 8.0;
    private static final double F = 13.5;

    @Override
    public void runOpMode() {

        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        servo = hardwareMap.get(Servo.class, "servo");

        shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        shooter.setVelocityPIDFCoefficients(P, I, D, F);

        servo.setPosition(servoPosition);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.y) {
                SHOOTER_VELOCITY += 10;
            }
            if (gamepad1.x) {
                SHOOTER_VELOCITY -= 10;
            }

            if (gamepad1.a) {
                shooter.setVelocity(SHOOTER_VELOCITY);
            }

            if (gamepad1.b) {
                shooter.setVelocity(0);
            }

            if (gamepad1.dpad_up) {
                servoPosition += 0.01;
                servoPosition = Math.min(servoPosition, 1.0);
                servo.setPosition(servoPosition);

                while (gamepad1.dpad_up && opModeIsActive()) {
                    idle();
                }
            }

            if (gamepad1.dpad_down) {
                servoPosition -= 0.01;
                servoPosition = Math.max(servoPosition, 0.0);
                servo.setPosition(servoPosition);

                while (gamepad1.dpad_down && opModeIsActive()) {
                    idle();
                }
            }

            telemetry.addData("Shooter Velocity", shooter.getVelocity());
            telemetry.addData("Target Velocity", SHOOTER_VELOCITY);
            telemetry.addData("Servo Position", servoPosition);
            telemetry.update();
        }
    }
}
