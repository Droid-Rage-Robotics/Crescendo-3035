package frc.robot.utility.motor;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class TalonEx extends CANMotorEx {
    private final TalonFX motor;
    private TalonFXConfiguration configuration = new TalonFXConfiguration();
    

    private TalonEx(TalonFX motor) {
        this.motor = motor;
        // motor.getConfigurator().apply(configuration);
    }

    public static DirectionBuilder create(int deviceID) {
        CANMotorEx motor = new TalonEx(new TalonFX(deviceID));
        return motor.new DirectionBuilder();
    }

    @Override
    public void setPower(double power) {
        motor.set(power);
    }

    @Override
    public void setVoltage(double outputVolts) {
        motor.setVoltage(outputVolts);
    }

    @Override
    public void setDirection(Direction direction) {
        motor.setInverted(switch (direction) {
                case Forward -> false;
                case Reversed -> true;
            });
    }

    @Override
    public void setIdleMode(IdleMode mode) {
        motor.setNeutralMode(switch (mode) {
            case Break -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
    }

    @Override
    public void setPositionConversionFactor(double positionConversionFactor) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setPositionConversionFactor'");
    }

    @Override
    public void setVelocityConversionFactor(double velocityConversionFactor) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setVelocityConversionFactor'");
    }

    @Override
    public double getVelocity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVelocity'");
    }

    @Override
    public double getPosition() {
        return motor.getPosition().getValueAsDouble();
    }

    @Override
    public int getDeviceID() {
        return motor.getDeviceID();
    }

    @Override
    public void setSupplyCurrentLimit(double currentLimit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentLimit'");
    }

    public TalonEx withStatorCurrentLimit(double statorCurrent){
        setStatorCurrentLimit(statorCurrent);
        return this;
    }
    public void setStatorCurrentLimit(double statorCurrent){
        configuration.CurrentLimits.StatorCurrentLimit = statorCurrent;
        configuration.CurrentLimits.StatorCurrentLimitEnable = true;
        motor.getConfigurator().apply(configuration);
    }

    @Override
    public void resetEncoder(int num) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetEncoder'");
    }
}
