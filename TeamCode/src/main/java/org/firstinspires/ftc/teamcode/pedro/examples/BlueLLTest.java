package org.firstinspires.ftc.teamcode.pedro.examples;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.pedro.examples.subclasses.LimeLightAprilTag;

@TeleOp(name = "Blue LimeLightTest", group = "TeleOp")
public class BlueLLTest extends LinearOpMode {

    private Limelight3A limelight;
    private LimeLightAprilTag audienceDetector;
    private LimeLightAprilTag oppositeDetector;

    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);

        audienceDetector = new LimeLightAprilTag(limelight, LimeLightAprilTag.Cluster.BLUE_AUDIENCE);
        oppositeDetector = new LimeLightAprilTag(limelight, LimeLightAprilTag.Cluster.BLUE_OPPOSITE);

        limelight.pipelineSwitch(2);
        limelight.start();

        waitForStart();

        while (opModeIsActive()) {
            audienceDetector.update();
            oppositeDetector.update();

            telemetry.addData("Audience", audienceDetector.hasCluster());
            telemetry.addData("Audience X", audienceDetector.getCenterX());

            telemetry.addData("Opposite", oppositeDetector.hasCluster());
            telemetry.addData("Opposite X", oppositeDetector.getCenterX());

            telemetry.update();
        }

        limelight.stop();
    }
}