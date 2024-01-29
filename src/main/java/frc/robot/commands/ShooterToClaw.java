package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.claw.clawElevator.ClawElevator;
import frc.robot.subsystems.claw.clawPivot.ClawPivot;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.Intake.Velocity;

public class ShooterToClaw extends SequentialCommandGroup {
    public ShooterToClaw(Shooter shooter, ClawElevator clawElevator, ClawPivot clawPivot, Claw claw, Intake intake) {
        addRequirements();
        addCommands(
            clawElevator.setTargetPositionCommand(ClawElevator.Position.SHOOTER_TRANSFER),
            clawPivot.setTargetPositionCommand(ClawPivot.Position.SHOOTER_TRANSFER),
            new WaitCommand(100),
            intake.setTargetVelocityCommand(Velocity.SHOOTER_TRANSFER),
            shooter.setTargetVelocity(Shooter.ShooterSpeeds.CLAW_TRANSFER),
            claw.setTargetVelocityCommand(Claw.Velocity.SHOOTER_TRANSFER),
            new WaitUntilCommand(()->claw.isElementInClaw()),//Wait Until it shows that it is in to continue
            claw.setTargetVelocityCommand(Claw.Velocity.STOP),
            clawPivot.setTargetPositionCommand(ClawPivot.Position.HOME),
            clawElevator.setTargetPositionCommand(ClawElevator.Position.HOME)


        );
    }
}