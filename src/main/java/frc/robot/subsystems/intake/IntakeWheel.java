package frc.robot.subsystems.intake;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.general.DisabledCommand;
import frc.robot.utility.motor.old.SafeTalonFX;
import frc.robot.utility.motor.old.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeWheel extends SubsystemBase {
     

    public static class Constants {
        public static final double GEAR_RATIO = 1 / 3;//1/1
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }

    protected final SafeTalonFX motor;
    protected final PIDController intakeController;
    protected final SimpleMotorFeedforward intakeFeedforward;
    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create    
      (0.0, "Intake/Target Wheel Velocity", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
         (0.0, "Intake/Wheel Encoder Velocity", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Intake/Wheel Encoder Velocity Error", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> voltageWriter = ShuffleboardValue.create
        (0.0, "Intake/Voltage", Intake.class.getSimpleName()).build();

    private final ShuffleboardValue<Boolean> isElementInWriter = ShuffleboardValue.create
            (false, "Is Element In", Intake.class.getSimpleName()).build();


    public IntakeWheel(Boolean isEnabled) {
        motor = new SafeTalonFX(
            16,
            true,
            IdleMode.Coast,
            Constants.ROTATIONS_TO_RADIANS,
            Constants.ROTATIONS_TO_RADIANS,
            ShuffleboardValue.create(isEnabled, "Intake/Is Enabled Wheel", Intake.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "Intake/Output Writer", Intake.class.getSimpleName())
                .build()//25
            // ,10
        );
        // intakeEncoder = intake.getEncoder();

        intakeController = new PIDController(
            0.0003,//0.0003 
            0,
            0);
        intakeController.setTolerance(5);
        // intakeFeedforward = new SimpleMotorFeedforward(0, 0, 0);
        intakeFeedforward = new SimpleMotorFeedforward(0.64, 0.000515, 0);

        ComplexWidgetBuilder.create(intakeController, "Wheel PID", Intake.class.getSimpleName());
        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetIntakeEncoder)), 
            "Reset Intake Wheel Encoder", Intake.class.getSimpleName());
        
    }

    @Override
    public void periodic() {
        motor.setVoltage(calculateIntakePID(getIntakeTargetVelocity()) + 
            calculateIntakeFeedforward(getIntakeTargetVelocity()));
        voltageWriter.set(motor.getVoltage());
        isElementInWriter.set(isElementIn());
        
    }
    
    public void setTargetPosition(double  target){
        intakeController.setSetpoint(target);
        targetVelocityWriter.set(target);
    }

    protected double getIntakeEncoderVelocity() {
        double velocity = motor.getVelocity();
        encoderVelocityWriter.write(velocity);
        encoderVelocityErrorWriter.write(intakeController.getPositionError());

        // encoderVelocityErrorWriter.write(getIntakeTargetVelocity() - velocity);
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
        motor.setPosition(0);//TODO: Test
    }

    public boolean isElementIn(){
        // return encoderVelocityErrorWriter.get()<-1000;
        return voltageWriter.get()<1.75;
        //or color sensor 
    }
    public Command setPower(double  power){
        return new InstantCommand(()->{
            motor.setPower(power);
        });
    }
    public SafeTalonFX getMotor(){
        return motor;
    }
}

