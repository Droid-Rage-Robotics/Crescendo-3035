package frc.robot.utility.motor;

import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;

public class SafeSparkAbsoluteEncoder {
    private SparkAbsoluteEncoder encoder;

    //Math.PI*2 = 360 Degrees
    //Math.PI*2/60
    public SafeSparkAbsoluteEncoder(SafeCanSparkMax motor, boolean isInverted, double positionConversionFactor, double velocityConversionFactor){
        encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
        encoder.setInverted(isInverted);
        encoder.setPositionConversionFactor(positionConversionFactor);
        encoder.setVelocityConversionFactor(velocityConversionFactor);
    } 
    public double getPosition() {
        return encoder.getPosition();
    }
    public double getDegrees() {
        return encoder.getPosition()*(Math.PI*2);
    }
    public double getRadian() {
        return encoder.getPosition();
    }
    public double getVelocity() {
        return encoder.getVelocity();  
    }
    public void setPositionConversionFactor(double positionConversionFactor) {
        encoder.setPositionConversionFactor(positionConversionFactor);
    }
    public void setVelocityConversionFactor(double velocityConversionFactor) {
        encoder.setVelocityConversionFactor(velocityConversionFactor);
    }
}