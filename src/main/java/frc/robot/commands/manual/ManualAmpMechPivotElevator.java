package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.ampMech.AmpMech;
public class ManualAmpMechPivotElevator extends Command {
    // private final AmpMech claw;
    // private final Supplier<Double> pivotMove;
    
    // public ManualAmpMechPivotElevator(AmpMech claw, Supplier<Double> pivotMove) {
    //     this.claw = claw;
    //     this.pivotMove = pivotMove;
        
    //     addRequirements(claw.getPivot());
    // }

    // @Override
    // public void initialize() { }

    // @Override
    // public void execute() {
    //     double move = -pivotMove.get();
    //     move = DroidRageConstants.squareInput(move);
    //     move = DroidRageConstants.applyDeadBand(move);
    //     claw.manualAmpMechPivot(claw.getAmpMechPivotTarget() + move * 0.2);
    //     claw.getPivot().setMovingManually(!(move == 0));
    // }

    // @Override
    // public void end(boolean interrupted) {}

    // @Override
    // public boolean isFinished() {
    //     return false;
    // }
}
