package frc.robot.subsystems.ampMech.ampMechArm;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

@Deprecated
public class AmpMechArmMotionProfiled extends AmpMechArm {
    public static class Constants {
        public static double MIN_POSITION = Math.toRadians(20);
        public static double MAX_POSITION = Math.toRadians(230);
    }
    protected TrapezoidProfile profile;
    protected final TrapezoidProfile.Constraints constraints;
    protected TrapezoidProfile.State state;
    protected TrapezoidProfile.State goal;

    protected final ShuffleboardValue<Double> goalPositionWriter = ShuffleboardValue.create(0.0, "Goal Position", AmpMech.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> goalVelocityWriter = ShuffleboardValue.create(0.0, "Goal Velocity", AmpMech.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create(0.0, "Target Velocity", AmpMech.class.getSimpleName()).build();
    
    public AmpMechArmMotionProfiled(Boolean isEnabled) {
        super(isEnabled);

        controller.setPID(5, 0, 0);//kP=5
        controller.setTolerance(0.10);

        // feedforward = new ArmFeedforward(0.079284, 0.12603, 2.3793, 0.07);
        feedforward = new ArmFeedforward(0.14762,0.16904,1.8031,0.036501);

        constraints = new TrapezoidProfile.Constraints(
            8, // radians per second
            16 //radians per second per second
        );

        state = new TrapezoidProfile.State(0, 0);
        goal = new TrapezoidProfile.State(0, 0);
        
        profile = new TrapezoidProfile(constraints, goal, state);
    }

    @Override
    public void periodic() {
        if (isMovingManually()) {
            double encoderPosition = getEncoderPosition();
            if (encoderPosition < Constants.MIN_POSITION) {
                stop();
                return;
            }
            if (encoderPosition > Constants.MAX_POSITION) {
                stop();
                return;
            }
            setVoltage(calculateFeedforward(encoderPosition, getTargetVelocity()));
            return;
        } 
        profile = new TrapezoidProfile(constraints, goal, state);
        state = profile.calculate(0.02); // 0.02 taken from TrapezoidProfileSubsystem measured in seconds
        setVoltage(calculateFeedforward(state.position, state.velocity) + calculatePID(state.position));
        getEncoderVelocity();
        
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }

    // @Override
    // public Command setTargetPositionCommand(Position target) {
    //     setMovingManually(false);
    //     return runOnce(()->setTarget(target.get(), 0));
    // }

    public void setTargetVelocity(double velocityRadiansPerSecond) {
        setMovingManually(true);
        targetVelocityWriter.write(velocityRadiansPerSecond);
    }

    public double getTargetVelocity() {
        return targetVelocityWriter.get();
    }

    public void setTarget(double positionRadians, double velocityRadiansPerSecond) {
        setMovingManually(false);
        if (positionRadians < Constants.MIN_POSITION) return;
        if (positionRadians > Constants.MAX_POSITION) return;
        goalPositionWriter.write(positionRadians);
        goalVelocityWriter.write(velocityRadiansPerSecond);
        state = new TrapezoidProfile.State(getEncoderPosition(), getEncoderVelocity());
        goal = new TrapezoidProfile.State(positionRadians, velocityRadiansPerSecond);
    }
}  