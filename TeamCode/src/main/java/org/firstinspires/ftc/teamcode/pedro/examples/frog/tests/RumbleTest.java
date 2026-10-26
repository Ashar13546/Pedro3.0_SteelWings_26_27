package org.firstinspires.ftc.teamcode.pedro.examples.frog.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Rumble Test")
public class RumbleTest extends OpMode {

    @Override
    public void init() {
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            gamepad1.rumble(1.0, 1.0, 100);
        }

        else if (gamepad1.x) {
            gamepad1.rumbleBlips(2);
        }

        else if (gamepad1.y) {
            gamepad1.rumble(500);
        }
    }
}
