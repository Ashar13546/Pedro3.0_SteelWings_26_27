package org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class ShooterTest1 {
    // Spiking issue fixed: feedforward voltage scaling and absolute velocity matching
    private DcMotorEx shooterMotor;   // config name: "shooter"
    private DcMotorEx shooterMotor2;  // config name: "shooter2"
    private VoltageSensor battery;

    // Feedforward: power per RPM at NOMINAL_VOLTAGE (1/6000 = 0.000167)
    public static double kF = 0.000135; //0.000167
    // Proportional: power per RPM of error. Keep small for flywheels!
    public static double kP = 0.002; //0.00015, 0.001
    public static double kI = 0.00001;
    public static double kD = 0.0;

    public static double targetRPM = 4500;
    public static double tolerance = 50;
    public static double maxRPM = 5000;

    // 0..1, lower = smoother but slower to react
    public static double filterAlpha = 0.3;
    // Max power change per second (soft start)
    public static double rampPerSec = 2.5;

    public static final double ticksPerRev = 28.0;
    public static final double NOMINAL_VOLTAGE = 14.0;

    private boolean on = false;
    private double activeTarget = 0;
    private double filteredRPM = 0;
    private double integral = 0;
    private double lastError = 0;
    private double lastPower = 0;
    private long lastTimeNs = 0;

    public void init(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        battery = hardwareMap.voltageSensor.iterator().next();

        // Flip these if one fights the other
        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMotor2.setDirection(DcMotorSimple.Direction.FORWARD);

        for (DcMotorEx m : new DcMotorEx[]{shooterMotor, shooterMotor2}) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        lastTimeNs = System.nanoTime();
        filteredRPM = 0;
    }

    public void shooterOn() {
        shooterOn(targetRPM);
    }

    public void shooterOn(double customRPM) {
        activeTarget = Math.min(customRPM, maxRPM);
        if (!on) {
            // Reset ramp memory to prevent an explosive start
            lastPower = 0;
            integral = 0;
            lastError = 0;
        }
        on = true;
    }

    public void shooterOff() {
        on = false;
        activeTarget = 0;
        integral = 0;
        lastPower = 0;
        shooterMotor.setPower(0);
        shooterMotor2.setPower(0);
    }

    /** MUST be called every loop (in your OpMode loop / autonomous update). */
    public void update() {
        long now = System.nanoTime();
        double dt = Math.max((now - lastTimeNs) / 1e9, 1e-3);
        lastTimeNs = now;

        double rawRPM = getRawRPM();
        filteredRPM = filterAlpha * rawRPM + (1 - filterAlpha) * filteredRPM;

        if (!on) {
            shooterMotor.setPower(0);
            shooterMotor2.setPower(0);
            lastPower = 0;
            return;
        }

        double error = activeTarget - filteredRPM;
        integral += error * dt;
        integral = Math.max(-1000, Math.min(1000, integral));
        double derivative = (error - lastError) / dt;
        lastError = error;

        // 1. Calculate base feedforward
        double ffPower = kF * activeTarget;

        // 2. ONLY apply battery compensation to the Feedforward block
        double volts = Math.max(battery.getVoltage(), 6.0);
        ffPower *= (NOMINAL_VOLTAGE / volts);

        // 3. Combine Feedforward and PID feedback terms
        double power = ffPower + (kP * error) + (kI * integral) + (kD * derivative);

        power = Math.max(0, Math.min(1.0, power));

        // 4. Soft-start ramp calculation
        double maxStep = rampPerSec * dt;
        if (power > lastPower + maxStep) {
            power = lastPower + maxStep;
        }
        lastPower = power;

        shooterMotor.setPower(power);
        shooterMotor2.setPower(power);
    }

    public boolean isAtVelocity() {
        return isAtVelocity(targetRPM);
    }

    public boolean isAtVelocity(double customRPM) {
        return on && Math.abs(filteredRPM - customRPM) <= tolerance;
    }

    private double getRawRPM() {
        // Math.abs handles any directional code inversion without killing your calculation
        double avgTps = (Math.abs(shooterMotor.getVelocity()) + Math.abs(shooterMotor2.getVelocity())) / 2.0;
        return (avgTps / ticksPerRev) * 60.0;
    }

    public double getRPM() {
        return filteredRPM;
    }

    public double getRPM1() {
        return (Math.abs(shooterMotor.getVelocity()) / ticksPerRev) * 60.0;
    }

    public double getRPM2() {
        return (Math.abs(shooterMotor2.getVelocity()) / ticksPerRev) * 60.0;
    }

    public double getPower() {
        return lastPower;
    }
}
