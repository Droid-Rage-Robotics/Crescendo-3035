package frc.robot.utility.motor.old;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeCanSparkMax extends SafeMotor {
    private final CANSparkMax motor;
    // private ShuffleboardValue<Double> numWriter = ShuffleboardValue.create
    //     (0.0, "Misc/safecansparkmac thing", Climb.class.getSimpleName())
    //     .build();
    public SafeCanSparkMax(
        int deviceId, MotorType type, 
        boolean isInverted, IdleMode idleMode,
        double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter) {
            this(deviceId, type, isInverted, idleMode, 
            positionConversionFactor, velocityConversionFactor, 
            isEnabled, outputWriter, 40);
            //Makes Default for all Spark Maxes; Sparks usual default is 80
    }   
    public SafeCanSparkMax(
        int deviceId, MotorType type, 
        boolean isInverted, IdleMode idleMode,
        double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter, int smartCurrentLimit) {

        super(isEnabled, outputWriter);
        motor = new CANSparkMax(deviceId, type);
        motor.setInverted(isInverted);
        setIdleMode(idleMode);
        motor.getEncoder().setPositionConversionFactor(positionConversionFactor);
        motor.getEncoder().setVelocityConversionFactor(positionConversionFactor/60);
        setSmartCurrentLimit(smartCurrentLimit);//Sparks usual default is 80
        motor.burnFlash();
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
            // numWriter.set(
            //     motor.getBusVoltage()
            //     // motor.getOutputCurrent()
            //     // motor.getVoltageCompensationNominalVoltage()
            //     // motor.enableVoltageCompensation(33) 

            // );
    }

    // @Override
    // public void setInverted(boolean isInverted) {
    //     motor.setInverted(isInverted);
    // }

    @Override
    public void setIdleMode(SafeMotor.IdleMode mode) {
        motor.setIdleMode(switch (mode) {
            case Brake -> CANSparkMax.IdleMode.kBrake;
            case Coast -> CANSparkMax.IdleMode.kCoast;
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
    public double getPosition() {
        return motor.getEncoder().getPosition();
    }

    // @Override
    // public void setPositionConversionFactor(double num) {
    //     motor.getEncoder().setPositionConversionFactor(num);
    // }    

    @Override
    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

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

    // @Override
    // public void set(double num){
    //     motor.set(num);
    // }
    @Override
    public int getDeviceID(){
        return motor.getDeviceId();
    }

    
    // @Override
    // public void setVelocityConversionFactor(double num) {
    //     motor.getEncoder().setVelocityConversionFactor(num);
    // }

    @Override
    public double getSpeed(){
        return motor.get();
    }

    private void setSmartCurrentLimit(int num){
        motor.setSmartCurrentLimit(num);
    }

    public double getVoltage(){
        // return motor.getAppliedOutput();//motor controller's applied output duty cycle.
        // return motor.getBusVoltage();//voltage fed into the motor controller.
        return motor.getOutputCurrent();//motor controller's output current in Amps.
    }

    public void reset(double num){
        motor.getEncoder().setPosition(num);
    }
}

