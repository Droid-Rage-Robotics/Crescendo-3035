package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.claw.ClawElevator;
public class ManualClawElevator extends Command {
    private final Claw claw;
    private final Supplier<Double> move;
    
    public ManualClawElevator(Claw claw, Supplier<Double> move) {
        this.claw = claw;
        this.move = move;
        
        addRequirements(claw.getClawElevator());
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        double move = -this.move.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        claw.manualClawElevator(claw.getClawElevatorTarget() + move * 0.2);
        // verticalElevator.setTargetPositionCommand(verticalElevator.getTargetPosition() + move * 0.2);
        claw.getClawElevator().setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
