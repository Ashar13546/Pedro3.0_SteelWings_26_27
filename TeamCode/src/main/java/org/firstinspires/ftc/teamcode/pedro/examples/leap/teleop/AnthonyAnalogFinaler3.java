//package org.firstinspires.ftc.teamcode.pedro.examples.teleop;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.math.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.skeletonarmy.marrow.weaver.PathConfig;
//import com.skeletonarmy.marrow.weaver.PathResult;
//import com.skeletonarmy.marrow.weaver.PedroPathingConverter;
//import com.skeletonarmy.marrow.weaver.Weaver;
//import com.skeletonarmy.marrow.zones.Point;
//
//import org.firstinspires.ftc.teamcode.pedro.Constants;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;
//
//@TeleOp(name = "Anthony's Analog Finaler3")
//public class AnthonyAnalogFinaler3 extends OpMode {
//
//    private Follower follower;
//
//    private ShooterSubclass shooter;
//    private TransferSubclass transfer;
//    private ServoSubclass servo;
//
//    private boolean holdServoToggled = false;
//    private boolean lastRB = false;
//    private boolean rampServoToggled = false;
//    private boolean lastYButtonPressed = false;
//
//    private boolean transferShooting = false;
//    private boolean lastXButtonPressed = false;
//
//    private boolean lastDpadUp = false;
//    private boolean lastDpadDown = false;
//    private boolean lastA = false;
//    private boolean lastB = false;
//
//    private Pose savedPose = null;
//    private boolean weaverDriving = false;
//    private boolean hasRumbled = false;
//
//    @Override
//    public void init() {
//        Weaver.setConfig(new PathConfig()
//                .intakeWidth(7.0)
//                .robotWidth(18.0)
//                .robotHeight(18.0));
//
//        follower = Constants.create(hardwareMap);
//
//        shooter = new ShooterSubclass();
//        transfer = new TransferSubclass();
//        servo = new ServoSubclass();
//
//        shooter.init(hardwareMap);
//        transfer.init(hardwareMap);
//        servo.init(hardwareMap);
//    }
//
//    @Override
//    public void loop() {
//        if (gamepad1.dpad_left) {
//            savedPose = follower.pose();
//        }
//
//        if (gamepad1.dpad_right && savedPose != null) {
//            PathResult result = Weaver.builder()
//                    .start(new Point(follower.pose().x(), follower.pose().y(), follower.pose().heading()))
//                    .end(new Point(savedPose.x(), savedPose.y(), savedPose.heading()))
//                    .build();
//            follower.follow(PedroPathingConverter.toPath(result));
//            weaverDriving = true;
//        }
//
//        if (weaverDriving) {
//            if (!follower.isBusy() || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
//                weaverDriving = false;
//            }
//        } else {
//            follower.manual(
//                    Math.signum(-gamepad1.left_stick_y) * Math.pow(Math.abs(gamepad1.left_stick_y),1.25),
//                    Math.signum(-gamepad1.left_stick_x) * Math.pow(Math.abs(gamepad1.left_stick_x),1.25),
//                    Math.signum(-gamepad1.right_stick_x) * Math.pow(Math.abs(gamepad1.right_stick_x),1.25)
//            );
//        }
//
//        follower.update();
//
//        if (gamepad1.x && !lastXButtonPressed) {
//            transferShooting = !transferShooting;
//        }
//        lastXButtonPressed = gamepad1.x;
//        if (transferShooting) {
//            transfer.transferShoot();
//        }
//        else if (gamepad1.left_trigger > 0.05) {
//            transfer.setTransferPower(gamepad1.left_trigger);
//        } else if (gamepad1.left_bumper) {
//            transfer.transferReverse();
//        } else {
//            transfer.transferOff();
//        }
//
//// Shooter
//        if (gamepad1.right_trigger > 0.05) {
//            shooter.shooterOn(ShooterSubclass.targetRPM);
//            if (shooter.isAtVelocity() && !hasRumbled) {
//                gamepad1.rumble(500);
//                hasRumbled = true;
//            }
//        } else {
//            shooter.shooterOff();
//            hasRumbled = false;
//        }
//        shooter.update();
//
//        if (gamepad1.back) {
//            if (gamepad1.dpad_up && !lastDpadUp) {
//                ShooterSubclass.adjustTargetRPM(5);
//            }
//            if (gamepad1.dpad_down && !lastDpadDown) {
//                ShooterSubclass.adjustTargetRPM(-5);
//            }
//        }
//        lastDpadUp = gamepad1.dpad_up;
//        lastDpadDown = gamepad1.dpad_down;
//
//// Hold Servo
//        if (gamepad1.right_bumper && !lastRB) {
//            holdServoToggled = !holdServoToggled;
//        }
//        lastRB = gamepad1.right_bumper;
//        if (holdServoToggled) {
//            servo.servoShoot();
//        }
//        else {
//            servo.servoBlock();
//        }
//// Ramp Servo
//        if (gamepad1.y && !lastYButtonPressed) {
//            rampServoToggled = !rampServoToggled;
//        }
//        lastYButtonPressed = gamepad1.y;
//        if (rampServoToggled) {
//            servo.servoUp();
//        }
//        else {
//            servo.servoDown();
//        }
//
//        if (gamepad1.a && !lastA) {
//            ShooterSubclass.setTargetRPM(3300);
//        }
//        if (gamepad1.b && !lastB) {
//            ShooterSubclass.setTargetRPM(3500);
//        }
//        lastA = gamepad1.a;
//        lastB = gamepad1.b;
//        lastXButtonPressed = gamepad1.x;
//
//        telemetry.addData("Target RPM", ShooterSubclass.targetRPM);
//        telemetry.addData("Actual RPM", shooter.getRPM());
//        telemetry.addData("RPM Error",ShooterSubclass.targetRPM - shooter.getRPM());
//
////        telemetry.addData("P", ShooterSubclass.P);
////        telemetry.addData("I", ShooterSubclass.I);
////        telemetry.addData("D", ShooterSubclass.D);
////        telemetry.addData("F", ShooterSubclass.F);
//
//        telemetry.addData("Servo Pos", servo.getHoldPosition());
//        telemetry.update();
//    }
//}
