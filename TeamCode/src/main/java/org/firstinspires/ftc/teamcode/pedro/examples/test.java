//package org.firstinspires.ftc.teamcode.pedro.examples;
//
//import static com.pedropathing.api.Paths.*;
//import com.pedropathing.api.PoseFactory;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.math.Pose;
//import com.pedropathing.paths.Path;
//import com.pedropathing.ivy.Command;
//import com.pedropathing.ivy.Scheduler;
//import static com.pedropathing.ivy.Scheduler.schedule;
//import static com.pedropathing.ivy.commands.Commands.*;
//import static com.pedropathing.ivy.groups.Groups.sequential;
//import static com.pedropathing.ivy.pedro.PedroCommands.follow;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import org.firstinspires.ftc.teamcode.pedro.Constants;
//
//@Autonomous(name = "Example Auto", group = "Autonomous")
//public class ExampleAuto extends LinearOpMode {
//
//    // 1. Declare the Follower variable
//    private Follower follower;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//        // 2. Initialize the Ivy Scheduler and your Follower BEFORE using them
//        Scheduler.reset();
//        follower = new Follower(hardwareMap); // Instantiating the object fixes the null pointer!
//
//        // 3. Set your starting pose safely
//        Pose startPose = new Pose(0, 0, 0); // Replace with your actual starting coordinates
//        follower.setPose(startPose);
//
//        // Build your paths / Ivy commands here...
//        // e.g., Command autoRoutine = sequential(follow(follower, yourPath));
//
//        waitForStart();
//
//        if (isStopRequested()) return;
//
//        // Run your routine
//        // schedule(autoRoutine);
//
//        while (opModeIsActive() && !isStopRequested()) {
//            Scheduler.update(); // Keep the scheduler running
//        }
//    }
//}
