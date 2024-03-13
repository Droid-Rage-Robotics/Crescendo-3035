package frc.robot.subsystems.ampMech.ampMechPivot;

import com.revrobotics.SparkAbsoluteEncoder;

import frc.robot.utility.motor.SafeCanSparkMax;

public class AmpMechPivotAbsolute extends AmpMechPivot {
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI;  //90 Degree
    }
    SparkAbsoluteEncoder absoluteEncoder;
    public AmpMechPivotAbsolute(Boolean isEnabled, SafeCanSparkMax sparkMax) {
        super(isEnabled);
        absoluteEncoder = sparkMax.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        
        absoluteEncoder.setPositionConversionFactor(Math.PI * 2);
        absoluteEncoder.setVelocityConversionFactor(Math.PI * 2 / 60);
        absoluteEncoder.setInverted(false);
        // setTargetPosition(180);
    }

    public void periodic(){
        // setVoltage(calculatePID(getEncoderPosition()));
        setVoltage(calculatePID(getEncoderPosition())*5);//+(Math.cos(getEncoderPosition()-(Math.PI/2))*(.2)));//.175
        // setVoltage(calculatePID(getEncoderPosition())+calculateFeedforward(getEncoderPosition(), 0));
    }
    
    @Override
    public double getEncoderPosition() {
        double radianPos = (absoluteEncoder.getPosition() + Constants.OFFSET) % Constants.RADIANS_PER_ROTATION;
        radianPosWriter.write(radianPos);
        // degreePosWriter.write(Math.toDegrees(radianPos));
        radianPosWriter.write(radianPos);
        degreePosWriter.write(Math.toDegrees(radianPos));
        double raw = (absoluteEncoder.getPosition());
        rawPosWriter.write(raw);
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