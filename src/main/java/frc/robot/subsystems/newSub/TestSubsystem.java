package frc.robot.subsystems.newSub;


import com.revrobotics.SparkLimitSwitch;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.DroidRageConstants.Control;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.encoder.AbsoluteDutyEncoderRIO;
import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.motor.better.SparkMax;
import frc.robot.utility.motor.better.TalonEx;
import frc.robot.utility.motor.better.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.template.Arm;
import frc.robot.utility.template.ArmAbsolute;
import frc.robot.utility.template.Elevator;

public class TestSubsystem extends Elevator {
    
    // private static SparkMax test = SparkMax.create(3)
    //     .withDirection(CANMotorEx.Direction.Forward)
    //     .withIdleMode(ZeroPowerMode.Brake)
    //     .withPositionConversionFactor(1)
    //     .withSubsystemName(TestSubsystem.class.getSimpleName())
    //     .withIsEnabled(false);
    
    private static SparkMax sparkMax = SparkMax.create(22)//Elevator 
        .withDirection(CANMotorEx.Direction.Forward)
        .withIdleMode(ZeroPowerMode.Coast)
        .withPositionConversionFactor(1)
        .withSubsystemName(TestSubsystem.class.getSimpleName())
        .withIsEnabled(true)
        .withSupplyCurrentLimit(0);

    // private static TalonEx talon = TalonEx.create(17)//DropDown
    //     .withDirection(CANMotorEx.Direction.Reversed)
    //     .withIdleMode(ZeroPowerMode.Brake)
    //     .withPositionConversionFactor((2 * Math.PI / 1)*2)
    //     .withSubsystemName(TestSubsystem.class.getSimpleName())
    //     .withIsEnabled(true)
    //     .withSupplyCurrentLimit(40);

    // private static AbsoluteDutyEncoderRIO encoder = 
    //     AbsoluteDutyEncoderRIO.create(0)
    //         .withDirection(false)
    //         .withOffset(0)
    //         .withSubsystemBase(TestSubsystem.class.getSimpleName());
            
    public TestSubsystem(){
        super(new CANMotorEx[]{sparkMax} , new PIDController(.52,0,0), 
        new ElevatorFeedforward(0,.37,.0,0), //+.37
        30, 0, 
        Control.FEEDFORWARD, TestSubsystem.class.getSimpleName(), 0);






        // super(new CANMotorEx[]{talon} , new PIDController(1,0,0), 
        // new ArmFeedforward(0.452,.65,.0859,.003), 
        // 360, 90, 90, 
        // Control.PID, TestSubsystem.class.getSimpleName(), 0);

        // super(new CANMotorEx[]{talon} , new PIDController(1,0,0), 
        // new ArmFeedforward(0.452,.65,.0859,.003), 
        // 360, 0, 90, 
        // Control.PID, TestSubsystem.class.getSimpleName(), 0, encoder);
        // setTargetPosition((Intake.Value.START.getAngle()));
        // test.get
    }

}
