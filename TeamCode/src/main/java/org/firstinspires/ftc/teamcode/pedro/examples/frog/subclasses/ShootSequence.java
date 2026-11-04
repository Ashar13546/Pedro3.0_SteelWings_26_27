package org.firstinspires.ftc.teamcode.pedro.examples.frog.subclasses;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

public class ShootSequence {
    // Change these 2
    private static final double pulseOnTime = 200;

    private static final double pulseOffTime = 200;


    public static Command rapidFire4(double targetRPM, ShooterSubclass shooter, TransferSubclass transfer, ServoSubclass servo) {
        return sequential(
                // 1. Spin up & wait
                instant(() -> shooter.shooterOn(targetRPM)),
                waitUntil(() -> shooter.isAtVelocity(targetRPM)),

                // 2. Open gate
                instant(() -> servo.servoShoot()),

                // 3. Pulse Transfer 4 times
                instant(() -> transfer.transferShoot()),
                waitMs(pulseOnTime),
                instant(() -> transfer.transferOff()),
                waitMs(pulseOffTime),

                instant(() -> transfer.transferShoot()),
                waitMs(pulseOnTime),
                instant(() -> transfer.transferOff()),
                waitMs(pulseOffTime),

                instant(() -> transfer.transferShoot()),
                waitMs(pulseOnTime),
                instant(() -> transfer.transferOff()),
                waitMs(pulseOffTime),

                instant(() -> transfer.transferShoot()),
                waitMs(pulseOnTime),
                instant(() -> transfer.transferOff()),
                waitMs(pulseOffTime),

                // 4. Turn Shooter Off & Close gate
                instant(() -> shooter.shooterOff()),
                instant(() -> servo.servoBlock())
        );
    }
}
