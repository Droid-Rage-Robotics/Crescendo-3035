package frc.robot.subsystems.drive;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.old.SafeCanSparkMax;
import frc.robot.utility.motor.old.SafeMotor;
import frc.robot.utility.motor.old.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SixWheel extends SubsystemBase {
    
    protected SafeMotor 
        frontLeft,
        frontRight,
        backLeft,
        backRight;
    private final Encoder leftEncoder = new Encoder (        //Left Encoder
        9,
        8,
        false,
        CounterBase.EncodingType.k4X
    );

    private final Encoder rightEncoder = new Encoder(        //Right Encoder
        7,
        6,
        false,
        CounterBase.EncodingType.k4X
    );
    
    private DifferentialDrive drive;
    private DifferentialDriveOdometry odometry;
    private DifferentialDriveKinematics driveKinematics = new DifferentialDriveKinematics(15);//trackWidth

    // private MotorController leftMotors = new MotorController();

    // private MotorController rightMotors = new MotorController(frontRight, backRight);

    PIDController turnController;
    double rotateToAngleRate;

    private final Pigeon2 pigeon2;
    private final ShuffleboardValue<Double> headingWriter = 
        ShuffleboardValue.create(0.0, "Current/Gyro/Heading-Yaw (Degrees)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Double> rollWriter = 
        ShuffleboardValue.create(0.0, "Current/Gyro/Roll (Degrees)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Double> pitchWriter =   
        ShuffleboardValue.create(0.0, "Current/Gyro/Pitch (Degrees)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<String> locationWriter = 
        ShuffleboardValue.create("", "Current/Robot Location", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Boolean> isEnabled = 
        ShuffleboardValue.create(true, "Is Drive Enabled", SwerveDrive.class.getSimpleName())
        .withWidget(BuiltInWidgets.kToggleSwitch)
        .build();
    private final ShuffleboardValue<Double> powerWriter =   
        ShuffleboardValue.create(0.0, "Power", SwerveDrive.class.getSimpleName()).build();
    
    
    
    public SixWheel(Boolean isEnabled){
        this.isEnabled.set(isEnabled);
        frontLeft = new SafeCanSparkMax(4, MotorType.kBrushless, 
            false, IdleMode.Coast, 
            1,1, 
            this.isEnabled, ShuffleboardValue.create(0.0, "Motor/Drive Voltage FL", SixWheel.class.getSimpleName())
            .build());
        frontRight = new SafeCanSparkMax(6, MotorType.kBrushless, 
            false, IdleMode.Coast, 
            1,1, 
            this.isEnabled, ShuffleboardValue.create(0.0, "Motor/Drive Voltage FR", SixWheel.class.getSimpleName())
            .build());
        backLeft = new SafeCanSparkMax(8, MotorType.kBrushless, 
            false, IdleMode.Coast, 
            1,1, 
            this.isEnabled, ShuffleboardValue.create(0.0, "Motor/Drive Voltage BL", SixWheel.class.getSimpleName())
            .build());
        backRight = new SafeCanSparkMax(22, MotorType.kBrushless, 
            false, IdleMode.Coast, 
            1,1, 
            this.isEnabled, ShuffleboardValue.create(0.0, "Motor/Drive Voltage BR", SixWheel.class.getSimpleName())
            .build());
        pigeon2 = new Pigeon2(4);

    }

    @Override
    public void periodic(){
        updateOdometry();
        powerWriter.set(frontLeft.getSpeed());
    }

    public void updateOdometry(){
        headingWriter.set(Math.IEEEremainder(pigeon2.getYaw().getValueAsDouble(), 360));
        rollWriter.set(Math.IEEEremainder(pigeon2.getRoll().getValueAsDouble(), 360));
        pitchWriter.set(Math.IEEEremainder(pigeon2.getPitch().getValueAsDouble(), 360));
        odometry.update(
            new Rotation2d(headingWriter.get()),
            leftEncoder.getDistance(),
            rightEncoder.getDistance()
        );
        locationWriter.set(odometry.toString());
    }
    public void reset(){
        pigeon2.setYaw(0);
    }
    public Command setPower(double power){
        return new InstantCommand(
            ()->{
                frontLeft.setPower(power);
                powerWriter.set(power);
            }
        );
    }

    public void arcadeDrive(double fwd, double rot) {
        drive.arcadeDrive(fwd, rot);
    }

    public void slowDrive() {
        drive.setMaxOutput(.3);
    }

    public void normalDrive() {
            drive.setMaxOutput(1);
    }

    public void turboDrive() {
        drive.setMaxOutput(3);
    }

    
    public void curvatureDrive(double fwd, double rot) {
        drive.curvatureDrive(fwd, rot, true);
    }

    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param leftVolts  the commanded left output
     * @param rightVolts the commanded right output
     */
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        frontLeft.setVoltage(leftVolts);
        backLeft.setVoltage(leftVolts);
        frontRight.setVoltage(rightVolts);
        backRight.setVoltage(rightVolts);
    }

    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    /**
     * Gets the average distance of the two encoders.
     *
     * @return the average of the two encoder readings
     */
    public double getAverageEncoderDistance() {
        return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.0;
    }

    /**
     * Gets the left drive encoder.
     *
     * @return the left drive encoder
     */
    public Encoder getLeftEncoder() {
        return leftEncoder;
    }

    /**
     * Gets the right drive encoder.
     *
     * @return the right drive encoder
     */
    public Encoder getRightEncoder() {
        return rightEncoder;
    }

    /**
     * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
     *
     * @param maxOutput the maximum output to which the drive will be constrained
     */
    public void setMaxOutput(double maxOutput) {
        drive.setMaxOutput(maxOutput);
    }



    
    public DifferentialDriveKinematics getKinematics() {
        return driveKinematics;
    }
    


    public void antiTipArcadeDrive(double xAxisRate, double zAxisRate) {
        drive.setSafetyEnabled(true);

        // double xAxisRate            = stick.getX();
        // double yAxisRate            = stick.getY();
        double rollAngleDegrees    = rollWriter.get();
        // double rollAngleDegrees     = navx.getRoll();
        final double kOffBalanceAngleThresholdDegrees = 5;
        final double kOonBalanceAngleThresholdDegrees  = 2;
        boolean fixBalance = false;
        
        if ( !fixBalance && 
                (Math.abs(rollAngleDegrees) >= 
                Math.abs(kOffBalanceAngleThresholdDegrees))) {
            fixBalance = true;
        }
        else if ( fixBalance && 
                    (Math.abs(rollAngleDegrees) <= 
                    Math.abs(kOonBalanceAngleThresholdDegrees))) {
            fixBalance = false;
        }
        
        // Control drive system automatically, 
        // driving in reverse direction of pitch/roll angle,
        // with a magnitude based upon the angle
        
        if (fixBalance) {
            double pitchAngleRadians = rollAngleDegrees * (Math.PI / 180.0);
            xAxisRate = Math.sin(pitchAngleRadians) * -1;
        }
        drive.arcadeDrive(xAxisRate, zAxisRate);
    }

    public void encoderDrive(double leftInches, double rightInches, double leftSpeed, double rightSpeed) {
        double leftTarget = leftEncoder.getDistance() + leftInches;
        double rightTarget = rightEncoder.getDistance() + rightInches;

        while (leftEncoder.getDistance() < leftTarget || rightEncoder.getDistance() < rightTarget) {
            if (leftEncoder.getDistance() < leftTarget) {
                frontLeft.setPower(leftSpeed);
                frontRight.setPower(leftSpeed);
            }
            if (rightEncoder.getDistance() < rightTarget) {
                frontLeft.setPower(rightSpeed);
                frontRight.setPower(rightSpeed);
            } 
        }
        tankDriveVolts(0, 0);
    }

    public void encoderDrive(double leftInches, double rightInches) {
        encoderDrive(leftInches, rightInches, 0.25, 0.25);
    }

}
