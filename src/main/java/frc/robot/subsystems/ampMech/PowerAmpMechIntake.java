package frc.robot.subsystems.ampMech;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class PowerAmpMechIntake extends SubsystemBase {
    protected final ShuffleboardValue<Double> targetPowerWriter = ShuffleboardValue.create
        (0.0, "Intake/ Claw Intake Target Power", AmpMech.class.getSimpleName()).build();
    private final ShuffleboardValue<Boolean> isElementInClawWriter = ShuffleboardValue.create
            (false, "Intake/ Claw Intake Is Element In", AmpMech.class.getSimpleName()).build();
    
    
    protected final SafeCanSparkMax motor;

    public PowerAmpMechIntake(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            25,
            MotorType.kBrushless,
            false,
            IdleMode.Coast,
            1,
            1,
            ShuffleboardValue.create(isEnabled, "Intake/ Claw Intake Is Enabled", AmpMech.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Intake/ Claw Intake Voltage", AmpMech.class.getSimpleName())
                    .build()
        );
    }

    @Override
    public void periodic() {
        isElementInClawWriter.set(isElementInClaw());
    }
    
    protected void setTargetPosition(double target) {
        motor.setPower(target);
        targetPowerWriter.set(target);
    }
    
    protected boolean isElementInClaw(){
        return motor.getSpeed()<targetPowerWriter.get();
    }
    public SafeCanSparkMax getMotor(){
        return motor;
    }
    public double getTargetPosition(){
        return targetPowerWriter.get();
    }
}

