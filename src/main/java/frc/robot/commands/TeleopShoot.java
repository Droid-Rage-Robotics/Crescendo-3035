package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.intake.Intake;

public class TeleopShoot extends SequentialCommandGroup {
    public TeleopShoot (Intake intake, Shooter shooter){
        addCommands(
            new SequentialCommandGroup(
                shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
                )
            );
    }   
}
