package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.ampMech.AmpMechElevator;
public class ManualAmpMechElevator extends Command {
    private final AmpMech claw;
    private final Supplier<Double> move;
    
    public ManualAmpMechElevator(AmpMech claw, Supplier<Double> move) {
        this.claw = claw;
        this.move = move;
        
        addRequirements(claw.getElevator());
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        double move = -this.move.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        claw.manualAmpMechElevator(claw.getAmpMechElevatorTarget() + move * 0.2);
        // verticalElevator.setTargetPositionCommand(verticalElevator.getTargetPosition() + move * 0.2);
        claw.getElevator().setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
