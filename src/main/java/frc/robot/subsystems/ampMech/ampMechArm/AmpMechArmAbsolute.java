package frc.robot.subsystems.ampMech.ampMechArm;

import frc.robot.utility.encoder.old.SafeSparkAbsoluteEncoder;
import frc.robot.utility.motor.old.SafeCanSparkMax;

public class AmpMechArmAbsolute extends AmpMechArm {
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI;///2;  //90 Degree
    }
    SafeSparkAbsoluteEncoder absoluteEncoder;
    public AmpMechArmAbsolute(Boolean isEnabled, SafeCanSparkMax sparkMax) {
        super(isEnabled);
        absoluteEncoder = new SafeSparkAbsoluteEncoder(sparkMax, false, this);
        // absoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
       
        // absoluteEncoder.setPositionConversionFactor(Math.PI * 2);
        // absoluteEncoder.setVelocityConversionFactor(Math.PI * 2 / 60);
        // absoluteEncoder.setInverted(false);
        // setTargetPosition(Constants.OFFSET);
    }

    public void periodic(){
        // setVoltage(calculatePID(getEncoderPosition()));
        // setVoltage(calculatePID(getEncoderPosition())+(Math.cos(getRawEncoder())*(.23)));

        // setVoltage((calculatePID(getEncoderPosition())*1.8)+(Math.cos(getRawEncoder())*(.23)));//WORKS
        setVoltage(calculatePID(getEncoderPosition())+calculateFeedforward(controller.getSetpoint(), 0));//WORKS :)
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
    
    public double getRawEncoder() {//FOR The Math.cos()/ kG
        double radianPos = (absoluteEncoder.getPosition() + (Math.PI/2)) % Constants.RADIANS_PER_ROTATION;
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