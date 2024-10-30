package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.old.SafeTalonFX;
import frc.robot.utility.motor.old.SafeMotor.IdleMode;
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
        AMP_SHOOT(3000),//200
        SPEAKER_SHOOT(8000),//17900 CHNGE THIS
        AUTO_SPEAKER_SHOOT(18900),//10000

        HOLD(4000),
        STOP(0), 
        CLAW_TRANSFER(2300),//4000
        POSITION_TOLERANCE(5),
        // AMP(3000)

        ;
        private final ShuffleboardValue<Double> velocityRPM;
        private ShooterSpeeds(double velocityRPM) {
            this.velocityRPM = ShuffleboardValue.create(velocityRPM, "ShooterSpeeds"+"/"+
                name()+": Velocity (RPM)", Shooter.class.getSimpleName())
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
    protected final ShuffleboardValue<Double> addShooterWriter = ShuffleboardValue.create
        (0.0, "ShooterAddPower", Shooter.class.getSimpleName()).build();
    protected final ShuffleboardValue<String> targetSpeedWriter = ShuffleboardValue.create
        ("____", "Shooter/TargetShooterSpeed", 
        Shooter.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> shooterVoltageWriter = ShuffleboardValue.create
        (0.0, "Shooter/ShooterVoltageWriter", 
        Shooter.class.getSimpleName()).build();
    private final PIDController shooterController;
    private final SimpleMotorFeedforward feedforward;
    private ShooterSpeeds targetShooterSpeed = ShooterSpeeds.HOLD;
    
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
                    .build(), 
                    30,
                    30
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
                    .build(),
                    30,
                    30
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

        setTargetVelocity(ShooterSpeeds.STOP);
    }

    @Override
    public void periodic() {
        double velocity = getVelocity();
        encoderVelocityWriter.set(velocity);
        // encoderVelocityErrorWriter.set(shooterController.getVelocityError());
        encoderVelocityErrorWriter.set(shooterController.getSetpoint() - velocity);

        setPower(shooterController.calculate(velocity)+
            feedforward.calculate(velocity, velocity));

        shooterVoltageWriter.set(motorR.getVoltage());
        
    }
      
    @Override
    public void simulationPeriodic() {}

    public void setTargetVelocity(ShooterSpeeds velocity) {
        targetSpeedWriter.set(velocity.toString());
        targetShooterSpeed = velocity;
        if (velocity==ShooterSpeeds.SPEAKER_SHOOT){
            shooterController.setSetpoint(velocity.get()+addShooterWriter.get());
            targetVelocityWriter.set(velocity.get()+addShooterWriter.get());
        } else{
            shooterController.setSetpoint(velocity.get());
            targetVelocityWriter.set(velocity.get());
        }
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

    public void addShooterSpeed(double num){
        addShooterWriter.set(addShooterWriter.get()+num);
    }
    public boolean isShooterReadyToShootSpeaker(){
        return (targetShooterSpeed==ShooterSpeeds.SPEAKER_SHOOT)&&(shooterController.getPositionError()<200);
    }

    public boolean isShooterTransferAmp(){
        //1.58-1.59 Usual Wih No Ring
        //With Ring is .5 ish
        return shooterVoltageWriter.get()<.6;
        // return true;
    }
}