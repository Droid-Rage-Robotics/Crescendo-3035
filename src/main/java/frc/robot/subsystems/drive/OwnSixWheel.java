package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class OwnSixWheel extends SubsystemBase {
    private final SafeCanSparkMax frontLeftMotor = new SafeCanSparkMax(
        3,
        MotorType.kBrushless,
        false,
        IdleMode.Coast,
        1,
        1/60,
        ShuffleboardValue.create(true, "SixWheel/Is Enabled Wheel", SixWheel.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
        ShuffleboardValue.create(0.0, "SixWheel/Output Writer", SixWheel.class.getSimpleName())
                .build() );
        
    private final SafeCanSparkMax frontRightMotor = new SafeCanSparkMax(1,
        MotorType.kBrushless,
        false,
        IdleMode.Coast,
        1,
        1/60,
        ShuffleboardValue.create(true, "SixWheel/Is Enabled Wheel", SixWheel.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
        ShuffleboardValue.create(0.0, "SixWheel/Output Writer", SixWheel.class.getSimpleName())
                .build() );

    private final SafeCanSparkMax backLeftMotor = new SafeCanSparkMax(
        4,
        MotorType.kBrushless,
        false,
        IdleMode.Coast,
        1,
        1/60,
        ShuffleboardValue.create(true, "SixWheel/Is Enabled Wheel", SixWheel.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
        ShuffleboardValue.create(0.0, "SixWheel/Output Writer", SixWheel.class.getSimpleName())
                .build() );

    private final SafeCanSparkMax backRightMotor = new SafeCanSparkMax(
        2,
        MotorType.kBrushless,
        false,
        IdleMode.Coast,
        1,
        1/60,
        ShuffleboardValue.create(true, "SixWheel/Is Enabled Wheel", SixWheel.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
        ShuffleboardValue.create(0.0, "SixWheel/Output Writer", SixWheel.class.getSimpleName())
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

