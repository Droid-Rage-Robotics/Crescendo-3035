package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.shooter.SetIntakeAndShooter;
import frc.robot.commands.shooter.ShootPreload;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;

public final class BasicAutos {
    public static Command shootPLusPark(SwerveDrive drive, Intake intake, Shooter shooter, double wait) {
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(wait),
            PathPlannerFollow.create(drive, "ForwardTest")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }
    public static Command shootPLusTurnParkHuman(SwerveDrive drive, Intake intake, Shooter shooter, double wait) {
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(wait),
            PathPlannerFollow.create(drive, "R1+F1Blue")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
            ,new WaitCommand(3),
            new SetIntakeAndShooter(intake, Intake.Value.CLIMB, shooter, Shooter.ShooterSpeeds.HOLD)
        );
    }
    // public static Command shootPLusTurnParkNonHUMAN(SwerveDrive drive, Intake intake, Shooter shooter, double wait) {
    //     return new SequentialCommandGroup(
    //         new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
    //         new WaitCommand(wait),
    //         PathPlannerFollow.create(drive, "L1+F3Red")
    //             .setMaxVelocity(1)
    //             .setAcceleration(3)
    //             .build()
    //         ,new WaitCommand(3),
    //         new SetIntakeAndShooter(intake, Intake.Value.CLIMB, shooter, Shooter.ShooterSpeeds.HOLD)
    //     );
    // }
    public static Command toCenter(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(    
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(1),
            new SetIntakeAndShooter(intake, Intake.Value.INTAKE_GROUND, shooter, Shooter.ShooterSpeeds.STOP),
            new WaitCommand(1),
            PathPlannerFollow.create(drive, "toCenter")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }
    public static Command out(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(4.5),
            PathPlannerFollow.create(drive, "oldOut")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }

    public static Command right(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(1),
            PathPlannerFollow.create(drive, "right")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }
    public static Command left(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(1),
            PathPlannerFollow.create(drive, "left")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }
        
    public static Command shoot(Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(1),
            new SetIntakeAndShooter(intake, Intake.Value.HOLD, shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT)
        );
    }

    public static Command test(SwerveDrive drive){
        return new SequentialCommandGroup(
            // PathPlannerPathFollow.create(drive, "one")
            //     .build(),
            //     PathPlannerPathFollow.create(drive, "two")
            //     .build(),
            //     PathPlannerPathFollow.create(drive, "three")
            //     .build(),
            //     PathPlannerPathFollow.create(drive, "four")
            //     .build(),
            //     PathPlannerPathFollow.create(drive, "five")
            //     .build(),
            //     PathPlannerPathFollow.create(drive, "six")
            //     .build()
            PathPlannerFollow.create(drive, "one")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build(),
                PathPlannerFollow.create(drive, "two")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build(),PathPlannerFollow.create(drive, "three")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build(),
                PathPlannerFollow.create(drive, "four")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }

    private BasicAutos () {}
}