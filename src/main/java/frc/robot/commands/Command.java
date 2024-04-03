package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.InfoTracker.CycleTracker;

public class Command {
    public SequentialCommandGroup ClimbAndTrap(Intake intake, Shooter shooter, AmpMech ampMech, Climb climb){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                climb.runOnce(()->climb.setTargetPosition(Climb.Position.TRAP)),
                ampMech.setPositionCommand(AmpMech.Value.START),
                intake.setPositionCommand(Intake.Value.CLIMB_DOWN)//CLIMB_HOLD
            )
            // new WaitCommand(5),
            // intake.setPositionCommand(Intake.Value.CLIMB_HOLD)//CLIMBCLIMB_HOLD

            // ,new WaitUntilCommand(()->(climb.getError())<.5),
            // new WaitUntilCommand(()->Math.abs(climb.getTargetPosition()-climb.getEncoderPosition())<.5),
            // ampMech.setPositionCommand(AmpMech.Value.TRAP)
        );
    }

    public SequentialCommandGroup DropAmp (AmpMech ampMech, CycleTracker cycleTracker){
        return new SequentialCommandGroup(
            ampMech.setPositionCommand(AmpMech.Value.AMP), //true
            new InstantCommand(()->cycleTracker.trackCycle(CycleTracker.ScorePos.AMP)),
            new WaitCommand(.5),
            ampMech.setPositionCommand(AmpMech.Value.START)
        );
    }

    public Command(){}
}
