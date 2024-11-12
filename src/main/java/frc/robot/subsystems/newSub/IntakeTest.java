package frc.robot.subsystems.newSub;

import com.revrobotics.SparkMaxLimitSwitch.Direction;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.encoder.AbsoluteDutyEncoderRIO;
import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.motor.better.SparkMax;
import frc.robot.utility.motor.better.TalonEx;
import frc.robot.utility.motor.better.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.template.ArmAbsolute;
import frc.robot.utility.template.SetPower;

public class IntakeTest extends SetPower {
    private static TalonEx talon = TalonEx.create(2)
        .withDirection(CANMotorEx.Direction.Forward)
        .withIdleMode(ZeroPowerMode.Brake)
        .withPositionConversionFactor(1)
        .withIsEnabled(false)
        .withSupplyCurrentLimit(0);
            
    public IntakeTest(){
        super(new CANMotorEx[]{talon} , TestSubsystem.class.getName());
        // test.get
    }

}
