package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.Intake.Velocity;
import frc.robot.subsystems.intake.dropDown.IntakeDropDown;
import frc.robot.subsystems.intake.dropDown.IntakeDropDown.Position;

public class IntakeElementInCommand extends SequentialCommandGroup {
    public IntakeElementInCommand(Intake intake, IntakeDropDown intakeDropDown) {
        addRequirements(intake, intakeDropDown);
        addCommands(
            new ConditionalCommand(
                new SequentialCommandGroup(
                    intakeDropDown.setTargetCommand(Position.SHOOTER_TRANSFER),
                    intake.setTargetVelocityCommand(Velocity.STOP)
                ),
                new SequentialCommandGroup(),
                intake::isElementIn)
        );
    }
}
