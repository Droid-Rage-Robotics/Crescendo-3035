package frc.robot.subsystems.intake.dropDown;

import com.revrobotics.SparkAbsoluteEncoder;

import frc.robot.subsystems.claw.Claw;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class IntakeDropDownAbsolute extends IntakeDropDown {
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
        absoluteEncoder.setInverted(false);
        setTargetPosition(Constants.OFFSET);
    }

    public void periodic(){
        getEncoderPosition();
        // setVoltage(calculatePID(getTargetPosition()));
        setVoltage(calculatePID(getTargetPosition())+calculateFeedforward(getEncoderPosition(), getEncoderVelocity()));
    }
    
    
    @Override
    public double getEncoderPosition() {
        double radianPos = (absoluteEncoder.getPosition() + Constants.OFFSET) % Constants.RADIANS_PER_ROTATION;
        // double radianPos = (absoluteEncoder.getPosition());
        radianPosWriter.write(radianPos);
        degreePosWriter.write(Math.toDegrees(radianPos));
        double raw = (absoluteEncoder.getPosition());
        rawEncoderPositionWriter.write((raw));
        return radianPos;
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