package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.claw.Claw.Value;
import frc.robot.subsystems.intake.Intake;

public class ShooterToClaw extends SequentialCommandGroup {
    public ShooterToClaw(Shooter shooter, Claw claw, Intake intake) {
        addRequirements();
        addCommands(
            claw.setPositionCommand(Value.INTAKE_SHOOTER),
            new WaitCommand(100),
            intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
            shooter.runOnce(()->shooter.setTargetVelocity(Shooter.ShooterSpeeds.CLAW_TRANSFER)),
            new WaitUntilCommand(()->claw.isElementInClaw()),
            //^^Wait Until it shows that it is in to continue
            claw.setPositionCommand(Value.HOLD)


        );
    }
}