package frc.robot.subsystems.shooter;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
@Deprecated
public class ShooterHood extends SubsystemBase {
    public enum HoodPos {
        AMP(1),
        SPEAKER(2),
        STOP(0), 
        POSITION_TOLERANCE(.5),

        ;
        private final ShuffleboardValue<Double> pos;
        private HoodPos(double pos) {
            this.pos = ShuffleboardValue.create(pos, Shooter.class.
                getSimpleName()+"/"+name()+": POS", Shooter.class.getSimpleName())
                    .withSize(1, 3)
                    .build();
        }
        public double get() {
            return pos.get();
        }
    }
    protected final SafeCanSparkMax shooterHood;
    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create
        (0.0, "Target Velocity", Shooter.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity", Shooter.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity Error", Shooter.class.getSimpleName()).build();
    
    private final PIDController hoodController;
    private final SimpleMotorFeedforward feedforward;
    private final ShuffleboardValue<Boolean> isEnabled;

    public ShooterHood(Boolean isEnabled) {
        this.isEnabled = ShuffleboardValue.create(isEnabled, "Is Enabled", Shooter.class.getSimpleName())
            .withWidget(BuiltInWidgets.kToggleSwitch)
            .build();

        shooterHood = new SafeCanSparkMax(
            19,
            MotorType.kBrushless,
            this.isEnabled,
                ShuffleboardValue.create(0.0, "Voltage", Shooter.class.getSimpleName())
                    .build()
        );
        shooterHood.setInverted(true);
        hoodController = new PIDController(
            0.000173611,
            0,
            0);
        hoodController.setTolerance(HoodPos.POSITION_TOLERANCE.get());
        feedforward = new SimpleMotorFeedforward(0,0,0);
    }

    @Override
    public void periodic() {
        setHoodPower(hoodController.calculate(getTargetPos())+ feedforward.calculate(getTargetPos()));
    }
  
    @Override
    public void simulationPeriodic() {}

    public Command setTargetPos(HoodPos pos) {
        return runOnce(() -> {
            hoodController.setSetpoint(pos.get());
            targetVelocityWriter.set(pos.get());
        });
    }

    private double getIntakePos() {
        return shooterHood.getEncoder().getPosition();
    }
    private double getTargetPos() {
        return hoodController.getSetpoint();
    }
    protected double calculateFeedforward(double targetVelocity) {
        return feedforward.calculate(targetVelocity);
    }
      
    private void setHoodPower(double power) {
        if (!isEnabled.get()) return;
        shooterHood.setPower(power);
    }
}