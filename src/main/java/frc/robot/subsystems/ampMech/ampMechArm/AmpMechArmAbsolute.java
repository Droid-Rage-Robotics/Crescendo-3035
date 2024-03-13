package frc.robot.subsystems.ampMech.ampMechArm;

import com.revrobotics.SparkAbsoluteEncoder;

public class AmpMechArmAbsolute extends AmpMechArm {
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI / 2;  //90 Degree
    }
    SparkAbsoluteEncoder absoluteEncoder;
    public AmpMechArmAbsolute(Boolean isEnabled) {
        super(isEnabled);
        absoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        
        absoluteEncoder.setPositionConversionFactor(Math.PI * 2);
        absoluteEncoder.setVelocityConversionFactor(Math.PI * 2 / 60);
        absoluteEncoder.setInverted(false);
        // setTargetPosition(Constants.OFFSET);
    }

    public void periodic(){
        // setVoltage(calculatePID(getEncoderPosition()));
        setVoltage((calculatePID(getEncoderPosition())*1.2)+(Math.cos(getEncoderPosition())*(.21)));//.175
        // setVoltage(calculatePID(getEncoderPosition())+calculateFeedforward(getEncoderPosition(), 0));
    }
    
    @Override
    public double getEncoderPosition() {
        double radianPos = (absoluteEncoder.getPosition() + Constants.OFFSET) % Constants.RADIANS_PER_ROTATION;
        radianPosWriter.write(radianPos);
        degreePosWriter.write(Math.toDegrees(radianPos));
        double raw = (absoluteEncoder.getPosition());
        rawPosWriter.write((raw));
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