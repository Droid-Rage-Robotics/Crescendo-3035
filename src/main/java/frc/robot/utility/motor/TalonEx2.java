package frc.robot.utility.motor;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class TalonEx2 extends CANMotorEx2 {
    private final TalonFX motor;

    private TalonEx2(TalonFX motor) {
        this.motor = motor;
    }

    public static TalonEx2 create(int deviceID) {
        return new TalonEx2(new TalonFX(deviceID));
    }

    public static TalonEx2 create(int deviceID, String canbus) {
        return new TalonEx2(new TalonFX(deviceID, canbus));
    }

    @Override
    public void setPower(double power) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setPower'");
    }

    @Override
    public void setVoltage(double outputVolts) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setVoltage'");
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
