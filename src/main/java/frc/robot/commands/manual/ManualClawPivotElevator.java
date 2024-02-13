package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.claw.clawPivot.ClawPivot;
public class ManualClawPivotElevator extends Command {
    private final Claw claw;
    private final Supplier<Double> pivotMove;
    
    public ManualClawPivotElevator(Claw claw, Supplier<Double> pivotMove) {
        this.claw = claw;
        this.pivotMove = pivotMove;
        
        addRequirements(claw.getClawPivot());
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        double move = -pivotMove.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        claw.manualClawPivot(claw.getClawPivotTarget() + move * 0.2);
        claw.getClawPivot().setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
