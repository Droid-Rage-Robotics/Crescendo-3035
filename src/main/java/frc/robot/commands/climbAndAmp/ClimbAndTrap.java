package frc.robot.commands.climbAndAmp;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.intake.Intake;

public class ClimbAndTrap extends SequentialCommandGroup {
    public ClimbAndTrap (Intake intake, Shooter shooter, AmpMech ampMech, Climb climb){
        addCommands(
            new ParallelCommandGroup(//CLIMBING
                // climb.climb
                ampMech.setPositionCommand(AmpMech.Value.CLIMB)
            )
            
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
