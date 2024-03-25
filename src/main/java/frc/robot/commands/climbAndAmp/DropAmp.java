package frc.robot.commands.climbAndAmp;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.utility.InfoTracker.CycleTracker;

public class DropAmp extends SequentialCommandGroup {
    public DropAmp (AmpMech ampMech, CycleTracker cycleTracker){
        addCommands(
            ampMech.setPositionCommand(AmpMech.Value.AMP), //true
            new InstantCommand(()->cycleTracker.trackCycle(CycleTracker.ScorePos.AMP)),
            new WaitCommand(.5),
            ampMech.setPositionCommand(AmpMech.Value.START)
        );
    }
}