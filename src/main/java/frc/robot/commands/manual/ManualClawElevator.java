package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.claw.clawElevator.ClawElevator;
public class ManualClawElevator extends Command {
    private final ClawElevator clawElevator;
    private final Supplier<Double> move;
    
    public ManualClawElevator(ClawElevator clawElevator, Supplier<Double> move) {
        this.clawElevator = clawElevator;
        this.move = move;
        
        addRequirements(clawElevator);
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        double move = -this.move.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        clawElevator.setTargetPositionCommand(clawElevator.getTargetPosition() + move * 0.2);
        // verticalElevator.setTargetPositionCommand(verticalElevator.getTargetPosition() + move * 0.2);
        clawElevator.setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
