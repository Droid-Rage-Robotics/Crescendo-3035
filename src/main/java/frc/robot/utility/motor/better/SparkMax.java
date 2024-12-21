package frc.robot.utility.motor.better;

import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.DroidRageConstants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.CANSparkBase.IdleMode;

public class SparkMax extends CANMotorEx{

    private final CANSparkMax motor;
    private SparkMax(CANSparkMax motor) {
        this.motor = motor;
    }

    public static DirectionBuilder create(int deviceID) {
        CANMotorEx motor = new SparkMax(new CANSparkMax(deviceID, MotorType.kBrushless));
        motor.motorID = deviceID;
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

    public CANSparkMax getSparkMax() {
        return motor;
    }

    public RelativeEncoder getEncoder() {
        return motor.getEncoder();
    }

    public RelativeEncoder getAlternateEncoder(int countsPerRev) {
        return motor.getAlternateEncoder(countsPerRev);
    }

    @Override
    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    @Override
    public double getPosition() {
        return motor.getEncoder().getPosition();
    }

    public SparkAbsoluteEncoder getAbsoluteEncoder(SparkAbsoluteEncoder.Type encoderType) {
        return motor.getAbsoluteEncoder(encoderType);
    }

    public void follow(SparkMax leader, boolean invert) {
        motor.follow(leader.getSparkMax(), invert);
    }

    public void burnFlash() {
        motor.burnFlash();
    }

    @Override
    public int getDeviceID() {
        return motor.getDeviceId();
    }

    public double getSpeed(){
        return motor.get();
    }

    //Casting the double to an int
    @Override
    public void setSupplyCurrentLimit(double currentLimit) {
        motor.setSmartCurrentLimit((int) currentLimit);
    }

    public void setStatorCurrentLimit(double currentLimit){}
        
    public double getVoltage(){
        // return motor.getAppliedOutput();//motor controller's applied output duty cycle.
        // return motor.getBusVoltage();//voltage fed into the motor controller.
        return motor.getOutputCurrent();//motor controller's output current in Amps.
    }

    @Override
    public void resetEncoder(int num) {
        motor.getEncoder().setPosition(num);
    }
}
