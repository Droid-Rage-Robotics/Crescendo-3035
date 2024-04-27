package frc.robot.utility.motor;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class TalonEx extends CANMotorEx {
    private final TalonFX motor;

    private TalonEx(TalonFX motor) {
        this.motor = motor;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPosition'");
    }

    @Override
    public int getDeviceID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDeviceID'");
    }
}
