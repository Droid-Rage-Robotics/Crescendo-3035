package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Shooter extends SubsystemBase {
public class Constants {
        public static final double GEAR_RATIO = 1 / 180;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }

    public enum ShooterSpeeds {
        AMP_SHOOT(1000),
        SPEAKER_SHOOT(20000),
        HOLD(SPEAKER_SHOOT.get()*.3),
        STOP(0), 
        CLAW_TRANSFER(0),
        POSITION_TOLERANCE(5),

        ;
        private final ShuffleboardValue<Double> velocityRPM;
        private ShooterSpeeds(double velocityRPM) {
            this.velocityRPM = ShuffleboardValue.create(velocityRPM, Shooter.class.
                getSimpleName()+"/"+name()+": Velocity (RPM)", Shooter.class.getSimpleName())
                    .withSize(1, 3)
                    .build();
        }
        public double get() {
            return velocityRPM.get();
        }
    }
    protected final SafeCanSparkMax motorL, motorR;
    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create
        (0.0, "Target Velocity", Shooter.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity", Shooter.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity Error", Shooter.class.getSimpleName()).build();
    
    private final PIDController shooterController;
    private final SimpleMotorFeedforward feedforward;
    // private final RelativeEncoder shooterEncoder, hoodEncoder;
    
    private final ShuffleboardValue<Boolean> isEnabled;

    public Shooter(Boolean isEnabled) {
        this.isEnabled = ShuffleboardValue.create(isEnabled, "Is Enabled", Shooter.class.getSimpleName())
            .withWidget(BuiltInWidgets.kToggleSwitch)
            .build();

        motorL = new SafeCanSparkMax(
            1,
            MotorType.kBrushless,
            this.isEnabled,
                ShuffleboardValue.create(0.0, "VoltageL", Shooter.class.getSimpleName())
                    .build()
        );
        motorR = new SafeCanSparkMax(
            4,
            MotorType.kBrushless,
            this.isEnabled,
                ShuffleboardValue.create(0.0, "VoltageR", Shooter.class.getSimpleName())
                    .build() 
        );
        motorL.setInverted(true);
        motorR.setInverted(false);
        // motorL.follow(motorR, false);
        shooterController = new PIDController(
            0.00005,//0.000173611    //.00005
            0,
            0);
        motorL.setIdleMode(IdleMode.Brake);
        motorR.setIdleMode(IdleMode.Brake);
        feedforward = new SimpleMotorFeedforward(0.000,0,0);
        shooterController.setTolerance(ShooterSpeeds.POSITION_TOLERANCE.get());
    }

    @Override
    public void periodic() {
        setPower(shooterController.calculate(getVelocity())+
            feedforward.calculate(getVelocity(), getVelocity()));
    }
      
    @Override
    public void simulationPeriodic() {}

    public Command setTargetVelocity(ShooterSpeeds velocity) {
        return runOnce(() -> {
            shooterController.setSetpoint(velocity.get());
            targetVelocityWriter.set(velocity.get());
        });
    }

    public double getVelocity() {
        return motorL.getEncoder().getVelocity();
        // return motorR.getEncoder().getVelocity();
    }
      
    private void setPower(double power) {
        // if (!isEnabled.get()) return;
        motorL.setPower(power);
        motorR.setPower(power);
    }
}