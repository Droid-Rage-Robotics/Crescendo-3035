package frc.robot.utility.motor;

import frc.robot.utility.shuffleboard.ShuffleboardValue;

public abstract class CANMotorEx {
    // protected int deviceID; // specific and should not be in the abstract class
    // TODO: which fields should be final?
    protected Direction direction;
    protected IdleMode idleMode;
    protected double positionConversionFactor;
    protected double velocityConversionFactor;
    // protected final ShuffleboardValue<Boolean> isEnabled;
    // protected final ShuffleboardValue<Double> outputWriter;
    
    enum Direction {
        Forward,
        Reversed,
    }

    enum IdleMode {
        Break,
        Coast,
    }

    // public MotorEx(ShuffleboardValue<Boolean> isEnabled, 
    //     ShuffleboardValue<Double> outputWriter) {
    //     this.isEnabled = isEnabled;
    //     this.outputWriter = outputWriter;
    // }
    public abstract MotorExDirectionBuilder create(); // TODO: Move?

    private class MotorExDirectionBuilder {
        private CANMotorEx motor;
        private MotorExDirectionBuilder(CANMotorEx motor) {
            this.motor = motor;
        }
        public MotorExIdleModeBuilder withDirection(Direction direction) {
            motor.setDirection(direction);
            return new MotorExIdleModeBuilder(motor);
        }
    }
    private class MotorExIdleModeBuilder {
        private CANMotorEx motor;
        private MotorExIdleModeBuilder(CANMotorEx motor) {
            this.motor = motor;
        }
        public MotorExPositionConversionFactorBuilder withIdleMode(IdleMode idleMode) {
            motor.setIdleMode(idleMode);
            return new MotorExPositionConversionFactorBuilder(motor);
        }
    }
    private class MotorExPositionConversionFactorBuilder {
        private CANMotorEx motor;
        private MotorExPositionConversionFactorBuilder(CANMotorEx motor) {
            this.motor = motor;
        }
        public MotorExVelocityConversionFactorBuilder withPositionConversionFactor(double positionConversionFactor) {
            motor.setPositionConversionFactor(positionConversionFactor);
            return new MotorExVelocityConversionFactorBuilder(motor);
        }
    }
    private class MotorExVelocityConversionFactorBuilder {
        private CANMotorEx motor;
        private MotorExVelocityConversionFactorBuilder(CANMotorEx motor) {
            this.motor = motor;
        }
        public CANMotorEx withVelocityConversionFactor(double velocityConversionFactor) {
            motor.setPositionConversionFactor(positionConversionFactor);
            return motor;
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
    public abstract double getSpeed();

    public void stop() {
        setPower(0);
    }
}
