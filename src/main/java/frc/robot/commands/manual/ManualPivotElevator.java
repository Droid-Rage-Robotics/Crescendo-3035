package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.clawPivot.Pivot;
public class ManualPivotElevator extends Command {
    private final Pivot pivot;
    private final Supplier<Double> pivotMove;
    
    public ManualPivotElevator(Pivot pivot, Supplier<Double> pivotMove) {
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
        pivot.setTargetPosition(pivot.getTargetPosition() + move * 0.2);
        pivot.setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
