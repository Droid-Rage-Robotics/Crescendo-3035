package frc.robot.utility.motor;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.RobotController;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeTalonFX extends SafeMotor{
    private final TalonFX motor;

    public SafeTalonFX(int deviceNumber, String canbus, ShuffleboardValue<Boolean> isEnabled, ShuffleboardValue<Double> outputWriter) {
        super(isEnabled, outputWriter);
        motor = new TalonFX(deviceNumber, canbus);
    }

    public SafeTalonFX(int deviceNumber, ShuffleboardValue<Boolean> isEnabled, ShuffleboardValue<Double> outputWriter) {
        super(isEnabled, outputWriter);
        motor = new TalonFX(deviceNumber);
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
        motor.setNeutralMode(switch (mode) {
            case Brake -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
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
}

