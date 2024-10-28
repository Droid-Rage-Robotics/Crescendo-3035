package frc.robot.utility.encoder.old;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeCancoder{
    public enum EncoderRange {
        ZERO_TO_ONE,
        PLUS_MINUS_HALF
    }

    //Can use in relative and Absolute Encoder
    private final CANcoder encoder;
    private CANcoderConfiguration canConfiguration;
    private double positionConversionFactor, velocityConversionFactor;

    public SafeCancoder(int deviceNumber, boolean isInverted, 
        EncoderRange range, double offset, double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Double> positionWriter
        ) {
            encoder = new CANcoder(deviceNumber);
            if(isInverted){
                canConfiguration.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
            } else{
                canConfiguration.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
            }
            switch (range) {
                case PLUS_MINUS_HALF:
                canConfiguration.MagnetSensor.AbsoluteSensorRange= AbsoluteSensorRangeValue.Signed_PlusMinusHalf;
                    break;
                default:
                case ZERO_TO_ONE:
                canConfiguration.MagnetSensor.AbsoluteSensorRange= AbsoluteSensorRangeValue.Unsigned_0To1;
                    break;
            }
            canConfiguration.MagnetSensor.MagnetOffset = offset;
            this.positionConversionFactor = positionConversionFactor;
            this.velocityConversionFactor = positionConversionFactor/60;
            
            // encoder.getConfigurator() = canConfiguration;
            // encoder.setCon
    }
    public double getVelocity() {
        return encoder.getVelocity().getValueAsDouble()*velocityConversionFactor;
    }

    public double getPosition() {
        return encoder.getPosition().getValueAsDouble()*positionConversionFactor;
    }

    public void setPosition(double position) {
        encoder.setPosition(position);
    }
    public double getAbsolutePosition() {
        return encoder.getAbsolutePosition().getValueAsDouble();
    }



    // @Override
    public int getDeviceID(){
        return encoder.getDeviceID();
    }
}