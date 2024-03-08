package frc.robot.subsystems.intake.dropDown;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.SparkAbsoluteEncoder;

import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeDropDownAbs extends IntakeDropDown {
    public enum Thing{
        DEGREE,
        RADIAN
    }
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI*2;
        public static double DEGREES_PER_ROTATION = 360;
        public static double OFFSET = 0;
    }

    DutyCycleEncoder absoluteEncoder;

    protected final ShuffleboardValue<Double> rawEncoderPositionWriter = ShuffleboardValue
        .create(0.0, "Raw Encoder Position ()", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> degreeTargetPosWriter = ShuffleboardValue
        .create(0.0, "Degree Target Pos ()", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> degreePositionWriter = ShuffleboardValue
        .create(0.0, "Encoder  Position (Degrees)", Intake.class.getSimpleName())
        .withSize(1, 2)
        .build();

    public IntakeDropDownAbs(Boolean isEnabled) {
        super(isEnabled);
        /*//If It is connected to a Spark Max
        absoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        absoluteEncoder.setPositionConversionFactor(Constants.RADIANS_PER_ROTATION);
        absoluteEncoder.setVelocityConversionFactor(Constants.RADIANS_PER_ROTATION / 60); // the 60 is seconds cause the encoder position gets updated once a second
        absoluteEncoder.setInverted(true);*/

        absoluteEncoder = new DutyCycleEncoder(0); //What chnnel is it plugged into
        // absoluteEncoder.setPositionOffset(Math.toRadians(0)/Constants.RADIANS_PER_ROTATION);
    }
    
    @Override
    public void periodic(){
        getEncoderPosition();
        setVoltage( calculatePID(getTargetPosition()));
        // setVoltage( calculatePID(getTargetPosition()));

        
    }

    @Override
    public double getEncoderPosition() {
        double degreePos = (absoluteEncoder.getAbsolutePosition()*(Constants.DEGREES_PER_ROTATION));
        degreePositionWriter.write(((degreePos)));
        double radianPos = (absoluteEncoder.getAbsolutePosition()*(Constants.RADIANS_PER_ROTATION));
        encoderPositionWriter.write(((radianPos)));

        getRawEncoderPositions();
        return radianPos;
        // return degreePos;
    }

    public void getRawEncoderPositions() {
        double position = (absoluteEncoder.getAbsolutePosition());
        rawEncoderPositionWriter.write((position));
    }

    @Override
    public double getEncoderVelocity() {
        // double velocity = absoluteEncoder.getVelocity();
        double velocity = motor.getVelocity();  //Encoder doesn't read velocity
        encoderVelocityWriter.write(velocity);
        return velocity;
    }

    // @Override
    // public void resetEncoder() {
    //     absoluteEncoder.setPositionOffset(0);
    // }

    @Override 
    public void setTargetPosition(double posDegrees){
        // requiredPositionWriter.set(((posDegrees)*(Math.PI*2))/(360));//Radians??
        // degreeTargetPosWriter.set(((posDegrees))/(Constants.DEGREES_PER_ROTATION));//Rotations?
        // controller.setSetpoint(posDegrees/(Constants.DEGREES_PER_ROTATION));

        controller.setSetpoint(Math.toRadians(posDegrees));
        // controller.setSetpoint(posDegrees);
    }

    @Override
    protected double calculatePID(double positionDegree) {
        // return controller.calculate(getEncoderPosition(),Math.toRadians(positionDegree));
        return controller.calculate(getEncoderPosition(),(positionDegree));

    }
}