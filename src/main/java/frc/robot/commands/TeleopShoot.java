package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.InfoTracker.CycleTracker;

public class TeleopShoot extends SequentialCommandGroup {
    public TeleopShoot (Intake intake, Shooter shooter, CycleTracker cycleTracker){
        addCommands(
            new ParallelCommandGroup(
                shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
                new InstantCommand(()-> cycleTracker.trackCycle(ShooterSpeeds.SPEAKER_SHOOT))
                )
            );
    }   
}
