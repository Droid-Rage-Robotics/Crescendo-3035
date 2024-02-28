package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.intake.Intake;

public class ClimbAndScoreSequence extends SequentialCommandGroup {
    public ClimbAndScoreSequence (Claw claw, Climb climb, Intake intake){
        addCommands(
            new ParallelCommandGroup(
                climb.runOnce(()->climb.setTargetCommand(Climb.Position.CLIMB))
            ),
            
            Commands.waitSeconds(1),
            climb.runOnce(()->climb.setTargetCommand(Climb.Position.TRAP))
        );
    }
    
}
