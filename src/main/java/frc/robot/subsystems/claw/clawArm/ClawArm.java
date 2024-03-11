package frc.robot.subsystems.claw.clawArm;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class ClawArm extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 2;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;//4089
        public static final double ROTATIONS_TO_RADIANS = (2 * Math.PI / READINGS_PER_REVOLUTION)*2; //<--THIS WORK; cause gear ratio: (2*Math.PI)/Constants.GEAR_RATIO
    
    }


    protected final SafeCanSparkMax motor;
    protected final PIDController controller;
    protected ArmFeedforward feedforward;

    protected final ShuffleboardValue<Double> rawPosWriter = ShuffleboardValue
        .create(0.0, "Arm/Pos/Raw", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> radianPosWriter = 
        ShuffleboardValue.create(0.0, "Arm/Pos/Radian", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> degreePosWriter = ShuffleboardValue
        .create(0.0, "Arm/Pos/Degree", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    
        protected final ShuffleboardValue<Double> encoderVelocityWriter = 
        ShuffleboardValue.create(0.0, "Arm/ Encoder Velocity (Radians per Second)", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    
    protected final ShuffleboardValue<Double> degreeTargetPosWriter = ShuffleboardValue
        .create(0.0, "Arm/Target/Degree", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> radianTargetPosWriter = ShuffleboardValue
        .create(0.0, "Arm/Target/Radian", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> rawTargetPosWriter = ShuffleboardValue
        .create(0.0, "Arm/Target/Raw", Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    

    protected final ShuffleboardValue<Boolean> isMovingManually = 
        ShuffleboardValue.create(false, "Arm/ Moving manually", Claw.class.getSimpleName())
        .build();
    
    public ClawArm(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            23, 
            MotorType.kBrushless,
            true,
            IdleMode.Coast,
            Constants.ROTATIONS_TO_RADIANS,
            1.0,
            ShuffleboardValue.create(isEnabled, "Arm/Arm Is Enabled", Claw.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "Arm/Arm Voltage", Claw.class.getSimpleName())
                .build()
        );

        controller = new PIDController(1.9, 0.0, 0.0);//1.1
        // controller = new PIDController(0, 0.0, 0.0);

        controller.setTolerance(Math.toRadians(1));

        // feedforward = new ArmFeedforward(0.453,.65,.0859,.0035872); //SysID with just motor - may 

        // feedforward = new ArmFeedforward(0.,.60679,.085861,.0035872);//Make some 0 testing
        feedforward = new ArmFeedforward(0,0,0);


        ComplexWidgetBuilder.create(controller, "Arm PID", Claw.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 1);

        ComplexWidgetBuilder.create(
            DisabledCommand.create(runOnce(this::resetEncoder)),
             "Reset Arm Encoder", Claw.class.getSimpleName());
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