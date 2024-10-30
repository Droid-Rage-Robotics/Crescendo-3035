package frc.robot.utility.motor.old;

import frc.robot.utility.shuffleboard.ShuffleboardValue;

public abstract class SafeMotor {
    protected final ShuffleboardValue<Boolean> isEnabled;
    protected final ShuffleboardValue<Double> outputWriter;
    public SafeMotor(ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter) {
        this.isEnabled = isEnabled;
        this.outputWriter = outputWriter;
    }

    public enum IdleMode {
        Coast,
        Brake
    }
    public abstract void setPower(double power);
    public abstract void setVoltage(double outputVolts);
    // public abstract void setInverted(boolean isInverted);
    public abstract void setIdleMode(IdleMode mode);
    public abstract double getVelocity();
    

    // public abstract void setPositionConversionFactor(double num);
    public abstract double getPosition();
    // public abstract void set(double num);
    public abstract int getDeviceID();
    public void stop() {
        setPower(0);
    }
    public abstract double getSpeed();
    // public abstract void setVelocityConversionFactor(double num);
    // public abstract boolean getMotorFault();
}
