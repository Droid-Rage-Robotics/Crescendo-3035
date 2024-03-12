package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;

public class SetIntakeAndShooter extends SequentialCommandGroup {
    public SetIntakeAndShooter (Intake intake, Intake.Value intakePos, Shooter shooter, Shooter.ShooterSpeeds speed){
        addCommands(
            new ParallelCommandGroup(
                intake.setPositionCommand(intakePos),
                shooter.runOnce(()->shooter.setTargetVelocity(speed)))
            );
    }   
}
