package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.intake.Intake;

public class HumanToAmpMech extends SequentialCommandGroup {
    public HumanToAmpMech (Claw claw){
        addCommands(
            claw.setPositionCommand(Claw.Value.INTAKE_HUMAN),
            new WaitCommand(2), //Time For it to transfer
            claw.setPositionCommand(Claw.Value.HOLD)
        );
    }    
}
