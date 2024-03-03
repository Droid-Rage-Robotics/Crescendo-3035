package frc.robot.subsystems.claw.clawPivot;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.intake.IntakeWheel;
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

    protected final SafeCanSparkMax motor;
    protected final PIDController controller;
    protected ArmFeedforward feedforward;

    protected final ShuffleboardValue<Double> encoderPositionWriter = 
        ShuffleboardValue.create(0.0, "Pivot Encoder Position (Radians)", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = 
        ShuffleboardValue.create(0.0, "Pivot Encoder Velocity (Radians per Second)", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();

    protected final ShuffleboardValue<Boolean> isMovingManually = 
        ShuffleboardValue.create(false, " PivotMoving manually", Claw.class.getSimpleName())
        .build();
    
    public ClawPivot(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            24, 
            MotorType.kBrushless,
            false,
            IdleMode.Brake,
            Constants.ROTATIONS_TO_RADIANS,
            1.0,
            ShuffleboardValue.create(isEnabled, "Pivot Is Enabled", Claw.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "Pivot Voltage", Claw.class.getSimpleName())
                .build()
        );

        // encoder = motor.getEncoder();
  

        controller = new PIDController(0.0, 0.0, 0.0);//0.024
        controller.setTolerance(Math.toRadians(0.1));//How Much?

        feedforward = new ArmFeedforward(0.079284, 0.12603, 2.3793, 0.052763);//Old Values
        // feedforward = new ArmFeedforward(0, 0,0);

        ComplexWidgetBuilder.create(controller, "Pivot PID Controller", Claw.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 1);

        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetEncoder)), "Pivot Reset encoder", Claw.class.getSimpleName());

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
    
    // public Command setTargetPositionCommand(double target){
    //     return runOnce(()->setTargetPosition(target));
    // }
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

   
    public SafeCanSparkMax getMotor(){
        return motor;
    }
}  