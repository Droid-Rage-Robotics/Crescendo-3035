package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.commands.LightCommand.IntakeState;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeWheel extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 3;//?????
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }

    protected final SafeTalonFX intake;
    protected final PIDController intakeController;
    protected final SimpleMotorFeedforward intakeFeedforward;
    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create    
      (0.0, "Target Wheel Velocity", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
         (0.0, "Wheel Encoder Velocity", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Wheel Encoder Velocity Error", IntakeState.class.getSimpleName()).build();

    private final ShuffleboardValue<Boolean> isElementInWriter = ShuffleboardValue.create
            (false, "Is Element In", IntakeState.class.getSimpleName()).build();


    public IntakeWheel(Boolean isEnabled) {
        intake = new SafeTalonFX(
            15,
            true,
            IdleMode.Coast,
            ShuffleboardValue.create(isEnabled, "Is Enabled Wheel", Intake.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Voltage Wheel", Intake.class.getSimpleName())
                    .build()
        );
        // intakeEncoder = intake.getEncoder();

        intakeController = new PIDController(
            0.0003,//0.0003 
            0,
            0);
        intakeController.setTolerance(5);
        intakeFeedforward = new SimpleMotorFeedforward(0.64, 0.000515, 0);

        ComplexWidgetBuilder.create(intakeController, "Intake Wheel Controller", Intake.class.getSimpleName());
        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetIntakeEncoder)), 
            "Reset Intake Wheel Encoder", Intake.class.getSimpleName());
    }

    @Override
    public void periodic() {
        intake.setVoltage(calculateIntakePID(getIntakeTargetVelocity()) + 
            calculateIntakeFeedforward(getIntakeTargetVelocity()));
        isElementInWriter.set(isElementIn());
        
    }
    
    public Command setTargetPosition(double  target){
        return new InstantCommand(()->{
            intakeController.setSetpoint(target);
            targetVelocityWriter.set(target);
        });
    }

    protected double getIntakeEncoderVelocity() {
        double velocity = intake.getVelocity();
        encoderVelocityWriter.write(velocity);
        encoderVelocityErrorWriter.write(getIntakeTargetVelocity() - velocity);
        return velocity;
    }
    protected double getIntakeTargetVelocity() {
        return intakeController.getSetpoint();
    }
    protected double calculateIntakePID(double targetVelocity) {
        return intakeController.calculate(getIntakeEncoderVelocity(), targetVelocity);
    }
    protected double calculateIntakeFeedforward(double targetVelocity) {
        return intakeFeedforward.calculate(targetVelocity);
    }

    
    public void resetIntakeEncoder() {
        intake.setPosition(0);//TODO: Test
    }

    public boolean isElementIn(){
        return encoderVelocityErrorWriter.get()<-2000;
    }
}

