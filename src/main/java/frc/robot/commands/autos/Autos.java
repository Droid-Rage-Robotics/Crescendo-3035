package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drive.SwerveDrive;

public final class Autos {
    
    public static Command onePlusF1PlusParkRBlue(SwerveDrive drive) {//Top Red/Bottom Blue
        return new SequentialCommandGroup(
            PathPlannerFollow.create(drive, "R1+F1+ParkBlue")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }

    

    public static Command onePlusF3PlusParkLRed(SwerveDrive drive) {//Top Red/Bottom Blue
        return new SequentialCommandGroup(
            PathPlannerFollow.create(drive, "L1+F3+ParkRed")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }
    private Autos () {}
}