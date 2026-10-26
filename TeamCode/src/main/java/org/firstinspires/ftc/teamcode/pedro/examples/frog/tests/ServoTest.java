//package org.firstinspires.ftc.teamcode.pedro.examples.tests;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
//
//public class ServoTest extends OpMode {
//
//    private ServoSubclass servo;
//    private boolean lastDpadUp = false;
//    private boolean lastDpadDown = false;
//    private boolean lastDpadLeft = false;
//    private boolean lastDpadRight = false;
//
//    @Override
//    public void init() {
//        servo = new ServoSubclass();
//        servo.init(hardwareMap);
//    }
//
//    @Override
//    public void loop() {
//        // Ramp Servo
//        if (gamepad1.dpad_up && !lastDpadUp) {
//            servo.rampServoIncrementUp();
//        }
//        if (gamepad1.dpad_down && !lastDpadDown) {
//            servo.rampServoIncrementDown();
//        }
//        lastDpadUp = gamepad1.dpad_up;
//        lastDpadDown = gamepad1.dpad_down;
//
//        // Hold Servo
//        if (gamepad1.dpad_right && !lastDpadRight) {
//            servo.holdServoIncrementUp();
//        }
//        if (gamepad1.dpad_left && !lastDpadLeft) {
//            servo.holdServoIncrementDown();
//        }
//        lastDpadRight = gamepad1.dpad_right;
//        lastDpadLeft = gamepad1.dpad_left;
//
//        telemetry.addData("Ramp Servo Pos", servo.getRampServoPos());
//        telemetry.addData("Hold Servo Pos", servo.getHoldPosition());
//    }
//}
