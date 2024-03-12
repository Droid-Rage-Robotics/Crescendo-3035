package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.intake.Intake;

public class ScoreTrap extends SequentialCommandGroup {
    public ScoreTrap (Claw claw){
        addCommands(
        );
    }   
}
