//package org.firstinspires.ftc.teamcode.pedro.examples.autonomous;
//
//import static com.pedropathing.api.Paths.*;
//
//import com.pedropathing.api.PoseFactory;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.math.Pose;
//import com.pedropathing.paths.Path;
//import com.pedropathing.ivy.Command;
//import com.pedropathing.ivy.Scheduler;
//import static com.pedropathing.ivy.Scheduler.schedule;
//import static com.pedropathing.ivy.commands.Commands.*;
//import static com.pedropathing.ivy.groups.Groups.parallel;
//import static com.pedropathing.ivy.groups.Groups.sequential;
//import static com.pedropathing.ivy.pedro.PedroCommands.follow;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.teamcode.pedro.Constants;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.IntakeSubclass;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ServoSubclass;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;
//import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.TransferSubclass;
//
//@Autonomous(name = "BlueAuto With Shoot", group = "Autonomous")
//public class BlueAutoWShoot extends LinearOpMode {
//
//    private IntakeSubclass intake = new IntakeSubclass();
//    private ShooterSubclass shooter = new ShooterSubclass();
//    private TransferSubclass transfer = new TransferSubclass();
//    private ServoSubclass servo = new ServoSubclass();
//    private Follower follower;
//
//    private final PoseFactory poseFactory = PoseFactory.degrees();
//
//    private final Pose start = poseFactory.of(132, 118, 90);
//    private final Pose path1Start = poseFactory.of(132, 118, 180);
//    private final Pose path1 = poseFactory.of(83, 120, 275);
//    private final Pose point2 = poseFactory.of(125, 95, 0);
//    private final Pose point3 = poseFactory.of(83, 120, 275);
//    private final Pose point4 = poseFactory.of(130, 45, 180);
//    private final Pose point4Control1 = poseFactory.of(127, 129, 0);
//    private final Command pause1500 = waitMs(1500);
//    private final Command pause3000 = waitMs(3000);
//
//    // Autonomous routine
//    public Command autoRoutine() {
//        return sequential(
//                follow(follower, shootPos1()),
//                instant(() -> shooter.shooterOn()),
//                pause3000,
//                instant(() -> servo.servoShoot()),
//                instant(() -> intake.intakeOn()),
//                instant(() -> transfer.transferOn()),
//                pause3000,
//                instant(() -> shooter.shooterOff()),
//                instant(() -> intake.intakeOff()),
//                instant(() -> transfer.transferOff()),
//                instant(() -> servo.servoBlock()),
//                pause1500,
//                parallel(
//                        follow(follower, intakePollenPos()),
//                        instant(() -> intake.intakeOn()),
//                        instant(() -> transfer.transferOn())
//                ),
//                pause1500,
//                parallel(
//                        follow(follower, shootPos2()),
//                        instant(() -> intake.intakeOff()),
//                        instant(() -> transfer.transferOff())
//                ),
//                instant(() -> shooter.shooterOn()),
//                pause3000,
//                instant(() -> servo.servoShoot()),
//                instant(() -> intake.intakeOn()),
//                instant(() -> transfer.transferOn()),
//                pause3000,
//                parallel(
//                        follow(follower, park()),
//                        instant(() -> shooter.shooterOff()),
//                        instant(() -> intake.intakeOff()),
//                        instant(() -> transfer.transferOff()),
//                        instant(() -> servo.servoBlock())
//                )
//        );
//    }
//
//    @Override
//    public void runOpMode() {
//        Scheduler.reset();
//        follower = Constants.create(hardwareMap);
//
//        intake = new IntakeSubclass();
//        shooter = new ShooterSubclass();
//        transfer = new TransferSubclass();
//        servo = new ServoSubclass();
//
//        intake.init(hardwareMap);
//        shooter.init(hardwareMap);
//        transfer.init(hardwareMap);
//        servo.init(hardwareMap);
//
//        follower.setPose(path1Start);
//        follower.update();
//
//        waitForStart();
//        schedule(autoRoutine());
//
//        while (opModeIsActive()) {
//            follower.update();
//            Scheduler.execute();
//
//            telemetry.addData("x", follower.pose().x());
//            telemetry.addData("y", follower.pose().y());
//            telemetry.addData("heading", follower.pose().heading());
//
//            if (follower.currentPath() != null) {
//                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
//                telemetry.addData("Path number", follower.pathIndex());
//            }
//
//            telemetry.update();
//        }
//    }
//
//    public Path shootPos1() {
//        return line(path1Start, path1).linear(path1Start, path1);
//    }
//
//    public Path intakePollenPos() {
//        return line(path1, point2).linear(path1, point2);
//    }
//
//    public Path shootPos2() {
//        return line(point2, point3).linear(point2, point3);
//    }
//
//    public Path park() {
//        return curve(point3, point4Control1, point4).linear(point3, point4);
//    }
//}