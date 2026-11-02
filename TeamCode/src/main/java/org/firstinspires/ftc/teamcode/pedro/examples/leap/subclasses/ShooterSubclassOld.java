//package org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.VoltageSensor;
//
//@Config
//public class ShooterSubclass {
//
//    private DcMotorEx shooterMotor;   // config name: "shooter"
//    private DcMotorEx shooterMotor2;  // config name: "shooter2"
//    private VoltageSensor battery;
//
//    // Feedforward: power per RPM (1/6000 = 0.000167)
//    public static double kF = 0.00016667;
//    // Proportional: power per RPM of error. Start small!
//    public static double kP = 0.0002;
//    public static double kI = 0.0002;
//    public static double kD = 0.0;
//
//    public static double targetRPM = 3300;
//    public static double tolerance = 50;
//    public static double maxRPM = 5000;
//    // 0..1, lower = smoother but slower to react
//    public static double filterAlpha = 0.3;
//    // Max power change per second (soft start)
//    public static double rampPerSec = 3.0;
//
//    public static final double ticksPerRev = 28.0;
//    public static final double NOMINAL_VOLTAGE = 14.0;
//
//    private boolean on = false;
//    private double activeTarget = 0;
//    private double filteredRPM = 0;
//    private double integral = 0;
//    private double lastError = 0;
//    private double lastPower = 0;
//    private long lastTimeNs = 0;
//
//    public void init(HardwareMap hardwareMap) {
//        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
//        shooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter2");
//        battery = hardwareMap.voltageSensor.iterator().next();
//
//        // Flip these if one fights the other
//        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        shooterMotor2.setDirection(DcMotorSimple.Direction.FORWARD);
//
//        for (DcMotorEx m : new DcMotorEx[]{shooterMotor, shooterMotor2}) {
//            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            // Encoder is still read, but we drive with raw power from our own loop
//            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        }
//        lastTimeNs = System.nanoTime();
//    }
//
//    public void shooterOn() {
//        shooterOn(targetRPM);
//    }
//
//    public void shooterOn(double customRPM) {
//        activeTarget = Math.min(customRPM, maxRPM);
//        on = true;
//    }
//
//    public void shooterOff() {
//        on = false;
//        activeTarget = 0;
//        integral = 0;
//        lastPower = 0;
//        shooterMotor.setPower(0);
//        shooterMotor2.setPower(0);
//    }
//
//    /** MUST be called every loop (in your OpMode loop / autonomous update). */
//    public void update() {
//        long now = System.nanoTime();
//        double dt = Math.max((now - lastTimeNs) / 1e9, 1e-3);
//        lastTimeNs = now;
//
//        double rawRPM = getRawRPM();
//        filteredRPM = filterAlpha * rawRPM + (1 - filterAlpha) * filteredRPM;
//
//        if (!on) return;
//
//        double error = activeTarget - filteredRPM;
//        integral += error * dt;
//        // Anti-windup clamp
//        integral = Math.max(-1000, Math.min(1000, integral));
//        double derivative = (error - lastError) / dt;
//        lastError = error;
//
//        double power = kF * activeTarget
//                + kP * error
//                + kI * integral
//                + kD * derivative;
//
//        // Battery compensation so results don't change as voltage sags
//        double volts = Math.max(battery.getVoltage(), 6.0);
//        power *= NOMINAL_VOLTAGE / volts;
//
//        power = Math.max(0, Math.min(1.0, power));
//
//        // Soft-start ramp on the way up only
//        double maxStep = rampPerSec * dt;
//        if (power > lastPower + maxStep) power = lastPower + maxStep;
//        lastPower = power;
//
//        shooterMotor.setPower(power);
//        shooterMotor2.setPower(power);
//    }
//
//    public boolean isAtVelocity() {
//        return isAtVelocity(targetRPM);
//    }
//
//    public boolean isAtVelocity(double customRPM) {
//        return on && Math.abs(filteredRPM - customRPM) <= tolerance;
//    }
//
//    private double getRawRPM() {
//        double avgTps = (shooterMotor.getVelocity() + shooterMotor2.getVelocity()) / 2.0;
//        return (avgTps / ticksPerRev) * 60.0;
//    }
//
//    // Filtered average (use this for telemetry/ready checks)
//    public double getRPM() {
//        return filteredRPM;
//    }
//
//    public double getRPM1() {
//        return (shooterMotor.getVelocity() / ticksPerRev) * 60.0;
//    }
//
//    public double getRPM2() {
//        return (shooterMotor2.getVelocity() / ticksPerRev) * 60.0;
//    }
//
//    public double getPower() {
//        return lastPower;
//    }
//
//    public static void adjustTargetRPM(double change) {
//        targetRPM = Math.max(0, Math.min(maxRPM, targetRPM + change));
//    }
//
//    public static void setTargetRPM(double rpm) {
//        targetRPM = Math.max(0, Math.min(maxRPM, rpm));
//    }
//
//}