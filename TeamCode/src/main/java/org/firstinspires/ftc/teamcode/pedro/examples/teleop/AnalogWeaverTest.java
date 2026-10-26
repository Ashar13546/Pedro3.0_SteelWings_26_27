//package org.firstinspires.ftc.teamcode.pedro.examples.teleop;
//
//import com.pedropathing.api.PoseFactory;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.math.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.skeletonarmy.marrow.weaver.PathConfig;
//import com.skeletonarmy.marrow.weaver.PathResult;
//import com.skeletonarmy.marrow.weaver.PedroPathingConverter;
//import com.skeletonarmy.marrow.weaver.Weaver;
//import com.skeletonarmy.marrow.zones.Point;
//import com.skeletonarmy.marrow.zones.PolygonZone;
//
//import org.firstinspires.ftc.teamcode.pedro.Constants;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;
//
//@TeleOp(name = "Analog Weaver Test 1")
//public class AnalogWeaverTest extends OpMode {
//
//    private Follower follower;
//    private ShooterSubclass shooter;
//    private TransferSubclass transfer;
//    private ServoSubclass servo;
//
//    private ElapsedTime servoTimer = new ElapsedTime();
//
//    private final PoseFactory p = PoseFactory.degrees();
//
//    // Define your starting position cleanly using the factory: p.of(x, y, heading)
//    private final Pose startPose = p.of(130, 32, 180);
//
//    private boolean weaverDriving = false;
//
//    private final PolygonZone hiveSection1 = new PolygonZone(new Point(95,50), new Point(95,90),5);
//    private final PolygonZone hiveSection2 = new PolygonZone(new Point(47,50), new Point(47,90),5);
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
//        servoTimer.reset();
//    }
//
//    @Override
//    public void loop() {
//
//        if (gamepad1.dpad_up) {
//            follower.setPose(startPose);
//        }
//        if (gamepad1.dpad_down) {
//            PathResult result = Weaver.builder()
//                    .start(new Point(follower.pose().x(), follower.pose().y(), follower.pose().heading()))
//                    .end(new Point(startPose.x(), startPose.y(), startPose.heading()))
//                    .addObstacle(hiveSection1)
//                    .addObstacle(hiveSection2)
//                    .build();
//            follower.follow(PedroPathingConverter.toPath(result));
//            weaverDriving = true;
//        }
//
//        if (weaverDriving) {
//            if (!follower.isBusy() || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
//                weaverDriving = false;
//            }
//        }
//        else {
//            follower.manual(
//                    -gamepad1.left_stick_y,
//                    -gamepad1.left_stick_x,
//                    -gamepad1.right_stick_x
//            );
//        }
//
//        follower.update();
//
//// Intake + Transfer
//        if (gamepad1.left_trigger > 0.05) {
//            transfer.setTransferPower(gamepad1.left_trigger);
//        } else if (gamepad1.left_bumper) {
//            transfer.transferReverse();
//        } else {
//            transfer.transferOff();
//        }
//
//// Shooter
//        if (gamepad1.right_trigger > 0.05) {
//            shooter.shooterOn();
//            if (servoTimer.seconds() > 3) {
//                servo.servoShoot();
//            }
//        } else {
//            shooter.shooterOff();
//            servo.servoBlock();
//            servoTimer.reset();
//        }
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
//
//        telemetry.update();
//
//    }
//}