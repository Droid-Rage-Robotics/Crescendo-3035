package frc.robot.utility.motor.better;

import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.DroidRageConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;

public class SparkMax extends CANMotorEx{

    private final CANSparkMax motor;
    private SparkMax(CANSparkMax motor) {
        this.motor = motor;
    }

    public static DirectionBuilder create(int deviceID) {
        CANMotorEx motor = new SparkMax(new CANSparkMax(deviceID, MotorType.kBrushless));
        return motor.new DirectionBuilder();
    }

    @Override
    public void setPower(double power) {
        if(isEnabledWriter.get()){
            motor.set(power);
        }
        if(DroidRageConstants.removeWriterWriter.get()){
            outputWriter.set(power);
        }
    }

    @Override
    public void setVoltage(double outputVolts) {
        if(isEnabledWriter.get()){
            motor.setVoltage(outputVolts);
        }
        if(DroidRageConstants.removeWriterWriter.get()){
            outputWriter.set(outputVolts);
        }
    }

    @Override
    public void setDirection(Direction direction) {
        motor.setInverted(switch (direction) {
                case Forward -> false;
                case Reversed -> true;
            });
    }

    @Override
    public void setIdleMode(ZeroPowerMode mode) {
        motor.setIdleMode(switch (mode) {
            case Brake -> IdleMode.kBrake;
            case Coast -> IdleMode.kCoast;
        });
    }

    @Override
    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    @Override
    public double getPosition() {
        return motor.getEncoder().getPosition();
    }

    @Override
    public int getDeviceID() {
        return motor.getDeviceId();
    }

    //Casting the double to an int
    @Override
    public void setSupplyCurrentLimit(double currentLimit) {
        motor.setSmartCurrentLimit((int) currentLimit);
    }

    @Override
    public void resetEncoder(int num) {
        motor.getEncoder().setPosition(num);
    }
}
