//package org.firstinspires.ftc.teamcode.pedro.examples;
//
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//
//import java.util.List;
//
//@TeleOp(name = "LL Testing", group = "Examples")
//public class LLTesting extends LinearOpMode {
//
//    // Define your AprilTagProcessor and VisionPortal variables if needed,
//    // or assume aprilTag is initialized globally/externally.
//    private AprilTagProcessor aprilTag;
//
//    @Override
//    public void runOpMode() {
//        // TODO: Initialize your VisionPortal and AprilTagProcessor here.
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//            // Make sure aprilTag is not null before getting detections
//            if (aprilTag != null) {
//                List<AprilTagDetection> currentDetections = aprilTag.getDetections();
//                telemetry.addData("# AprilTags Detected", currentDetections.size());
//
//                // Step through the list of detections and display info for each one.
//                for (AprilTagDetection detection : currentDetections) {
//                    if (detection.metadata != null) {
//                        telemetry.addData(")==( Tag ID", "%d (%s)", detection.id, detection.metadata.name);
//                        telemetry.addData("Translation", "X = %.2f, Y = %.2f, Z = %.2f",
//                                detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z);
//                    } else {
//                        telemetry.addData(")==( Tag ID", "%d (Unknown)", detection.id);
//                    }
//                }
//            } else {
//                telemetry.addData("Error", "AprilTag Processor is null");
//            }
//
//            telemetry.update();
//            sleep(20);
//        }
//    }
//}
