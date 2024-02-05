package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.intake.Intake;

public class IntakeElementInCommand extends SequentialCommandGroup {
    public IntakeElementInCommand(Intake intake) {
        addRequirements(intake.getIntakeWheel(), intake.getDropDown());
        addCommands(
            new ConditionalCommand(
                new SequentialCommandGroup(
                    intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
                ),
                new SequentialCommandGroup(),
                intake::isElementInClaw)
        );
    }
}
