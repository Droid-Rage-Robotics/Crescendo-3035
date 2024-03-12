package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.intake.Intake;

//This No Work In Head
public class ClimbAndScoreSequence extends SequentialCommandGroup {
    public ClimbAndScoreSequence (AmpMech claw, Climb climb, Intake intake){
        addCommands(
            new ParallelCommandGroup(
                climb.runOnce(()->climb.setTargetPosition(Climb.Position.CLIMB))
            ),
            
            Commands.waitSeconds(1),
            climb.runOnce(()->climb.setTargetPosition(Climb.Position.TRAP))
        );
    }
    
}
