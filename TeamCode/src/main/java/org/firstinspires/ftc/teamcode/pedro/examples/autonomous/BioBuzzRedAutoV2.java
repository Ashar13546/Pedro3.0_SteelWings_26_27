package org.firstinspires.ftc.teamcode.pedro.examples.autonomous;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "RedAutoV2", group = "Autonomous")
public class BioBuzzRedAutoV2 extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(12, 72, 0);
    private final Pose point11Start = poseFactory.of(12, 72, 0);
    private final Pose point11 = poseFactory.of(124, 116.5, 90);
    private final Pose point11Control1 = poseFactory.of(24, 124, 0);
    private final Pose point22 = poseFactory.of(124, 129, 90);
    private final Pose point33 = poseFactory.of(85.5, 31, 132);
    private final Pose point33Control1 = poseFactory.of(35, 123.5, 0);
    private final Pose point4 = poseFactory.of(12, 112, 0);
    private final Pose point4Control1 = poseFactory.of(9.5, 17, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path11()),
                follow(follower, path22()),
                follow(follower, path33()),
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

    public Path path11() {
        return curve(point11Start, point11Control1, point11).linear(point11Start, point11);
    }

    public Path path22() {
        return line(point11, point22).linear(point11, point22);
    }

    public Path path33() {
        return curve(point22, point33Control1, point33).linear(point22, point33);
    }

    public Path path4() {
        return curve(point33, point4Control1, point4).linear(point33, point4);
    }
}