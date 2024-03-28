package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.ampMech.AmpMech;

public class ManualElevator extends Command {
    private final AmpMech ampMech;
    private final Supplier<Double> moveElev;
    
    public ManualElevator(Supplier<Double> moveElev, AmpMech ampMech) {
        this.ampMech = ampMech;
        this.moveElev = moveElev;
        
        addRequirements(ampMech.getElevator());
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        double move = -moveElev.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        ampMech.getElevator().setTargetPosition(ampMech.getElevator().getTargetPosition() + move * 0.2);
        ampMech.getElevator().setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
