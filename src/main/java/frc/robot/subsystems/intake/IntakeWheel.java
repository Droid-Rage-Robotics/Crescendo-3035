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
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeWheel extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 180;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }

    // public enum Velocity implements ShuffleboardValueEnum<Double> {
    //     INTAKE(-3000),
    //     OUTTAKE(2300),
    //     SHOOTER_TRANSFER(200),
    //     ELEV_TRANSFER(200),
    //     STOP(0)
    //     ;

    //     private final ShuffleboardValue<Double> velocityRPM;

    //     private Velocity(double velocityRPM) {
    //         this.velocityRPM = ShuffleboardValue.create(velocityRPM, Velocity.class.getSimpleName()+"/"+name()+": Velocity (RPM)", IntakeWheel.class.getSimpleName())
    //             .withSize(1, 3)
    //             .build();
    //     }

    //     @Override
    //     public ShuffleboardValue<Double> getNum() {
    //         return velocityRPM;
    //     }
    // }

    protected final SafeCanSparkMax intake;
    protected final PIDController intakeController;
    protected final SimpleMotorFeedforward intakeFeedforward;
    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create    
      (0.0, "Target Velocity", IntakeWheel.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
         (0.0, "Encoder Velocity", IntakeWheel.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity Error", IntakeState.class.getSimpleName()).build();

    private final ShuffleboardValue<Boolean> isElementInWriter = ShuffleboardValue.create
            (false, "Is Element In", IntakeState.class.getSimpleName()).build();


    public IntakeWheel(Boolean isEnabled) {
        intake = new SafeCanSparkMax(
            19,
            MotorType.kBrushless,
            ShuffleboardValue.create(isEnabled, "Is Enabled", IntakeWheel.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Voltage", IntakeWheel.class.getSimpleName())
                    .build()
        );
        // intakeEncoder = intake.getEncoder();
        intake.setIdleMode(IdleMode.Coast);
        intake.setInverted(true);

        intakeController = new PIDController(
            0.0003,//0.0003 
            0,
            0);
        intakeController.setTolerance(5);
        intakeFeedforward = new SimpleMotorFeedforward(0.64, 0.000515, 0);

        ComplexWidgetBuilder.create(intakeController, "Intake Controller", IntakeWheel.class.getSimpleName());
        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetIntakeEncoder)), 
            "Reset Intake Encoder", IntakeWheel.class.getSimpleName());
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
        double velocity = intake.getEncoder().getVelocity();
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
        intake.getEncoder().setPosition(0);//TODO: Test
    }

    public boolean isElementIn(){
        return encoderVelocityErrorWriter.get()<-2000;
    }
}

