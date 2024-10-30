package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.utility.motor.old.SafeCanSparkMax;
import frc.robot.utility.motor.old.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class OwnSixWheel extends SubsystemBase {
    private final SafeCanSparkMax frontLeftMotor = new SafeCanSparkMax(
        3,
        MotorType.kBrushed,
        false,
        IdleMode.Coast,
        1,
        1/60,
        ShuffleboardValue.create(true, "OwnSixWheel/Is Enabled Wheelfl", SixWheel.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
        ShuffleboardValue.create(0.0, "OwnSixWheel/Output Writerfl", SixWheel.class.getSimpleName())
                .build() );
        
    private final SafeCanSparkMax frontRightMotor = new SafeCanSparkMax(1,
        MotorType.kBrushed,
        false,
        IdleMode.Coast,
        1,
        1/60,
        ShuffleboardValue.create(true, "OwnSixWheel/Is Enabled Wheelfr", SixWheel.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
        ShuffleboardValue.create(0.0, "OwnSixWheel/Output Writerfr", SixWheel.class.getSimpleName())
                .build() );

    private final SafeCanSparkMax backLeftMotor = new SafeCanSparkMax(
        4,
        MotorType.kBrushed,
        false,
        IdleMode.Coast,
        1,
        1/60,
        ShuffleboardValue.create(true, "OwnSixWheel/Is Enabled Wheelbl", SixWheel.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
        ShuffleboardValue.create(0.0, "OwnSixWheel/Output Writerbl", SixWheel.class.getSimpleName())
                .build() );

    private final SafeCanSparkMax backRightMotor = new SafeCanSparkMax(
        2,
        MotorType.kBrushed,
        false,
        IdleMode.Coast,
        1,
        1/60,
        ShuffleboardValue.create(true, "OwnSixWheel/Is Enabled Wheelbr", SixWheel.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
        ShuffleboardValue.create(0.0, "OwnSixWheel/Output Writerbr", SixWheel.class.getSimpleName())
                .build() );
 
    public final DifferentialDrive drive = new DifferentialDrive(frontLeftMotor.getSparkMax(), frontRightMotor.getSparkMax());

    public OwnSixWheel() {
        backLeftMotor.follow(frontLeftMotor, false);
        backRightMotor.follow(frontRightMotor, false);
    }

    public void drive(double setPowerX, double setPowerRotation) {
        drive.arcadeDrive(setPowerX, setPowerRotation);
    }
}

