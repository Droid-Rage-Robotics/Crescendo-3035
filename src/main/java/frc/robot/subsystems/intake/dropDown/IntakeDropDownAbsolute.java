package frc.robot.subsystems.intake.dropDown;

import com.revrobotics.SparkAbsoluteEncoder;

import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeDropDownAbsolute extends IntakeDropDownMotionProfiled {
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI / 2;  //90 Degree
    }
    SparkAbsoluteEncoder absoluteEncoder;
    protected final ShuffleboardValue<Double> rawEncoderPositionWriter = ShuffleboardValue.create(0.0, "Raw Encoder Position (Degrees)", IntakeDropDown.class.getSimpleName())
        .withSize(1, 2)
        .build();
    public IntakeDropDownAbsolute(Boolean isEnabled) {
        super(isEnabled);
        absoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        absoluteEncoder.setPositionConversionFactor(Math.PI * 2);
        absoluteEncoder.setVelocityConversionFactor(Math.PI * 2 / 60);
        absoluteEncoder.setInverted(true);
        setTargetPosition(Constants.OFFSET);
    }
    
    
    @Override
    public double getEncoderPosition() {
        double position = (absoluteEncoder.getPosition() + Constants.OFFSET) % Constants.RADIANS_PER_ROTATION;
        encoderPositionWriter.write(Math.toDegrees(position));
        getRawEncoderPositions();
        return position;
    }

    public void getRawEncoderPositions() {
        double position = (absoluteEncoder.getPosition());
        rawEncoderPositionWriter.write(Math.toDegrees(position));
    }

    @Override
    public double getEncoderVelocity() {
        double velocity = absoluteEncoder.getVelocity();
        encoderVelocityWriter.write(velocity);
        return velocity;
    }

    @Override
    public void resetEncoder() {
        absoluteEncoder.setZeroOffset(0);
        motor.burnFlash();
    }
}