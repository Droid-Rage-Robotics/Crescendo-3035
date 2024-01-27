package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.claw.clawElevator.ClawElevator;
public class ManualClawElevator extends Command {
    private final ClawElevator verticalElevator;
    private final Supplier<Double> verticalElevatorMove;
    
    public ManualClawElevator(ClawElevator verticalElevator, Supplier<Double> verticalElevatorMove) {
        this.verticalElevator = verticalElevator;
        this.verticalElevatorMove = verticalElevatorMove;
        
        addRequirements(verticalElevator);
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        double move = -verticalElevatorMove.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        verticalElevator.setTargetPosition(verticalElevator.getTargetPosition() + move * 0.2);
        // verticalElevator.setTargetPositionCommand(verticalElevator.getTargetPosition() + move * 0.2);
        verticalElevator.setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}