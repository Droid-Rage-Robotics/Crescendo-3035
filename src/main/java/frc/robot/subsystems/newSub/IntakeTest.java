package frc.robot.subsystems.newSub;

import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.motor.better.SparkMax;
import frc.robot.utility.motor.better.TalonEx;
import frc.robot.utility.motor.better.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.template.SetPower;

public class IntakeTest extends SetPower {
    private static SparkMax talon = SparkMax.create(24)
        .withDirection(CANMotorEx.Direction.Forward)
        .withIdleMode(ZeroPowerMode.Brake)
        .withPositionConversionFactor(1)
        .withSubsystemName(IntakeTest.class.getSimpleName())
        .withIsEnabled(true)
        .withSupplyCurrentLimit(0);
            
    public IntakeTest(){
        super(new CANMotorEx[]{talon} , IntakeTest.class.getSimpleName());
        // test.get
    }

}
