package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.InfoTracker.CycleTracker;

public class ShooterCommands {
    public SequentialCommandGroup setIntakeAndShooter (Intake intake, Intake.Value intakePos, Shooter shooter, Shooter.ShooterSpeeds speed){
        return new SequentialCommandGroup(
            shooter.runOnce(()->shooter.setTargetVelocity(speed)),
            new WaitCommand(.8),//.6
            intake.setPositionCommand(intakePos));
    }
    public SequentialCommandGroup autoShoot (Intake intake, Shooter shooter){
        return new SequentialCommandGroup(
                // shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.AUTO_SPEAKER_SHOOT)),
                // new WaitCommand(.5),//.6
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
            );
    }
    public SequentialCommandGroup teleopShoot (Intake intake, Shooter shooter, CycleTracker cycleTracker, AmpMech ampMech){
        return new SequentialCommandGroup(
            // ampMech.setPositionCommand(AmpMech.Value.SHOOT),
            // shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
            // new WaitCommand(.9),
            intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
            new InstantCommand(()-> cycleTracker.trackCycle(CycleTracker.ScorePos.SPEAKER))
        );
    }
    public SequentialCommandGroup shootPreload (Intake intake, Intake.Value intakePos, Shooter shooter, Shooter.ShooterSpeeds speed){
        return new SequentialCommandGroup(
            shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
            new WaitCommand(1.1),//.8
            intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
            new WaitCommand(1.35),
            setIntakeAndShooter(intake, Intake.Value.INTAKE_GROUND, 
                shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT)
        );

    }      
}
