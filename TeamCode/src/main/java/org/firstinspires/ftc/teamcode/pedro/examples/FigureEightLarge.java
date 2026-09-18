package org.firstinspires.ftc.teamcode.pedro.examples;

import static com.pedropathing.api.Paths.*;

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

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "EightLarge", group = "Autonomous")
public class FigureEightLarge extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(72, 72, 90);
    private final Pose path1 = poseFactory.of(126, 72, -64.6226);
    private final Pose path1Control1 = poseFactory.of(85, 100, 0);
    private final Pose path1Control2 = poseFactory.of(113, 100, 0);
    private final Pose point2 = poseFactory.of(72, 72, 115.3774);
    private final Pose point2Control1 = poseFactory.of(113, 44, 0);
    private final Pose point2Control2 = poseFactory.of(85, 44, 0);
    private final Pose point3 = poseFactory.of(18, 72, -116.992);
    private final Pose point3Control1 = poseFactory.of(58, 100, 0);
    private final Pose point3Control2 = poseFactory.of(32, 100, 0);
    private final Pose point4 = poseFactory.of(72, 72, 63.008);
    private final Pose point4Control1 = poseFactory.of(32, 44, 0);
    private final Pose point4Control2 = poseFactory.of(58, 44, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
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
        return curve(start, path1Control1, path1Control2, path1).tangent();
    }

    public Path path2() {
        return curve(path1, point2Control1, point2Control2, point2).tangent();
    }

    public Path path3() {
        return curve(point2, point3Control1, point3Control2, point3).tangent();
    }

    public Path path4() {
        return curve(point3, point4Control1, point4Control2, point4).tangent();
    }
}