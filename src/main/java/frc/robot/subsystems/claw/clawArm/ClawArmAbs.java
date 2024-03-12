package frc.robot.subsystems.claw.clawArm;

import com.revrobotics.SparkAbsoluteEncoder;


public class ClawArmAbs extends ClawArm {
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI / 2;  //340 Degree didied by 1 rotaiton
        //Is 90 so the top is 90 instead of 0
    }
    SparkAbsoluteEncoder absoluteEncoder;
    public ClawArmAbs(Boolean isEnabled) {
        super(isEnabled);
        absoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        absoluteEncoder.setPositionConversionFactor(Constants.RADIANS_PER_ROTATION);
        absoluteEncoder.setVelocityConversionFactor(Constants.RADIANS_PER_ROTATION/ 60);
        absoluteEncoder.setInverted(false);
        // absoluteEncoder.setZeroOffset(Constants.OFFSET);
        setTargetPosition(Constants.OFFSET);
    }
    
    @Override
    public void periodic() {
        getEncoderVelocity();
        getEncoderPosition();
        setVoltage(calculatePID(getTargetPosition()));
    }
    
    @Override
    public double getEncoderPosition() {
        double position = (absoluteEncoder.getPosition() + Constants.OFFSET) % Constants.RADIANS_PER_ROTATION;
        degreePosWriter.write(Math.toDegrees(position));
        radianPosWriter.write(position);
        getRawEncoderPositions();
        return position;
    }

    public void getRawEncoderPositions() {
        double position = (absoluteEncoder.getPosition());
        encoderPositionWriter.write(Math.toDegrees(position));
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
    //     motor.burnFlash();
    // }
}