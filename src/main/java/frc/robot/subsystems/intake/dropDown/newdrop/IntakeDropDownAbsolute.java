package frc.robot.subsystems.intake.dropDown.newdrop;

import com.revrobotics.SparkAbsoluteEncoder;

import frc.robot.subsystems.claw.Claw;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeDropDownAbsolute extends IntakeDropDownNEW {
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI / 2;  //90 Degree
    }
    SparkAbsoluteEncoder absoluteEncoder;
    protected final ShuffleboardValue<Double> rawEncoderPositionWriter = 
        ShuffleboardValue.create(0.0, "Raw Encoder Position (Degrees)", 
        Claw.class.getSimpleName())
        .withSize(1, 2)
        .build();
    public IntakeDropDownAbsolute(Boolean isEnabled, SafeCanSparkMax sparkMax) {
        super(isEnabled);
        absoluteEncoder = sparkMax.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        absoluteEncoder.setPositionConversionFactor(Math.PI * 2);
        absoluteEncoder.setVelocityConversionFactor(Math.PI * 2 / 60);
        absoluteEncoder.setInverted(true);
        setTargetPosition(Constants.OFFSET);
    }
    
    
    @Override
    public double getEncoderPosition() {
        double position = (absoluteEncoder.getPosition() + Constants.OFFSET) % Constants.RADIANS_PER_ROTATION;
        degreePosWriter.write(Math.toDegrees(position));
        getRawEncoderPositions();
        return position;
    }

    public void getRawEncoderPositions() {
        double position = (absoluteEncoder.getPosition());
        rawEncoderPositionWriter.write((position));
    }

    @Override
    public double getEncoderVelocity() {
        double velocity = absoluteEncoder.getVelocity();
        encoderVelocityWriter.write(velocity);
        return velocity;
    }

    // @Override
    // public void resetEncoder() {
    //     absoluteEncoder.setZeroOffset(0);
    // }
}