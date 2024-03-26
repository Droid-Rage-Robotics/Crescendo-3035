package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.intake.Intake;

public class IntakeElementInCommand extends SequentialCommandGroup {
    public IntakeElementInCommand(CommandXboxController driver, Intake intake) {
        addRequirements(intake.getIntakeWheel(), intake.getDropDown());
        addCommands(
            new ConditionalCommand(
                new SequentialCommandGroup(//true
                    intake.setPositionCommand(Intake.Value.SHOOTER_HOLD),
                    new InstantCommand(
                        ()->driver.getHID().setRumble(RumbleType.kBothRumble, 1)
                    ).withTimeout(3)
                ),
                new SequentialCommandGroup(//false
                    intake.setPositionCommand(Intake.Value.INTAKE_GROUND)
                ),
                intake::isElementInClaw)
        );
    }
}
