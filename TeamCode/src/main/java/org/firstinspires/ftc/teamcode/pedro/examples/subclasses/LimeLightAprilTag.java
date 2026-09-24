package org.firstinspires.ftc.teamcode.pedro.examples.subclasses;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LimeLightAprilTag {

    public enum Cluster {
        RED_OPPOSITE(30, 31, 32, 33),
        RED_AUDIENCE(34, 35, 36, 37),
        BLUE_AUDIENCE(38, 39, 40, 41),
        BLUE_OPPOSITE(42, 43, 44, 45);

        private final Set<Integer> tagIds = new HashSet<>();

        Cluster(Integer... ids) {
            for (int id : ids) {
                tagIds.add(id);
            }
        }

        public boolean contains(int id) {
            return tagIds.contains(id);
        }
    }

    private final Limelight3A limelight;
    private final Cluster cluster;

    private double centerX;
    private double centerY;
    private int visibleTags;

    public LimeLightAprilTag(Limelight3A limelight, Cluster cluster) {
        this.limelight = limelight;
        this.cluster = cluster;
    }

    public void update() {

        centerX = 0;
        centerY = 0;
        visibleTags = 0;

        LLResult result = limelight.getLatestResult();

        if (result == null || !result.isValid()) {

            return;
        }

        List<LLResultTypes.FiducialResult> detections =
                result.getFiducialResults();

        if (detections == null) {
            return;
        }

        double totalX = 0;
        double totalY = 0;

        for (LLResultTypes.FiducialResult detection : detections) {

            int id = detection.getFiducialId();

            if (!cluster.contains(id)) {
                continue;
            }

            totalX += detection.getTargetXDegrees();
            totalY += detection.getTargetYDegrees();

            visibleTags++;
        }

        if (visibleTags > 0) {
            centerX = totalX / visibleTags;
            centerY = totalY / visibleTags;
        }
    }

    public boolean hasCluster() {
        return visibleTags > 0;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public int getVisibleTags() {
        return visibleTags;
    }

    public Cluster getCluster() {
        return cluster;
    }
}