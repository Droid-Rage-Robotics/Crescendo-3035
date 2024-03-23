package frc.robot.subsystems.ampMech;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class PowerAmpMechIntake extends SubsystemBase {
    protected final ShuffleboardValue<Double> targetPowerWriter = ShuffleboardValue.create
        (0.0, "AmpIntake/ Claw Intake Target Power", PowerAmpMechIntake.class.getSimpleName()).build();
    private final ShuffleboardValue<Boolean> isElementInClawWriter = ShuffleboardValue.create
            (false, "AmpIntake/ Claw Intake Is Element In", PowerAmpMechIntake.class.getSimpleName()).build();
    
    
    protected final SafeCanSparkMax motor;

    public PowerAmpMechIntake(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            24,
            MotorType.kBrushless,
            false,
            IdleMode.Coast,
            1,
            1,
            ShuffleboardValue.create(isEnabled, "AmpIntake/ Claw Intake Is Enabled", PowerAmpMechIntake.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "AmpIntake/ Claw Intake Voltage", PowerAmpMechIntake.class.getSimpleName())
                    .build()
        );
        // motor.setSmartCurrentLimit(20);
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

