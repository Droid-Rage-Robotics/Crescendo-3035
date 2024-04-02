package frc.robot.subsystems.ampMech;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class PowerAmpMechWheel extends SubsystemBase {
    protected final ShuffleboardValue<Double> targetPowerWriter = ShuffleboardValue.create
        (0.0, "AmpIntake/ Claw Intake Target Power", AmpMech.class.getSimpleName()).build();
    
    
    protected final SafeCanSparkMax motor;

    public PowerAmpMechWheel(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            25,
            MotorType.kBrushless,
            false,
            IdleMode.Coast,
            1,
            1,
            ShuffleboardValue.create(isEnabled, "AmpWheel/ Wheel Is Enabled", PowerAmpMechIntake.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "AmpWheel/ Wheel Voltage", PowerAmpMechIntake.class.getSimpleName())
                    .build()
        );
    }

    @Override
    public void periodic() {

    }
    
    protected void setTargetPower(double target) {
        motor.setPower(target);
        targetPowerWriter.set(target);
    }
    public SafeCanSparkMax getMotor(){
        return motor;
    }
    public double getTargetPower(){
        return targetPowerWriter.get();
    }
}

