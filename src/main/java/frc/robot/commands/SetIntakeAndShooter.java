package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;

public class SetIntakeAndShooter extends SequentialCommandGroup {
    public SetIntakeAndShooter (Intake intake, Intake.Value intakePos, Shooter shooter, Shooter.ShooterSpeeds speed){
        addCommands(
            new SequentialCommandGroup(
                shooter.runOnce(()->shooter.setTargetVelocity(speed)),
                new WaitCommand(.8),//.6
                intake.setPositionCommand(intakePos)
                )
            );
    }   
}
