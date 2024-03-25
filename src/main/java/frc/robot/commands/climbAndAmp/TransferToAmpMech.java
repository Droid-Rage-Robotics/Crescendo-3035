package frc.robot.commands.climbAndAmp;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.intake.Intake;

public class TransferToAmpMech extends SequentialCommandGroup {
    public TransferToAmpMech (Intake intake, Shooter shooter, AmpMech ampMech){
        addCommands(
            new ParallelCommandGroup(
                ampMech.setPositionCommand(AmpMech.Value.SHOOTER),
                shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.CLAW_TRANSFER)),
                intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
            ),
            // new WaitUntilCommand(()->shooter.isShooterTransferAmp()),
            new WaitCommand(.4),
            new ParallelCommandGroup(
                ampMech.setPositionCommand(AmpMech.Value.HOLD),
                shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.STOP)),
                intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
            )

            //Old
            // new ParallelCommandGroup(
            //     ampMech.setPositionCommand(AmpMech.Value.INTAKE_SHOOTER),
            //     shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.CLAW_TRANSFER)),
            //     intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
            // ),
            // new WaitCommand(1),
            // new ParallelCommandGroup(
            //     ampMech.setPositionCommand(AmpMech.Value.HOLD),
            //     shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.STOP)),
            //     intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
            // )
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
