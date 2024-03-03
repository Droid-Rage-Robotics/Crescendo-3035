package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Shooter extends SubsystemBase {
public class Constants {
    // (out)4/3 (input)
        public static final double GEAR_RATIO = 3/4;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
        public static final double CONVERSION = 4/3;
    }

    public enum ShooterSpeeds {
        //15000+
        AMP_SHOOT(5000),//200
        SPEAKER_SHOOT(12000),//10000
        HOLD(SPEAKER_SHOOT.get()*.3),
        STOP(0), 
        CLAW_TRANSFER(4000),//4000
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
    protected final SafeTalonFX motorL, motorR;
    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create
        (0.0, "Shooter/Target Velocity", 
        Shooter.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
        (0.0, "Shooter/Encoder Velocity", 
        Shooter.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Shooter/Encoder Velocity Error", 
        Shooter.class.getSimpleName()).build();
    
    private final PIDController shooterController;
    private final SimpleMotorFeedforward feedforward;
    
    private final ShuffleboardValue<Boolean> isEnabled;

    public Shooter(Boolean isEnabled) {
        this.isEnabled = ShuffleboardValue.create(isEnabled, "Is Enabled", 
            Shooter.class.getSimpleName())
            .withWidget(BuiltInWidgets.kToggleSwitch)
            .build();

        motorL = new SafeTalonFX(
            19,
            false,
            IdleMode.Coast,
            Constants.ROTATIONS_TO_RADIANS,
            Constants.ROTATIONS_TO_RADIANS,
            this.isEnabled,
                ShuffleboardValue.create(0.0, "Shooter/VoltageL", 
                Shooter.class.getSimpleName())
                    .build()
        );
        motorR = new SafeTalonFX(
            18,
            false,
            IdleMode.Coast,
            Constants.ROTATIONS_TO_RADIANS,
            Constants.ROTATIONS_TO_RADIANS,
            this.isEnabled,
                ShuffleboardValue.create(0.0, "Shooter/VoltageR", 
                Shooter.class.getSimpleName())
                    .build() 
        );
       
        shooterController = new PIDController(
            0.00005,//0.000173611    //.00005
            0,
            0);
        // feedforward = new SimpleMotorFeedforward(0.000,0,0);
        feedforward = new SimpleMotorFeedforward(.3246, 2.1305, .58723);//from sysid
        // feedforward = new SimpleMotorFeedforward(.01, 0.01305, 0);

        // feedforward = new SimpleMotorFeedforward(5.24, 5.7, 1);
        shooterController.setTolerance(ShooterSpeeds.POSITION_TOLERANCE.get());
    }

    @Override
    public void periodic() {
        encoderVelocityWriter.set(getVelocity());
        encoderVelocityErrorWriter.set(shooterController.getVelocityError());
        setPower(shooterController.calculate(getVelocity())+
            feedforward.calculate(getVelocity(), getVelocity()));

        
    }
      
    @Override
    public void simulationPeriodic() {}

    public void setTargetVelocity(ShooterSpeeds velocity) {
        // return runOnce(() -> {
            shooterController.setSetpoint(velocity.get());
            targetVelocityWriter.set(velocity.get());
        // });
    }

    public double getVelocity() {
        return motorL.getVelocity();
        // return motorR.getEncoder().getVelocity();
    }
      
    private void setPower(double power) {
        // if (!isEnabled.get()) return;
        motorL.setPower(power);
        motorR.setPower(power);
    }

    public SafeTalonFX getMotorL(){
        return motorL;
    }
    public SafeTalonFX getMotorR(){
        return motorR;
    }
    // public void setMotorLVoltage(double voltage) {
    //     motorL.setVoltage(voltage);
    // }
    // public void setMotorRVoltage(double voltage) {
    //     motorR.setVoltage(voltage);
    // }
}