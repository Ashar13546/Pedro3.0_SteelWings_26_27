package org.firstinspires.ftc.teamcode.pedro.examples.autonomous;

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
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.IntakeSubclass;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.ShooterSubclass;

@Autonomous(name = "BlueTest", group = "Autonomous")
public class BlueTest extends LinearOpMode {

    private IntakeSubclass intake = new IntakeSubclass();

    private ShooterSubclass shooter = new ShooterSubclass();
    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(132, 32, 90);
    private final Pose point11Start = poseFactory.of(132, 32, 180);
    private final Pose point11 = poseFactory.of(20, 27.5, 270);
    private final Pose point22 = poseFactory.of(20, 15, 270);
    private final Pose point33 = poseFactory.of(58.5, 113, 312);
    private final Pose point33Control1 = poseFactory.of(109, 20.5, 0);

    // Autonomous routine


    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
        intake.init(hardwareMap);
        shooter.init(hardwareMap);

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

    public Command autoRoutine() {
        return sequential(
                follow(follower, path11()),
                instant(() -> intake.intakeOn()),
                follow(follower, path22()),
                instant(() -> intake.intakeOff()),
                follow(follower, path33()),
                instant(() -> shooter.shooterOn())
        );
    }

    public Path path11() {
        return line(point11Start, point11).linear(point11Start, point11);
    }

    public Path path22() {
        return line(point11, point22).linear(point11, point22);
    }

    public Path path33() {
        return curve(point22, point33Control1, point33).linear(point22, point33);
    }
}