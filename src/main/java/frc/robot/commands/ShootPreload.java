package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.intake.Intake;

public class ShootPreload extends SequentialCommandGroup {
    public ShootPreload (Intake intake, Intake.Value intakePos, Shooter shooter, Shooter.ShooterSpeeds speed){
        addCommands(
            new SequentialCommandGroup(
                shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
                new WaitCommand(.8),
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
                )
            );

    }   
}
