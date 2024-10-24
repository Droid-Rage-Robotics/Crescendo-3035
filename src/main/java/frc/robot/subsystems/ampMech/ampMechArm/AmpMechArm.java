package frc.robot.subsystems.ampMech.ampMechArm;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.general.DisabledCommand;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.utility.motor.old.SafeCanSparkMax;
import frc.robot.utility.motor.old.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class AmpMechArm extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 2;//not right
        public static final double READINGS_PER_REVOLUTION = 1;//4089
        public static final double ROTATIONS_TO_RADIANS = (2 * Math.PI / READINGS_PER_REVOLUTION)*2; //<--THIS WORK; cause gear ratio: (2*Math.PI)/Constants.GEAR_RATIO
    }


    protected final SafeCanSparkMax motor;
    protected final PIDController controller;
    protected ArmFeedforward feedforward;

    protected final ShuffleboardValue<Double> rawPosWriter = ShuffleboardValue
        .create(0.0, "Arm/Pos/Raw", AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> radianPosWriter = 
        ShuffleboardValue.create(0.0, "Arm/Pos/Radian", AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> degreePosWriter = ShuffleboardValue
        .create(0.0, "Arm/Pos/Degree", AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    
    protected final ShuffleboardValue<Double> encoderVelocityWriter = 
        ShuffleboardValue.create(0.0, "Arm/ Encoder Velocity (Radians per Second)", 
            AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    
    protected final ShuffleboardValue<Double> degreeTargetPosWriter = ShuffleboardValue
        .create(0.0, "Arm/Target/Degrees", AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> radianTargetPosWriter = ShuffleboardValue
        .create(0.0, "Arm/Target/Radians", AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> rawTargetPosWriter = ShuffleboardValue
        .create(0.0, "Arm/Target/Raws", AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    

    protected final ShuffleboardValue<Boolean> isMovingManually = 
        ShuffleboardValue.create(false, "Arm/ Moving manually", 
            AmpMech.class.getSimpleName())
        .build();
    
    public AmpMechArm(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            23, 
            MotorType.kBrushless,
            true,
            IdleMode.Brake,
            Constants.ROTATIONS_TO_RADIANS,
            1.0,
            ShuffleboardValue.create(isEnabled, "Arm/Arm Is Enabled", AmpMech.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "Arm/Arm Voltage", AmpMech.class.getSimpleName())
                .build()
        );

        controller = new PIDController(3.5, 0.0, 0.0);//1.6
        // controller = new PIDController(0, 0.0, 0.0);

        controller.setTolerance(Math.toRadians(1));

        feedforward = new ArmFeedforward(0.46,.655,.0859,.003587);


        ComplexWidgetBuilder.create(controller, "Arm PID", AmpMech.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 1);

        ComplexWidgetBuilder.create(
            DisabledCommand.create(runOnce(this::resetEncoder)),
             "Reset Arm Encoder", AmpMech.class.getSimpleName());
    }

    @Override
    public void periodic() {
        getEncoderPosition();
        // setVoltage(calculatePID(getEncoderPosition()));
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


    public void setTargetPosition(double posDegree) {
        radianTargetPosWriter.set(Math.toRadians(posDegree));
        degreeTargetPosWriter.set(posDegree);
        rawTargetPosWriter.set(posDegree);
        // rawTargetPosWriter.set(posDegree/Constants.DEGREES_PER_ROTATION) // Not for motor encoder???
        controller.setSetpoint(Math.toRadians(posDegree));
    }

    public double getTargetPosition() {
        return controller.getSetpoint();
    }

    public void resetEncoder() {
        // motor.setPosition(0);
    }

    public double getMotorPos() {
        double position = motor.getPosition();
        rawPosWriter.write(position);
        return position;
    }

    public double getEncoderVelocity() {
        double velocity = motor.getVelocity();
        encoderVelocityWriter.write(velocity);
        return velocity;
    }

    public void setVoltage(double voltage) {
        motor.setVoltage(-voltage); //TODO: FIx this to be better
    }

    protected void stop() {
        motor.stop();
    }

    protected double calculateFeedforward(double positionRadians, double velocity) {
        return feedforward.calculate(positionRadians, velocity);
    }

    protected double calculatePID(double pos) {
        return controller.calculate(pos);
    }
   
    public double getSpeed(){
        return motor.getSpeed();
    }
    public SafeCanSparkMax getMotor(){
        return motor;
    }

    public double getEncoderPosition() {
        double position = motor.getPosition();
        rawPosWriter.write(position);
        return position;
    }
}  