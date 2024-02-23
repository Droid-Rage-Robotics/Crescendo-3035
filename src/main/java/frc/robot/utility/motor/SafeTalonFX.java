package frc.robot.utility.motor;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.RobotController;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeTalonFX extends SafeMotor{
    private final TalonFX motor;
    private final Orchestra orchestra;

    public SafeTalonFX(int deviceNumber, boolean isInverted, 
        IdleMode mode, ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter) {
        super(isEnabled, outputWriter);
        motor = new TalonFX(deviceNumber);
        
        TalonFXConfiguration configuration = new TalonFXConfiguration();
        if(isInverted){
            configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        } else {
            configuration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        }
        motor.setNeutralMode(switch (mode) {
            case Brake -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
        configuration.Audio.AllowMusicDurDisable = false; //true
        // configuration.Feedback.SensorToMechanismRatio = 1;

        orchestra = new Orchestra();
        orchestra.addInstrument(motor);
        orchestra.loadMusic("Test.chrp"); // Can Load ALL Music in a array
        motor.getConfigurator().apply(configuration);
    }


    public void setPower(double power) {
        outputWriter.write(power);
        if (!isEnabled.get()) motor.set(0);
            else motor.set(power);
    }

    public void setVoltage(double outputVolts) {
        outputWriter.write(outputVolts);
        if (!isEnabled.get()) motor.set(0);
            else motor.set(outputVolts / RobotController.getBatteryVoltage());
    }

    @Override
    public void setInverted(boolean isInverted) {
        motor.setInverted(isInverted);;
    }

    @Override
    public void setIdleMode(IdleMode mode) {
        // motor.setNeutralMode(switch (mode) {
        //     case Brake -> NeutralModeValue.Brake;
        //     case Coast -> NeutralModeValue.Coast;
        // });
    }

    public double getVelocity() {
        return motor.getVelocity().getValueAsDouble();
    }

    public double getPosition() {
        return motor.getPosition().getValueAsDouble();
    }

    public void setPosition(double position) {
        motor.setPosition(position);
    }

    public TalonFXConfigurator getConfigurator(){
        return motor.getConfigurator();
    }

    public void playMusic(){
        orchestra.play();
    }

    public void pauseMusic(){
        orchestra.pause();
    }

    public void stopMusic(){
        orchestra.stop();
    }
}