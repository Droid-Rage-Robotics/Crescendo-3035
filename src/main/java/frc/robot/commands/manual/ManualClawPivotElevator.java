package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.claw.clawPivot.ClawPivot;
public class ManualClawPivotElevator extends Command {
    private final ClawPivot pivot;
    private final Supplier<Double> pivotMove;
    
    public ManualClawPivotElevator(ClawPivot pivot, Supplier<Double> pivotMove) {
        this.pivot = pivot;
        this.pivotMove = pivotMove;
        
        addRequirements(pivot);
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        double move = -pivotMove.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        pivot.setTargetPositionCommand(pivot.getTargetPosition() + move * 0.2);
        pivot.setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
