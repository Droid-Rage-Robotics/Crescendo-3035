package frc.robot.subsystems.drive;

import java.util.function.Supplier;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
// import com.ctre.phoenix.sensors.CANCoder;
// import com.ctre.phoenix.sensors.CANCoderConfiguration;
// import com.ctre.phoenix.sensors.SensorInitializationStrategy;
// import com.ctre.phoenix.sensors.SensorTimeBase;
import com.revrobotics.CANSparkMax;
// import com.ctre.phoenix6.configs.CANcoderConfiguration;
// import com.ctre.phoenix6.configs.CANcoderConfigurator;
// import com.ctre.phoenix6.hardware.CANcoder;
// import com.ctre.phoenix6.signals.MagnetHealthValue;
// import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SwerveModuleKraken {
//     public static class Constants {
//         public static final double WHEEL_DIAMETER_METERS = Units.inchesToMeters(4);
//         public static final double DRIVE_MOTOR_GEAR_RATIO = 1 / 6.75;
//         public static final double TURN_MOTOR_GEAR_RATIO = 1 / 21.42;

//         public static final double DRIVE_ENCODER_ROT_2_METER = DRIVE_MOTOR_GEAR_RATIO * Math.PI * WHEEL_DIAMETER_METERS;
//         public static final double DRIVE_ENCODER_RPM_2_METER_PER_SEC = DRIVE_ENCODER_ROT_2_METER / 60;
//         public static final double READINGS_PER_REVOLUTION = 1;//4096
//         public static final double TURN_ENCODER_ROT_2_RAD = 2 * Math.PI / READINGS_PER_REVOLUTION;

//         // public static final double TURN_P = 0.0005;//0.5

//         public static final double PHYSICAL_MAX_SPEED_METERS_PER_SECOND = 4.47;

//         // private static final double DRIVE_KA = 0.12; 
//         private static final double DRIVE_KV = 2.7; // this value is multiplied by veloicty in meteres per second
//         private static final double DRIVE_KS = 0.18966; //this value is the voltage that iwll be constantly applied
//     }

//     private final SafeTalonFX driveMotor;
//     private final SafeCanSparkMax turnMotor;

//     private final Supplier<Double> absoluteEncoderOffsetRad;
//     private final CANcoder turnEncoder;
//     private final PIDController turningPidController;
//     private final SimpleMotorFeedforward feedforward;

//     // private final double driveSpeedMultiplier;

//     public SwerveModuleKraken(int driveMotorId, int turnMotorId, boolean driveMotorReversed, boolean turningMotorReversed,
//             int absoluteEncoderId, Supplier<Double> absoluteEncoderOffsetRad, boolean absoluteEncoderReversed, boolean isEnabled) {
        
//         this.absoluteEncoderOffsetRad = absoluteEncoderOffsetRad;

//         turnEncoder = new CANcoder(absoluteEncoderId);
//         CANcoderConfiguration config = new CANcoderConfiguration();
//         if(absoluteEncoderReversed){
//             config.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
//         } else{
//             config.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
//         }
//         config.MagnetSensor.MagnetOffset = absoluteEncoderOffsetRad.get()/Constants.TURN_ENCODER_ROT_2_RAD;
//         config.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
//         turnEncoder.getConfigurator().apply(config);
//         //TODO: FIGURE Out whether I need to manually change from rotation to radians?
//         driveMotor = new SafeTalonFX(driveMotorId,
//             ShuffleboardValue.create(isEnabled, "Drive Is Enabled", SwerveDrive.class.getSimpleName())
//                     .withWidget(BuiltInWidgets.kToggleSwitch)
//                     .build(),
//             ShuffleboardValue.create(0.0, "Drive Voltage", SwerveDrive.class.getSimpleName())
//                 .build());
//         turnMotor = new SafeCanSparkMax(turnMotorId, MotorType.kBrushless,
//             ShuffleboardValue.create(isEnabled, "Turn Is Enabled", SwerveDrive.class.getSimpleName())
//                     .withWidget(BuiltInWidgets.kToggleSwitch)
//                     .build(),
//             ShuffleboardValue.create(0.0, "Turn Voltage", SwerveDrive.class.getSimpleName())
//                 .build());

//         driveMotor.setInverted(driveMotorReversed);
//         turnMotor.setInverted(turningMotorReversed);

//         driveMotor.getPosition()
//         driveMotor.getEncoder().setPositionConversionFactor(Constants.DRIVE_ENCODER_ROT_2_METER);
//         driveMotor.getEncoder().setVelocityConversionFactor(Constants.DRIVE_ENCODER_RPM_2_METER_PER_SEC);
// // Constants.TURN_P
//         turningPidController = new PIDController(0.1, 0.0, 0.0);
//         turningPidController.enableContinuousInput(-Math.PI, Math.PI);

//         feedforward = new SimpleMotorFeedforward(Constants.DRIVE_KS, Constants.DRIVE_KV);

//         resetDriveEncoder();
//     }

//     public double getDrivePos() {
//         return driveMotor.getEncoder().getPosition();
//     }

//     public double getTurningPosition() {
//         return (turnEncoder.getAbsolutePosition().getValueAsDouble()*Constants.TURN_ENCODER_ROT_2_RAD);// + absoluteEncoderOffsetRad.get();
//         // return turnEncoder.getAbsolutePosition().getValueAsDouble() + absoluteEncoderOffsetRad.get();
//     }

//     public double getDriveVelocity(){
//         return driveMotor.getEncoder().getVelocity();
//     }

//     public double getTurningVelocity(){
//         return turnEncoder.getVelocity().getValueAsDouble();
//     }

//     // public double getTurnEncoderRad() {
//     //     return getTurningPosition();
//     // }
    
//     public void resetDriveEncoder(){
//         driveMotor.getEncoder().setPosition(0);
//     }

//     public SwerveModulePosition getPosition() {
//         return new SwerveModulePosition(getDrivePos(), new Rotation2d(getTurningPosition()));
//     }

//     public SwerveModuleState getState(){
//         return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getTurningPosition()));
//     }

//     public void setState(SwerveModuleState state) {
//         if (Math.abs(state.speedMetersPerSecond) < 0.001) {
//             stop();
//             return;
//         }
//         state = SwerveModuleState.optimize(state, getState().angle);
//         driveMotor.set(state.speedMetersPerSecond / Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND);
//         turnMotor.set((turningPidController.calculate(getTurningPosition(), state.angle.getRadians()))*1);
//         SmartDashboard.putString("Swerve[" + turnEncoder.getDeviceID() + "] state", state.toString());
//         SmartDashboard.putString("Swerve[" + turnMotor.getDeviceId() + "] state", state.toString());
//     }

//     public void setFeedforwardState(SwerveModuleState state) {
//         if (Math.abs(state.speedMetersPerSecond) < 0.001) {
//             stop();
//             return;
//         }
//         state = SwerveModuleState.optimize(state, getState().angle);
//         driveMotor.setVoltage(feedforward.calculate(state.speedMetersPerSecond));
//         turnMotor.set(turningPidController.calculate(getTurningPosition(), state.angle.getRadians()));
//         SmartDashboard.putString("Swerve[" + turnEncoder.getDeviceID() + "] state", state.toString());
//         SmartDashboard.putString("Swerve[" + turnMotor.getDeviceId() + "] state", state.toString());
//     }

//     public void stop(){
//         driveMotor.set(0);
//         turnMotor.set(0);
//     }

//     public void coastMode() {
//         driveMotor.setIdleMode(IdleMode.kCoast);
//         turnMotor.setIdleMode(IdleMode.kCoast);
//     }

//     public void brakeMode() {
//         driveMotor.setIdleMode(IdleMode.kBrake);
//         turnMotor.setIdleMode(IdleMode.kBrake);
//     }

//     public void brakeAndCoastMode() {
//         driveMotor.setIdleMode(IdleMode.kBrake);
//         turnMotor.setIdleMode(IdleMode.kCoast);
//     }
}
