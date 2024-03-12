package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.intake.Intake;

public class TransferToAmpMech extends SequentialCommandGroup {
    public TransferToAmpMech (Intake intake, Shooter shooter, Claw claw){
        addCommands(
            claw.setPositionCommand(Claw.Value.INTAKE_SHOOTER),
            new WaitCommand(2),
            shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.CLAW_TRANSFER)),
            intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER),
            new WaitCommand(3), //Time For it to transfer
            claw.setPositionCommand(Claw.Value.HOLD),
            shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.HOLD)),
            intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
        );
    }

    // public SequentialCommandGroup s (Intake intake, Shooter shooter, Claw claw){
    //     return new SequentialCommandGroup(
    //         claw.setPositionCommand(Claw.Value.INTAKE_SHOOTER),
    //         new WaitCommand(2),
    //         shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.CLAW_TRANSFER)),
    //         intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
    //     );
    // }
    // public SequentialCommandGroup intake (Intake intake){
    //     return new SequentialCommandGroup(
    //         intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
    //     );
    // }
    
}
