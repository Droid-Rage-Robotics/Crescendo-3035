package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.intake.Intake;

public class AutoShoot extends SequentialCommandGroup {
    public AutoShoot (Intake intake, Shooter shooter){
        addCommands(
            new SequentialCommandGroup(
                // shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.AUTO_SPEAKER_SHOOT)),
                // new WaitCommand(.5),//.6
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
                )
            );
    }   
}
