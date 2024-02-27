package frc.robot.subsystems.intake.dropDown;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.SparkAbsoluteEncoder;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeDropDownAbsolute extends IntakeDropDownMotionProfiled {
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI / 2;  //90 Degree
    }
    DutyCycleEncoder absoluteEncoder;
    protected final ShuffleboardValue<Double> rawEncoderPositionWriter = ShuffleboardValue.create(0.0, "Raw Encoder Position (Degrees)", IntakeDropDown.class.getSimpleName())
        .withSize(1, 2)
        .build();
    public IntakeDropDownAbsolute(Boolean isEnabled) {
        super(isEnabled);
        /*//If It is connected to a Spark Max
        absoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        absoluteEncoder.setPositionConversionFactor(Constants.RADIANS_PER_ROTATION);
        absoluteEncoder.setVelocityConversionFactor(Constants.RADIANS_PER_ROTATION / 60);
        absoluteEncoder.setInverted(true);*/
        absoluteEncoder = new DutyCycleEncoder(2); //What chnnel is it plugged into
        absoluteEncoder.setDistancePerRotation(Constants.RADIANS_PER_ROTATION); //Not sure if it is the right //#
        absoluteEncoder.setPositionOffset(getTargetPosition());

        
        setTargetPosition(Constants.OFFSET);
    }
    
    
    @Override
    public double getEncoderPosition() {
        double position = (absoluteEncoder.getAbsolutePosition() + Constants.OFFSET) % Constants.RADIANS_PER_ROTATION;
        encoderPositionWriter.write(Math.toDegrees(position));
        getRawEncoderPositions();
        return position;
    }

    public void getRawEncoderPositions() {
        double position = (absoluteEncoder.getAbsolutePosition());
        rawEncoderPositionWriter.write(Math.toDegrees(position));
    }

    @Override
    public double getEncoderVelocity() {
        // double velocity = absoluteEncoder.getVelocity();
        double velocity = motor.getVelocity();  //Encoder doesn't read velocity
        encoderVelocityWriter.write(velocity);
        return velocity;
    }

    @Override
    public void resetEncoder() {
        absoluteEncoder.setPositionOffset(0);
    }
}