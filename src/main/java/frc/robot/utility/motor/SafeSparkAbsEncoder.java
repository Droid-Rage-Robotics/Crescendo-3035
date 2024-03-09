package frc.robot.utility.motor;

import com.revrobotics.REVLibError;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;

public class SafeSparkAbsEncoder {
    private SparkAbsoluteEncoder encoder;


    public SafeSparkAbsEncoder(SafeCanSparkMax motor, boolean isInverted, double positionConversionFactor, double velocityConversionFactor){
        encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
        encoder.setInverted(isInverted);
        encoder.setPositionConversionFactor(positionConversionFactor);
        encoder.setVelocityConversionFactor(velocityConversionFactor);
        
    } 
    public double getPosition() {
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