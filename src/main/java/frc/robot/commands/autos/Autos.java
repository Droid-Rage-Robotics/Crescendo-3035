package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.SetIntakeAndShooter;
import frc.robot.commands.ShootPreload;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;

public final class Autos {
    public static Command shootPLusPark(SwerveDrive drive, Intake intake, Shooter shooter, double wait) {
        return new SequentialCommandGroup(            
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.SPEAKER_SHOOT),
            new WaitCommand(1),
            new SetIntakeAndShooter(intake, Intake.Value.HOLD, shooter, Shooter.ShooterSpeeds.SPEAKER_SHOOT),
            new WaitCommand(wait),
            PathPlannerFollow.create(drive, "ForwardTest")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }
    public static Command shoot(Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.SPEAKER_SHOOT),
            new WaitCommand(1),
            new SetIntakeAndShooter(intake, Intake.Value.HOLD, shooter, Shooter.ShooterSpeeds.SPEAKER_SHOOT)
        );
    }


    public static Command onePlusF1PlusParkRed(SwerveDrive drive, Intake intake, Shooter shooter) {//Top Red/Bottom Blue
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.SPEAKER_SHOOT),
            new WaitCommand(1),
            new SetIntakeAndShooter(intake, Intake.Value.INTAKE_GROUND, shooter, Shooter.ShooterSpeeds.SPEAKER_SHOOT),
            PathPlannerFollow.create(drive, "L1+F3+ParkRed")
                .setMaxVelocity(1)
                .setAcceleration(3)
                .build()
        );
    }

    

    public static Command onePlusF1PlusParkBlue(SwerveDrive drive, Intake intake, Shooter shooter) {//Top Red/Bottom Blue
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.SPEAKER_SHOOT),
            new WaitCommand(.3),
            new SetIntakeAndShooter(intake, Intake.Value.AUTO_INTAKE_GROUND, shooter, Shooter.ShooterSpeeds.SPEAKER_SHOOT),
            new WaitCommand(.8),
            PathPlannerFollow.create(drive, "R1+F1+ParkBlue")
                .setMaxVelocity(1.5)
                .setAcceleration(3)
                .build()
        );
    }
    
    public static Command onePlusF1ParkBlue(SwerveDrive drive, Intake intake, Shooter shooter) {//Top Red/Bottom Blue
        return new SequentialCommandGroup(
            new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.SPEAKER_SHOOT),
            new WaitCommand(.3),
            new SetIntakeAndShooter(intake, Intake.Value.AUTO_INTAKE_GROUND, shooter, Shooter.ShooterSpeeds.SPEAKER_SHOOT),
            new WaitCommand(.8),
            PathPlannerFollow.create(drive, "R1+F1Blue")
                .setMaxVelocity(1.5)
                .setAcceleration(3)
                .build()
        );
    }

    // public static Command test(SwerveDrive drive, Intake intake, Shooter shooter) {//Top Red/Bottom Blue
    //     return new SequentialCommandGroup(
    //         new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.SPEAKER_SHOOT),
    //         new WaitCommand(.3),
    //         new SetIntakeAndShooter(intake, Intake.Value.AUTO_INTAKE_GROUND, shooter, Shooter.ShooterSpeeds.SPEAKER_SHOOT),
    //         new WaitCommand(.6),
    //         PathPlannerPathFollow.create(drive, "rSidetoF1")
    //             .setMaxVelocity(1.5)
    //             .setAcceleration(3)
    //             .build()
    //     );
    // }
    // R1+F1P+ParkBlue
    private Autos () {}
}