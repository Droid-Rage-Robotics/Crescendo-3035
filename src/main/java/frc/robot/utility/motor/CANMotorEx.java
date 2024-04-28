package frc.robot.utility.motor;

public abstract class CANMotorEx {
    // protected int deviceID; // specific and should not be in the abstract class
    // TODO: which fields should be final?
    protected Direction direction;
    protected IdleMode idleMode;
    protected double positionConversionFactor;
    protected double velocityConversionFactor;
    // protected final ShuffleboardValue<Boolean> isEnabled;
    // protected final ShuffleboardValue<Double> outputWriter;
    
    public enum Direction {
        Forward,
        Reversed,
    }

    public enum IdleMode {
        Break,
        Coast,
    }

    // public MotorEx(ShuffleboardValue<Boolean> isEnabled, 
    //     ShuffleboardValue<Double> outputWriter) {
    //     this.isEnabled = isEnabled;
    //     this.outputWriter = outputWriter;
    // }

    public class DirectionBuilder {
        public IdleModeBuilder withDirection(Direction direction) {
            setDirection(direction);
            return new IdleModeBuilder();
        }
    }
    public class IdleModeBuilder {
        public PositionConversionFactorBuilder withIdleMode(IdleMode idleMode) {
            setIdleMode(idleMode);
            return new PositionConversionFactorBuilder();
        }
    }
    public class PositionConversionFactorBuilder {
        public VelocityConversionFactorBuilder withPositionConversionFactor(double positionConversionFactor) {
            setPositionConversionFactor(positionConversionFactor);
            return new VelocityConversionFactorBuilder();
        }
    }
    public class VelocityConversionFactorBuilder {
        public <T extends CANMotorEx> T withVelocityConversionFactor(double velocityConversionFactor) {
            setPositionConversionFactor(positionConversionFactor);
            return (T) CANMotorEx.this;
        }
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
