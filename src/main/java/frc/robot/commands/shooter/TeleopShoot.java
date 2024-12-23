package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.InfoTracker.CycleTracker;

public class TeleopShoot extends SequentialCommandGroup {
    public TeleopShoot (Intake intake, Shooter shooter, CycleTracker cycleTracker, AmpMech ampMech){
        addCommands(
            new SequentialCommandGroup(
                // ampMech.setPositionCommand(AmpMech.Value.SHOOT),
                // shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
                // new WaitCommand(.9),
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
                new InstantCommand(()-> cycleTracker.trackCycle(CycleTracker.ScorePos.SPEAKER))
                )
            );
    }   
}
