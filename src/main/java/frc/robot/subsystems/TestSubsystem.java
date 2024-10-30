package frc.robot.subsystems;

import com.revrobotics.SparkMaxLimitSwitch.Direction;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.motor.better.SparkMax;
import frc.robot.utility.motor.better.TalonEx;
import frc.robot.utility.motor.better.CANMotorEx.ZeroPowerMode;

public class TestSubsystem extends SubsystemBase {
    
    private SparkMax test = SparkMax.create(3)
        .withDirection(CANMotorEx.Direction.Forward)
        .withIdleMode(ZeroPowerMode.Brake)
        .withPositionConversionFactor(1)
        .withIsEnabled(false);

    private TalonEx talon = TalonEx.create(2)
    .withDirection(CANMotorEx.Direction.Forward)
    .withIdleMode(ZeroPowerMode.Brake)
    .withPositionConversionFactor(1)
    .withIsEnabled(false)
    .withSupplyCurrentLimit(0);
}
