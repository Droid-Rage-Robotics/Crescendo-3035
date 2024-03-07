package frc.robot.subsystems.claw;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

@Deprecated
public class ClawIntake extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 180;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }

    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create
        (0.0, "Intake/ Claw Intake Target Velocity", Claw.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
        (0.0, "Intake/ Claw Intake Encoder Velocity", Claw.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Intake/ Claw Intake Encoder Velocity Error", Claw.class.getSimpleName()).build();

    private final ShuffleboardValue<Boolean> isElementInClawWriter = ShuffleboardValue.create
            (false, "Intake/ Claw Intake Is Element In", Claw.class.getSimpleName()).build();
    
    
    protected final SafeCanSparkMax motor;
    protected final PIDController controller;
    protected final SimpleMotorFeedforward feedforward;

    public ClawIntake(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            25,
            MotorType.kBrushless,
            false,
            IdleMode.Coast,
            1,
            1,
            ShuffleboardValue.create(isEnabled, "Intake/ Claw Intake Is Enabled", Claw.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Intake/ Claw Intake Voltage", Claw.class.getSimpleName())
                    .build()
        );

        controller = new PIDController(
            0.0003,//0.0003 
            0,
            0);
        controller.setTolerance(5);
        feedforward = new SimpleMotorFeedforward(0, 0, 0);
        // feedforward = new SimpleMotorFeedforward(0.64, 0.000515, 0);


        ComplexWidgetBuilder.create(controller, "Claw Intake Controller", Claw.class.getSimpleName());
        ComplexWidgetBuilder.create(
            DisabledCommand.create(runOnce(this::resetEncoder)),
            "Claw Intake Reset Claw Encoder", Claw.class.getSimpleName());
    }

    @Override
    public void periodic() {
        motor.setVoltage(controller.calculate(getEncoderVelocity(),getTargetPosition()) + 
            feedforward.calculate(getTargetPosition()));
        isElementInClawWriter.set(isElementInClaw());
        
    }
    
    
    protected void setTargetPosition(double target) {
        controller.setSetpoint(target);
        targetVelocityWriter.set(target);
    }
    protected double getEncoderVelocity() {
        double velocity = motor.getEncoder().getVelocity();
        encoderVelocityWriter.write(velocity);
        encoderVelocityErrorWriter.write(getTargetPosition() - velocity);
        return velocity;
    }
    public void resetEncoder() {
        motor.getEncoder().setPosition(0);//TODO: Test
    }

    protected boolean isElementInClaw(){
        return encoderVelocityErrorWriter.get()<-2000;
    }
    public double getTargetPosition(){
        return controller.getSetpoint();
    }

    public SafeCanSparkMax getMotor(){
        return motor;
    }
}

