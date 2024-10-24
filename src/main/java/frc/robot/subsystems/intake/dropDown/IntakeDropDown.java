package frc.robot.subsystems.intake.dropDown;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.general.DisabledCommand;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.motor.old.SafeTalonFX;
import frc.robot.utility.motor.old.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeDropDown extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 2;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;//4089
        public static final double ROTATIONS_TO_RADIANS = (2 * Math.PI / READINGS_PER_REVOLUTION)*2; //<--THIS WORK; cause gear ratio: (2*Math.PI)/Constants.GEAR_RATIO
    
    }


    protected final SafeTalonFX motor;
    protected final PIDController controller;
    protected ArmFeedforward feedforward;

    protected final ShuffleboardValue<Double> rawPosWriter = ShuffleboardValue
        .create(0.0, "DropDown/Pos/Raw", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> radianPosWriter = 
        ShuffleboardValue.create(0.0, "DropDown/Pos/Radian", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> degreePosWriter = ShuffleboardValue
        .create(0.0, "DropDown/Pos/Degree", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    
        protected final ShuffleboardValue<Double> encoderVelocityWriter = 
        ShuffleboardValue.create(0.0, "DropDown/ Encoder Velocity (Radians per Second)", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    
    protected final ShuffleboardValue<Double> degreeTargetPosWriter = ShuffleboardValue
        .create(0.0, "DropDown/Target/Degree", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> radianTargetPosWriter = ShuffleboardValue
        .create(0.0, "DropDown/Target/Radian", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> rawTargetPosWriter = ShuffleboardValue
        .create(0.0, "DropDown/Target/Raw", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    

    protected final ShuffleboardValue<Boolean> isMovingManually = 
        ShuffleboardValue.create(false, "DropDown/ Moving manually", Intake.class.getSimpleName())
        .build();
    
    public IntakeDropDown(Boolean isEnabled) {
        motor = new SafeTalonFX(
            17,
            true,
            IdleMode.Coast,
            Constants.ROTATIONS_TO_RADIANS,
            Constants.ROTATIONS_TO_RADIANS,
            ShuffleboardValue.create(isEnabled, "DropDown/Motor/Drop Is Enabled", Intake.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "DropDown/Motor/Drop Voltage", Intake.class.getSimpleName())
                .build(),
                40
        );

        controller = new PIDController(1, 0.0, 0.0);//0.62
        // controller = new PIDController(0, 0.0, 0.0);

        controller.setTolerance(Math.toRadians(1));

        feedforward = new ArmFeedforward(0.452,.65,.0859,.003); //SysID with minor changes
        // feedforward = new ArmFeedforward(0.453,.65,.0859,.0035872); //SysID with just motor 



        ComplexWidgetBuilder.create(controller, "Drop PID", Intake.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 1);

        ComplexWidgetBuilder.create(
            DisabledCommand.create(runOnce(this::resetEncoder)),
             "Reset Intake Drop Encoder", Intake.class.getSimpleName());
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
        motor.setPosition(0);
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
    public SafeTalonFX getMotor(){
        return motor;
    }

    public double getEncoderPosition() {
        double position = motor.getPosition();
        rawPosWriter.write(position);
        return position;
    }

    public void setDropIdleMode(IdleMode mode){
        motor.setIdleMode(mode);
    }

    public void setMotorMode(IdleMode mode){
        motor.setIdleMode(mode);
    }
}  