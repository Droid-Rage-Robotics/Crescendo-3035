package frc.robot.utility.motor;

import frc.robot.utility.shuffleboard.ShuffleboardValue;

public abstract class CANMotorEx2 {
    protected Direction direction;
    protected IdleMode idleMode;
    protected double positionConversionFactor;
    protected double velocityConversionFactor;

    public enum Direction {
        Forward,
        Reversed,
    }

    public enum IdleMode {
        Break,
        Coast,
    }

    public <T extends CANMotorEx2> T withDirection(Direction direction) {
        setDirection(direction);
        return (T) this;
    }

    public <T extends CANMotorEx2> T withIdleMode(IdleMode idleMode) {
        setIdleMode(idleMode);
        return (T) this;
    }

    public <T extends CANMotorEx2> T withPositionConversionFactor(double positionConversionFactor) {
        setPositionConversionFactor(positionConversionFactor);
        return (T) this;
    }

    public <T extends CANMotorEx2> T withVelocityConversionFactor(double velocityConversionFactor) {
        setVelocityConversionFactor(velocityConversionFactor);
        return (T) this;
    }

    public abstract void setPower(double power);
    public abstract void setVoltage(double outputVolts);

    public abstract void setDirection(Direction direction);
    public abstract void setIdleMode(IdleMode mode);
    public abstract void setPositionConversionFactor(double positionConversionFactor);
    public abstract void setVelocityConversionFactor(double velocityConversionFactor);
    
    public abstract double getVelocity();
    public abstract double getPosition();
    public abstract int getDeviceID();

    public void stop() {
        setPower(0);
    }

}
