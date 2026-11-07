package org.firstinspires.ftc.teamcode.pedro.examples.frog.autonomous;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.paths.interpolator.Interpolator;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ServoSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ShootSequence;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.ShooterSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses.TransferSubclass;

@Autonomous(name = "FlowerAudienceShotLP", group = "Autonomous")
public class FlowerAudienceShotLP extends LinearOpMode {

    private ShooterSubclass shooter = new ShooterSubclass();
    private TransferSubclass transfer = new TransferSubclass();
    private ServoSubclass servo = new ServoSubclass();

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(80, 10.5, 90);
    private final Pose path1 = poseFactory.of(81.5452, 19.5471, -95.0821);
    private final Pose path1Segment1Start = poseFactory.of(81.5452, 19.5471, 270);
    private final Pose path1Segment1End = poseFactory.of(81.5452, 19.5471, 270);
    private final Pose path1Segment2Target = poseFactory.of(84, 47.15, 0);
    private final Pose point2Start = poseFactory.of(81.5452, 19.5471, -95);
    private final Pose point2 = poseFactory.of(125, 20, 180);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                waitMs(15000),
                follow(follower, path1()),
                ShootSequence.rapidFire4(3150,shooter,transfer,servo),
                follow(follower, path2())
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

        follower.setPose(start);
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

    public Path path1() {
        return Paths.line(start, path1).heading(Interpolator.piecewise().until(0.3, Interpolator.linear(path1Segment1Start, path1Segment1End)).until(1, Interpolator.facingPoint(path1Segment2Target).reverse()));
    }

    public Path path2() {
        return Paths.line(point2Start, point2).linear(point2Start, point2);
    }
}
