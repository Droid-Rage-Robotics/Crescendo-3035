package frc.robot.subsystems.drive;

import java.util.function.Supplier;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeCancoder;
import frc.robot.utility.motor.SafeMotor;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SwerveModuleKraken {
    public enum POD{
        FL,
        BL,
        FR,
        BR
    }
    public static class Constants {
        public static final double WHEEL_DIAMETER_METERS = Units.inchesToMeters(4);
        public static final double DRIVE_MOTOR_GEAR_RATIO = 1 / 6.75;
        public static final double TURN_MOTOR_GEAR_RATIO = 1 / 21.42;

        public static final double DRIVE_ENCODER_ROT_2_METER = DRIVE_MOTOR_GEAR_RATIO * Math.PI * WHEEL_DIAMETER_METERS;
        public static final double DRIVE_ENCODER_RPM_2_METER_PER_SEC = DRIVE_ENCODER_ROT_2_METER / 60;
        public static final double READINGS_PER_REVOLUTION = 1;//4096
        public static final double TURN_ENCODER_ROT_2_RAD = 2 * Math.PI / READINGS_PER_REVOLUTION;

        public static final double TURN_P = 0.11;//0.5

        public static final double PHYSICAL_MAX_SPEED_METERS_PER_SECOND = 4.47;

        // private static final double DRIVE_KA = 0.12; 
        private static final double DRIVE_KV = 2.7; // this value is multiplied by veloicty in meteres per second
        private static final double DRIVE_KS = 0.18966; //this value is the voltage that iwll be constantly applied
    }

    private final SafeTalonFX driveMotor;
    private final SafeCanSparkMax turnMotor;

    // private final Supplier<Double> absoluteEncoderOffsetRad;
    private final CANcoder turnEncoder;
    private final PIDController turningPidController;
    private final SimpleMotorFeedforward feedforward;
    // private static int num = 1;
    // private final double driveSpeedMultiplier;
    private ShuffleboardValue<Double> turnPositionWriter;
    private ShuffleboardValue<Double> drivePositionWriter;
    public SwerveModuleKraken(int driveMotorId, 
        int turnMotorId, boolean driveMotorReversed, 
        boolean turningMotorReversed, int absoluteEncoderId, 
        Supplier<Double> absoluteEncoderOffsetRad, 
        boolean absoluteEncoderReversed, boolean isEnabled, POD podName) {

        turnEncoder = new CANcoder(absoluteEncoderId);
        CANcoderConfiguration config = new CANcoderConfiguration();
        if(absoluteEncoderReversed){
            config.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        } else{
            config.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        }
        config.MagnetSensor.MagnetOffset = absoluteEncoderOffsetRad.get()/Constants.TURN_ENCODER_ROT_2_RAD;
        config.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        turnEncoder.getConfigurator().apply(config);
        
        driveMotor = new SafeTalonFX(driveMotorId,
            driveMotorReversed,
            SafeMotor.IdleMode.Brake,
            Constants.DRIVE_ENCODER_ROT_2_METER,
            Constants.DRIVE_ENCODER_RPM_2_METER_PER_SEC,
            ShuffleboardValue.create(isEnabled, "Module/Module " + podName.toString() + "/Drive Is Enabled "+ 
                podName.toString() + driveMotorId, SwerveDrive.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "Module/Module " + podName.toString() + "/Drive Voltage "+ 
                podName.toString() + driveMotorId, SwerveDrive.class.getSimpleName())
                .build(),
                30
                // ,300
                );
        turnMotor = new SafeCanSparkMax(turnMotorId, MotorType.kBrushless,
            turningMotorReversed,
            IdleMode.Coast,
            Constants.TURN_ENCODER_ROT_2_RAD,
            1.0,
            ShuffleboardValue.create(isEnabled, "Module/Module " + podName.toString() + "/Turn Is Enabled "+
                podName.toString() +turnMotorId, SwerveDrive.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            ShuffleboardValue.create(0.0, "Module/Module " + podName.toString() + "/Turn Voltage "+ 
                podName.toString() + turnMotorId, SwerveDrive.class.getSimpleName())
                .build()
                // 7
        );
        turnMotor.setSmartCurrentLimit(30);

        // driveMotor.setPositionConversionFactor(Constants.DRIVE_ENCODER_ROT_2_METER);
        // driveMotor.getEncoder().setVelocityConversionFactor(Constants.DRIVE_ENCODER_RPM_2_METER_PER_SEC);

        turningPidController = new PIDController(Constants.TURN_P, 0.0, 0.0);
        turningPidController.enableContinuousInput(0, 2*Math.PI);//Was  -Math.PI, Math.PI but changed to 0 and 2PI

        feedforward = new SimpleMotorFeedforward(Constants.DRIVE_KS, Constants.DRIVE_KV);

        this.turnPositionWriter = ShuffleboardValue.create(0.0, "Module/Module " + podName.toString() + "/Turn Position (Radians)", 
            SwerveDrive.class.getSimpleName()).build();
        this.drivePositionWriter = ShuffleboardValue.create(0.0, "Module/Module " + podName.toString() + "/Drive Position (Radians)", 
            SwerveDrive.class.getSimpleName()).build();
        resetDriveEncoder();
    }

    public double getDrivePos() {
        drivePositionWriter.write(driveMotor.getPosition());
        return driveMotor.getPosition();
    }

    public double getTurningPosition() {
        turnPositionWriter.write(turnEncoder.getAbsolutePosition().getValueAsDouble()*Constants.TURN_ENCODER_ROT_2_RAD);
        return (turnEncoder.getAbsolutePosition().getValueAsDouble()*Constants.TURN_ENCODER_ROT_2_RAD);
    }

    public double getDriveVelocity(){
        // return driveMotor.getEncoder().getVelocity();
        return driveMotor.getVelocity();
    }

    public double getTurningVelocity(){
        return turnEncoder.getVelocity().getValueAsDouble();
    }

    // public double getTurnEncoderRad() {
    //     return getTurningPosition();
    // }
    
    public void resetDriveEncoder(){
        // driveMotor.getEncoder().setPosition(0);
        driveMotor.setPosition(0);
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(getDrivePos(), new Rotation2d(getTurningPosition()));
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getTurningPosition()));
    }

    public void setState(SwerveModuleState state) {
        if (Math.abs(state.speedMetersPerSecond) < 0.001) {
            stop();
            return;
        }
        state = SwerveModuleState.optimize(state, getState().angle);
        driveMotor.setPower(state.speedMetersPerSecond / Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND);
        turnMotor.setPower((turningPidController.calculate(getTurningPosition(), state.angle.getRadians()))*1);
        SmartDashboard.putString("Swerve[" + turnEncoder.getDeviceID() + "] state", state.toString());
        SmartDashboard.putString("Swerve[" + turnMotor.getDeviceID() + "] state", state.toString());
    }

    public void setFeedforwardState(SwerveModuleState state) {
        if (Math.abs(state.speedMetersPerSecond) < 0.001) {
            stop();
            return;
        }
        state = SwerveModuleState.optimize(state, getState().angle);
        driveMotor.setVoltage(feedforward.calculate(state.speedMetersPerSecond));
        turnMotor.setPower(turningPidController.calculate(getTurningPosition(), state.angle.getRadians()));
        SmartDashboard.putString("Swerve[" + turnEncoder.getDeviceID() + "] state", state.toString());
        SmartDashboard.putString("Swerve[" + turnMotor.getDeviceID() + "] state", state.toString());
    }

    public void stop(){
        driveMotor.setPower(0);
        turnMotor.setPower(0);
    }

    public void coastMode() {
        driveMotor.setIdleMode(IdleMode.Coast);
        turnMotor.setIdleMode(IdleMode.Coast);
    }

    public void brakeMode() {
        driveMotor.setIdleMode(IdleMode.Brake);
        turnMotor.setIdleMode(IdleMode.Brake);
    }

    public void brakeAndCoastMode() {
        driveMotor.setIdleMode(IdleMode.Brake);
        turnMotor.setIdleMode(IdleMode.Coast);
    }

    public void playMusic(int num){
        driveMotor.playMusic(num);
    }
    public SafeCanSparkMax getTurnMotor(){
        return turnMotor;
    }
}
