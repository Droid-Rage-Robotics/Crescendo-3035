package frc.robot.commands.climbAndAmp;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.climb.ClimbAlternate;
import frc.robot.subsystems.intake.Intake;

public class ClimbAndTrap extends SequentialCommandGroup {
    public ClimbAndTrap (Intake intake, Shooter shooter, AmpMech ampMech, Climb climb){
        addCommands(
            new ParallelCommandGroup(
                climb.runOnce(()->climb.setTargetPosition(Climb.Position.TRAP)),
                ampMech.setPositionCommand(AmpMech.Value.HOLD_TRAP),
                intake.setPositionCommand(Intake.Value.CLIMB)
            )
            // ,new WaitUntilCommand(()->(climb.getError())<.5),
            // new WaitUntilCommand(()->Math.abs(climb.getTargetPosition()-climb.getEncoderPosition())<.5),
            // ampMech.setPositionCommand(AmpMech.Value.TRAP)
        );
    }
}