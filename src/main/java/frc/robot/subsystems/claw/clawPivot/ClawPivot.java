package frc.robot.subsystems.claw.clawPivot;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
import frc.robot.utility.shuffleboard.ShuffleboardValueEnum;

public class ClawPivot extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 180;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }

     public enum Position implements ShuffleboardValueEnum<Double> {
        SHOOTER_TRANSFER(-3000),
        HOME(2300),
        HUMAN_PLAYER (200),
        AMP (200),
        TRAP (0)
        ;

        private final ShuffleboardValue<Double> position;
        private Position(double position) {
            this.position = ShuffleboardValue.create(position, Position.class.getSimpleName()+"/"+name()+": Velocity (RPM)", Intake.class.getSimpleName())
                .withSize(1, 3)
                .build();
        }
        @Override
        public ShuffleboardValue<Double> getNum() {
            return position;
        }
    }

    protected final SafeCanSparkMax motor;
    protected final PIDController controller;
    protected ArmFeedforward feedforward;
    // protected final RelativeEncoder encoder;

    protected final ShuffleboardValue<Double> encoderPositionWriter = 
        ShuffleboardValue.create(0.0, "Encoder Position (Radians)", ClawPivot.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = 
        ShuffleboardValue.create(0.0, "Encoder Velocity (Radians per Second)", ClawPivot.class.getSimpleName())
        .withSize(1, 2)
        .build();

    protected final ShuffleboardValue<Boolean> isMovingManually = 
        ShuffleboardValue.create(false, "Moving manually", ClawPivot.class.getSimpleName())
        .build();
    
    public ClawPivot(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            18, 
            MotorType.kBrushless,
            ShuffleboardValue.create(isEnabled, "Is Enabled", ClawPivot.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "Voltage", ClawPivot.class.getSimpleName())
                .build()
        );
        motor.setIdleMode(IdleMode.Coast);//TODO: Make it brake 
        motor.setInverted(false);
        // encoder = motor.getEncoder();
        motor.getEncoder().setPositionConversionFactor(Constants.ROTATIONS_TO_RADIANS);
        motor.getEncoder().setVelocityConversionFactor(Constants.ROTATIONS_TO_RADIANS);
  

        controller = new PIDController(0.0, 0.0, 0.0);//0.024
        controller.setTolerance(Math.toRadians(0.1));//How Much?

        // feedforward = new ArmFeedforward(0.079284, 0.12603, 2.3793, 0.052763);
        // feedforward = new ArmFeedforward(0, 0,0);

        ComplexWidgetBuilder.create(controller, "PID Controller", ClawPivot.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 1);

        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetEncoder)), "Reset encoder", ClawPivot.class.getSimpleName());

        motor.burnFlash();
    }

    @Override
    public void periodic() {
        // motor.set(calculateFeedforward(getTargetPosition(), 0.) + calculatePID(getTargetPosition()));
        setVoltage(calculateFeedforward(getTargetPosition(), 2.3793) + calculatePID(getTargetPosition()));
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }
     public Command setTargetPositionCommand(Position target){
        return runOnce(()->setTargetPosition(target.get()));
    }
    public Command setTargetPositionCommand(double target){
        return runOnce(()->setTargetPosition(target));
    }
    public void setTargetPosition(double target){
        controller.setSetpoint(target);
    }

    public void setMovingManually(boolean value) {
        isMovingManually.set(value);
    }

    public boolean isMovingManually() {
        return isMovingManually.get();
    }


    // public void setTargetPosition(double positionRadians) {
    //     controller.setSetpoint(positionRadians);
    // }

    public double getTargetPosition() {
        return controller.getSetpoint();
    }

    public void resetEncoder() {
        motor.getEncoder().setPosition(0);
    }

    public double getEncoderPosition() {
        double position = motor.getEncoder().getPosition();
        encoderPositionWriter.write(position);
        return position;
    }

    public double getEncoderVelocity() {
        double velocity = motor.getEncoder().getVelocity();
        encoderVelocityWriter.write(velocity);
        return velocity;
    }

    protected void setVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    protected void stop() {
        motor.stop();
    }

    protected double calculateFeedforward(double positionRadians, double velocity) {
        return feedforward.calculate(positionRadians, velocity);
    }

    protected double calculatePID(double positionRadians) {
        return controller.calculate(getEncoderPosition(), positionRadians);
    }

   
}  