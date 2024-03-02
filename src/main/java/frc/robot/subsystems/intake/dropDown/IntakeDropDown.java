package frc.robot.subsystems.intake.dropDown;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeDropDown extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 2;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;//4089
        public static final double ROTATIONS_TO_RADIANS = (2 * Math.PI / READINGS_PER_REVOLUTION)*2; //<--THIS WORK
        // (2 * Math.PI / READINGS_PER_REVOLUTION)/(GEAR_RATIO);
        //  (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
        //  (Math.PI * 2)/(GEAR_RATIO * READINGS_PER_REVOLUTION);
    }


    protected final SafeTalonFX motor;
    protected final PIDController controller;
    protected ArmFeedforward feedforward;
    // protected final DigitalInput intakeLimitSwitch; //Limit Switch?

    protected final ShuffleboardValue<Double> encoderPositionWriter = 
        ShuffleboardValue.create(0.0, "Encoder Position (Radians)", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = 
        ShuffleboardValue.create(0.0, "Encoder Velocity (Radians per Second)", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();

    protected final ShuffleboardValue<Boolean> isMovingManually = 
        ShuffleboardValue.create(false, "Moving manually", Intake.class.getSimpleName())
        .build();
    
    public IntakeDropDown(Boolean isEnabled) {
        motor = new SafeTalonFX(
            17,
            true,
            IdleMode.Coast,
            Constants.ROTATIONS_TO_RADIANS,
            Constants.ROTATIONS_TO_RADIANS,
            ShuffleboardValue.create(isEnabled, "Drop Is Enabled", Intake.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "Drop Voltage", Intake.class.getSimpleName())
                .build()
        );

        controller = new PIDController(0.1, 0.0, 0.0);//0.024
        controller.setTolerance(Math.toRadians(0.1));

        // feedforward = new ArmFeedforward(0.079284, 0.12603, 2.3793, 0.052763);
        feedforward = new ArmFeedforward(0, 0,0);

        ComplexWidgetBuilder.create(controller, "Drop PID Controller", Intake.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 1);

        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetEncoder)), "Reset Intake Drop Encoder", 
            Intake.class.getSimpleName());

        // intakeLimitSwitch = new DigitalInput(5);//WHERE is it plugged in
    }

    @Override
    public void periodic() {
        // motor.set(calculateFeedforward(getTargetPosition(), 0.) + calculatePID(getTargetPosition()));
        setVoltage(calculateFeedforward(getTargetPosition(), 2.3793) + calculatePID(getTargetPosition()));
    
        // if(intakeLimitSwitch.get()){    //NEED to check whether to add !
        //     resetEncoder();
        // }
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }

    
    public void setMovingManually(boolean value) {
        isMovingManually.set(value);
    }
    public boolean isMovingManually() {
        return isMovingManually.get();
    }


    public void setTargetPosition(double positionRadians) {
        controller.setSetpoint(positionRadians);
    }

    public double getTargetPosition() {
        return controller.getSetpoint();
    }

    public void resetEncoder() {
        motor.setPosition(0);
    }

    public double getEncoderPosition() {
        double position = motor.getPosition();
        encoderPositionWriter.write(position);
        return position;
    }

    public double getEncoderVelocity() {
        double velocity = motor.getVelocity();
        encoderVelocityWriter.write(velocity);
        return velocity;
    }

    public void setVoltage(double voltage) {
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
   
    public double getSpeed(){
        return motor.getSpeed();
    }
    public SafeTalonFX getMotor(){
        return motor;
    }
    // public double getDistance(){
    //     return motor.getPosition()
    // }
}  