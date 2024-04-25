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
//THIS WORKS
public class CommandList {
    public SequentialCommandGroup climbAndTrap(Intake intake, Shooter shooter, AmpMech ampMech, Climb climb){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                climb.runOnce(()->climb.setTargetPosition(Climb.Position.TRAP)),
                ampMech.setPositionCommand(AmpMech.Value.START),
                intake.setPositionCommand(Intake.Value.CLIMB_DOWN)//CLIMB_HOLD
            )
        );
    }

    public SequentialCommandGroup dropAmp (AmpMech ampMech, CycleTracker cycleTracker){
        return new SequentialCommandGroup(
            ampMech.setPositionCommand(AmpMech.Value.AMP), //true
            new InstantCommand(()->cycleTracker.trackCycle(CycleTracker.ScorePos.AMP)),
            new WaitCommand(.5),
            ampMech.setPositionCommand(AmpMech.Value.START)
        );
    }
    public static SequentialCommandGroup intake (Intake intake){
        return new SequentialCommandGroup(
            intake.setPositionCommand(Intake.Value.INTAKE_GROUND)
        );
    }
    public static SequentialCommandGroup outtake (Intake intake){
        return new SequentialCommandGroup(
            intake.setPositionCommand(Intake.Value.OUTTAKE)
        );
    }
    public static SequentialCommandGroup hold (Intake intake){
        return new SequentialCommandGroup(
            intake.setPositionCommand(Intake.Value.HOLD)
        );
    }

    public CommandList(){}
}
