package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.vision.Vision;
import frc.robot.commands.drive.AutoAim;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.Shooter.ShooterSpeeds;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class RobotContainer {
    private final CommandXboxController driver =
        new CommandXboxController(DroidRageConstants.Gamepad.DRIVER_CONTROLLER_PORT);
    private final CommandXboxController operator =
        new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);
    private ShuffleboardValue<Double> matchTime = ShuffleboardValue.create
        (0.0, "Match Time", "Misc")
        .withWidget(BuiltInWidgets.kTextView)
        .build();
    private final Shooter shooter;
    private final SwerveDrive drive;
    private final Vision vision;
    private final Light light;
    public RobotContainer(
        SwerveDrive drive,
        Shooter shooter,
        Vision vision,
        Light light
        ) {
            this.drive = drive;
            this.shooter = shooter;
            this.vision = vision;
            this.light = light;
    }

    public void configureTeleOpBindings() {
            
        DriverStation.silenceJoystickConnectionWarning(true);
        // light.setDefaultCommand(new LightCommand(intake, light, driver));
        
        drive.setDefaultCommand(
            new SwerveDriveTeleop(
                drive, 
                driver::getLeftX, 
                driver::getLeftY, 
                driver::getRightX,
                driver.rightBumper()
                )
            );
        operator.rightTrigger().onTrue(new AutoAim(drive, vision, light)
            .andThen(shooter.setTargetVelocity(ShooterSpeeds.SPEAKER)));
    }

    public void configureShooterTestBindings(){
        operator.rightTrigger().onTrue(shooter.setTargetVelocity(Shooter.ShooterSpeeds.AMP))
            .onFalse(shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP));
        operator.leftTrigger().onTrue(shooter.setTargetVelocity(Shooter.ShooterSpeeds.SPEAKER))
            .onFalse(shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP));
    }
    
    public void configureTestBindings(){}
    
    public Command getAutonomousCommand() {
        // return autoChooser.getSelected();
        return new InstantCommand();
    }

    public void teleopPeriodic() {
        matchTime.set(DriverStation.getMatchTime());
        // vision.periodic();
    }
}
