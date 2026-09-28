package org.firstinspires.ftc.teamcode.pedro.examples.other;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp (name = "Diagonostic Test", group = "TeleOp")
public class DiagnosticTest extends LinearOpMode {

    private Limelight3A limelight;

    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(2);
        limelight.start();

        waitForStart();

        while (opModeIsActive()) {

            LLResult result = limelight.getLatestResult();

            telemetry.addData("Result Null", result == null);

            if (result != null) {
                telemetry.addData("Result Valid", result.isValid());
            }

            List<LLResultTypes.FiducialResult> detections = result.getFiducialResults();

            telemetry.addData("Fiducials", detections == null ? "NULL" : detections.size());

            if (detections != null) {
                for (LLResultTypes.FiducialResult detection :detections) {
                    telemetry.addData("Tag", "ID %d X %.2f Y %.2f", detection.getFiducialId(), detection.getTargetXDegrees(), detection.getTargetYDegrees());
                }
            }
            telemetry.update();
        }
        limelight.stop();
    }
}