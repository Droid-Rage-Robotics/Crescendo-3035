package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.InfoTracker.CycleTracker;

public class ClimbAndAmpCommands {
    public static SequentialCommandGroup transferToAmpMech (Intake intake, Shooter shooter, AmpMech ampMech){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                ampMech.setPositionCommand(AmpMech.Value.SHOOTER),
                shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.CLAW_TRANSFER)),
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
            ),
            // new WaitUntilCommand(()->shooter.isShooterTransferAmp()),
            new WaitCommand(.5),
            new ParallelCommandGroup(
                ampMech.setPositionCommand(AmpMech.Value.HOLD),
                shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.STOP)),
                intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
            )
        );
    }

    public static SequentialCommandGroup dropAmp (AmpMech ampMech, CycleTracker cycleTracker){
        return new SequentialCommandGroup(
            ampMech.setPositionCommand(AmpMech.Value.AMP), //true
            // new InstantCommand(()->cycleTracker.trackCycle(CycleTracker.ScorePos.AMP)),
            new WaitCommand(.5),
            ampMech.setPositionCommand(AmpMech.Value.START)
        );
    }

    public static SequentialCommandGroup climbAndTrap (Intake intake, Shooter shooter, AmpMech ampMech, Climb climb){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                climb.runOnce(()->climb.setTargetPosition(Climb.Position.TRAP)),
                ampMech.setPositionCommand(AmpMech.Value.START),
                intake.setPositionCommand(Intake.Value.CLIMB_DOWN)//CLIMB_HOLD
            )
        );
    }
}
