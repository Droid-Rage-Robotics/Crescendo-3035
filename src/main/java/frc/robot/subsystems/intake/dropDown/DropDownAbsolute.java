package frc.robot.subsystems.intake.dropDown;

import com.revrobotics.SparkAbsoluteEncoder;

import frc.robot.utility.shuffleboard.ShuffleboardValue;

//Uses feedforward and controller with absolute encoder
public class DropDownAbsolute extends DropDownMotionProfiled {
    public static class Consants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI / 2;  //90 Degree
    }
    SparkAbsoluteEncoder absoluteEncoder;
    protected final ShuffleboardValue<Double> rawEncoderPositionWriter = ShuffleboardValue.create(0.0, "Raw Encoder Position (Degrees)", DropDown.class.getSimpleName())
        .withSize(1, 2)
        .build();
    public DropDownAbsolute(Boolean isEnabled) {
        super(isEnabled);
        absoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        absoluteEncoder.setPositionConversionFactor(Math.PI * 2);
        absoluteEncoder.setVelocityConversionFactor(Math.PI * 2 / 60);
        absoluteEncoder.setInverted(true);
        setTargetPosition(Consants.OFFSET);
    }
    
    
    @Override
    public double getEncoderPosition() {
        double position = (absoluteEncoder.getPosition() + Consants.OFFSET) % Consants.RADIANS_PER_ROTATION;
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