package org.firstinspires.ftc.teamcode.pedro.examples.leap.autonomous;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.leap.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.leap.subclasses.ShootSequence;
import org.firstinspires.ftc.teamcode.pedro.examples.leap.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.leap.subclasses.TransferSubclass;

@Autonomous(name = "SST Back Wall Left Park", group = "Autonomous")
public class ShootSequenceTest extends LinearOpMode {
    private ShooterSubclass shooter = new ShooterSubclass();
    private TransferSubclass transfer = new TransferSubclass();
    private ServoSubclass servo = new ServoSubclass();
    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(132, 118, 90);
    private final Pose path1Start = poseFactory.of(94.33, 131, 270);
    private final Pose path1 = poseFactory.of(83, 120, 275-180);
    private final Pose point2 = poseFactory.of(125, 95, 0);
    private final Pose point3 = poseFactory.of(83, 120, 275-180);
    private final Pose point4 = poseFactory.of(125, 25, 180); //x 130
    private final Pose point4Control1 = poseFactory.of(45, 15, 0);



    public Command autoRoutine() {
        return sequential(
                // 0. (initialize ramp servo to up position)
                instant(() -> servo.servoUp()),
                // 1. drive to shooting position
                follow(follower, shootPos1()),

                // 2. shoot sequence
                ShootSequence.rapidFire4(3150, shooter, transfer, servo),
                instant(() -> servo.servoDown()),

                // 4. drive to flower & intake more pollen for 1.5s then stop
                parallel(
                        follow(follower, intakePollenPos()),
                        sequential(
                                instant(() -> {
                                    transfer.transferOn();
                                }),
                                waitMs(1500),
                                instant(() -> {
                                    transfer.transferOff();
                                })
                        )
                ),

                // 5. drive to shooting pos (again)
                follow(follower, shootPos2()),

                // 6. shoot sequence & raise ramp servo
                instant(() -> servo.servoUp()),
                ShootSequence.rapidFire4(3150, shooter, transfer, servo),

                // 8. drive to park
                follow(follower, park())

        );
    }


    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);

        shooter = new ShooterSubclass();
        transfer = new TransferSubclass();
        servo = new ServoSubclass();

        shooter.init(hardwareMap);
        transfer.init(hardwareMap);
        servo.init(hardwareMap);

        follower.setPose(path1Start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            shooter.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path shootPos1() {
        return line(path1Start, path1).linear(path1Start, path1);
    }

    public Path intakePollenPos() {
        return line(path1, point2).linear(path1, point2);
    }

    public Path shootPos2() {
        return line(point2, point3).linear(point2, point3);
    }

    public Path park() {
        return curve(point3, point4Control1, point4).linear(point3, point4);
    }
}