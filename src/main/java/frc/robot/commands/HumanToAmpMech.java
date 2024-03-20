package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ampMech.AmpMech;

public class HumanToAmpMech extends SequentialCommandGroup {
    public HumanToAmpMech (AmpMech claw){
        addCommands(
            claw.setPositionCommand(AmpMech.Value.INTAKE_HUMAN),
            new WaitCommand(2), //Time For it to transfer
            claw.setPositionCommand(AmpMech.Value.HOLD)
        );
    }    
}
