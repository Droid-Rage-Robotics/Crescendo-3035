package frc.robot.utility.motor;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeCanSparkMax extends SafeMotor {
    private final CANSparkMax motor;
    public SafeCanSparkMax(int deviceId, MotorType type, 
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter) {
        super(isEnabled, outputWriter);
        motor = new CANSparkMax(deviceId, type);
    }
    public SafeCanSparkMax(int deviceId, MotorType type, 
        boolean isInverted,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter) {
        super(isEnabled, outputWriter);
        motor = new CANSparkMax(deviceId, type);
    }

    @Override
    public void setPower(double power) {
        if(!DriverStation.isFMSAttached()){ //TODO: Test
            outputWriter.write(power);
        }
        if (!isEnabled.get()) motor.set(0);
            else motor.set(power);
    }

    @Override
    public void setVoltage(double outputVolts) {
        if(!DriverStation.isFMSAttached()){
            outputWriter.write(outputVolts);
        }
        if (!isEnabled.get()) motor.set(0);
            else motor.setVoltage(outputVolts);
    }

    @Override
    public void setInverted(boolean isInverted) {
        motor.setInverted(isInverted);
    }

    @Override
    public void setIdleMode(IdleMode mode) {
        motor.setIdleMode(switch (mode) {
            case Brake -> CANSparkMax.IdleMode.kBrake;
            case Coast -> CANSparkMax.IdleMode.kCoast;
        });
    }
    
    private CANSparkMax getSparkMax() {
        return motor;
    }


    public RelativeEncoder getEncoder() {
        return motor.getEncoder();
    }    

    // public double getVelocity() {
    //     return motor.getEncoder().getVelocity();
    // }

    public SparkAbsoluteEncoder getAbsoluteEncoder(SparkAbsoluteEncoder.Type encoderType) {
        return motor.getAbsoluteEncoder(encoderType);
    }
    
    public void follow(SafeCanSparkMax leader, boolean invert) {
        motor.follow(leader.getSparkMax(), invert);
    }

    public void burnFlash() {
        motor.burnFlash();
    }
    // public boolean getMotorFault(){
    //     return motor.getFault(FaultID.kCANRX);
    //     // return motor.getFault(FaultID.kCANTX);
    // }
    // public void periodic(){}
    // public boolean getStickyFault(){
    //     return motor.getStickyFault(FaultID.kCANRX);
    //     // return motor.getFault(FaultID.kCANTX);
    // }
}
