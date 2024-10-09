package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ShooterCommands;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;

public final class Autos {
    public static Command onePlusThree(SwerveDrive drive, Intake intake, Shooter shooter, AmpMech ampMech) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            // new WaitCommand(.3),
            PathPlannerFollow.create(drive, "1+3")
                .setMaxVelocity(2.3)
                .setAcceleration(2)
                .build()
        );
    }
    public static Command onePlusTwo(SwerveDrive drive, Intake intake, Shooter shooter, AmpMech ampMech) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            PathPlannerFollow.create(drive, "1+2")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build()
        );
    }    
    public static Command amp(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            // shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
            // new WaitCommand(2),//.8
            // intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
            // new WaitCommand(4.7),
            //^^ Freiendswood Playoff

            // new ShootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            

            shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
                new WaitCommand(1.1),//.8
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
                new WaitCommand(1.3),
                ShooterCommands.shootPreload(intake, Intake.Value.HOLD, 
                    shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT),
                
            PathPlannerFollow.create(drive, "amp")
                .setMaxVelocity(3.5)
                .setAcceleration(3.5)
                .build()
        );
    }
    public static Command centerAppreciate(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
                new WaitCommand(1.1),//.8
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
                new WaitCommand(1.3),
                ShooterCommands.setIntakeAndShooter(intake, Intake.Value.HOLD, 
                    shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT),
                
            new WaitCommand(4),
            PathPlannerFollow.create(drive, "centerAp")
                .setMaxVelocity(3.5)
                .setAcceleration(3.5)
                .build()
        );
    }
    public static Command shootOut(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
                new WaitCommand(1.1),//.8
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
                new WaitCommand(1.3),
                ShooterCommands.setIntakeAndShooter(intake, Intake.Value.HOLD, 
                    shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT),
                
            new WaitCommand(4),
            PathPlannerFollow.create(drive, "shootOut")
                .setMaxVelocity(3.5)
                .setAcceleration(3.5)
                .build()
        );
    }
    public static Command onePlusFullSeperate(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            PathPlannerFollow.create(drive, "F1+F2")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build(),
            PathPlannerFollow.create(drive, "1+F1")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build(),
            // intake.setPositionCommand(Intake.Value.AUTO_INTAKE_GROUND),
            
            
            // PathPlannerFollow.create(drive, "F2+F3")
            //     .setMaxVelocity(3)
            //     .setAcceleration(3)
            //     .build()

            PathPlannerFollow.create(drive, "F2+F3Pick")//F2+F3Pick
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build()
            //push code                                                 
                
        );
    }
    public static Command onePlusMiddle(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.7),
            PathPlannerFollow.create(drive, "shootMiddle")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build()
            // PathPlannerFollow.create(drive, "F1+F2")
            //     .setMaxVelocity(3)
            //     .setAcceleration(3)
            //     .build(),
            // PathPlannerFollow.create(drive, "ForwardTest")
            //     .setMaxVelocity(3)
            //     .setAcceleration(3)
            //     .build()
        );
    }
    public static Command onePlusHUuman(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.7),
            PathPlannerFollow.create(drive, "shootHuman")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build()
        );
    }
    public static Command onePlusHumanAndMidle(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.7),
            PathPlannerFollow.create(drive, "shootHumanPlusMid")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build()
        );
    }
    public static Command onePlusMiddleAndHuman(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.7),
            PathPlannerFollow.create(drive, "shootMidPlusHuman")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build()
        );
    }
    public static Command onePlusTwoSeperate(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            PathPlannerFollow.create(drive, "1+F1")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build(),
            // intake.setPositionCommand(Intake.Value.AUTO_INTAKE_GROUND),
            PathPlannerFollow.create(drive, "F1+F2")
                .setMaxVelocity(3)
                .setAcceleration(3)
                .build(),
            PathPlannerFollow.create(drive, "ForwardTest")
                .setMaxVelocity(6)
                .setAcceleration(6)
                    .build()

            // PathPlannerFollow.create(drive, "F2+F3Pick")//F2+F3Pick
            //     .setMaxVelocity(3)
            //     .setAcceleration(3)
            //     .build()
            //push code                                                 
                
        );
    }
    public static Command onePlusF1PlusParkBlue(SwerveDrive drive, Intake intake, Shooter shooter) {//Top Red/Bottom Blue
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.3),
            ShooterCommands.setIntakeAndShooter(intake, Intake.Value.HOLD, shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            // new SetIntakeAndShooter(intake, Intake.Value.AUTO_INTAKE_GROUND, shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.8),
            PathPlannerFollow.create(drive, "R1+F1+ParkBlue")
                .setMaxVelocity(1.5)
                .setAcceleration(3)
                .build()
        );
    }
    
    public static Command onePlusF1ParkBlue(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.3),
            ShooterCommands.setIntakeAndShooter(intake, Intake.Value.AUTO_INTAKE_GROUND, shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.8),
            PathPlannerFollow.create(drive, "R1+F1Blue")
                .setMaxVelocity(1.5)
                .setAcceleration(3)
                .build()
        );
    }

    public static Command onePlusF1ParkRed(SwerveDrive drive, Intake intake, Shooter shooter) {
        return new SequentialCommandGroup(
            ShooterCommands.shootPreload(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.3),
            ShooterCommands.setIntakeAndShooter(intake, Intake.Value.AUTO_INTAKE_GROUND, shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT),
            new WaitCommand(.8),
            PathPlannerFollow.create(drive, "L1+F3Red")
                .setMaxVelocity(1.5)
                .setAcceleration(3)
                .build()
        );
    }

    public static Command test(SwerveDrive drive, Intake intake, Shooter shooter, AmpMech ampMech) {
        return new SequentialCommandGroup(
            PathPlannerFollow.create(drive, "test")
                .build()
        );
    }
    private Autos () {}
}