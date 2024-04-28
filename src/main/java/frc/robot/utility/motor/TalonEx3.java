package frc.robot.utility.motor;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class TalonEx3 {
    private final TalonFX motor;

    private TalonEx3(TalonFX motor) {
        this.motor = motor;
    }

    public static MotorProperties<TalonEx3>.DirectionBuilder create(int deviceID) {
        TalonEx3 motor = new TalonEx3(new TalonFX(deviceID));
        return MotorProperties.create(
            motor, 
            motor::setDirection, 
            motor::setIdleMode, 
            motor::setPositionConversionFactor, 
            motor::setVelocityConversionFactor
        );
    }

    public void setPower(double power) {
        motor.set(power);
    }

    public void setVoltage(double outputVolts) {
        motor.setVoltage(outputVolts);
    }

    public void setDirection(MotorProperties.Direction direction) {
        motor.setInverted(switch (direction) {
                case Forward -> false;
                case Reversed -> true;
            });
    }

    public void setIdleMode(MotorProperties.IdleMode mode) {
        motor.setNeutralMode(switch (mode) {
            case Break -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
    }

    public void setPositionConversionFactor(double positionConversionFactor) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setPositionConversionFactor'");
    }

    public void setVelocityConversionFactor(double velocityConversionFactor) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setVelocityConversionFactor'");
    }
}
