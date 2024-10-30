package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.intake.Intake;
public class ManualClimb extends Command {
    private final Climb climb;
    private final Supplier<Double> climbMove;
    private boolean isClimbing = false;
    private final Intake intake;
    
    public ManualClimb(Climb climb, Supplier<Double> climbMove, Intake intake) {
        this.climb = climb;
        this.climbMove = climbMove;
        this.intake = intake;
        
        addRequirements(climb);
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        // if(!isClimbing){
        //     if(climb.getEncoderPosition() > 15){
        //         isClimbing = true;
        //     }
        // }
        // if(isClimbing){
        //     intake.setPositionCommand(Intake.Value.CLIMB);
        // }
        double move = -climbMove.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        // climb.setPower(move*1);
        // climb.setTargetPosition(climb.getTargetPosition() + move * 0.4);//For Motor
        climb.setTargetPosition(climb.getTargetPosition() + move * 0.05);//For Encoder

        climb.setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
