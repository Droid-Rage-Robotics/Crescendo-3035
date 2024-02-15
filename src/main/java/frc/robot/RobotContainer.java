package frc.robot;

import edu.wpi.first.units.Velocity;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.IntakeElementInCommand;
import frc.robot.commands.LightCommand;
import frc.robot.commands.drive.AutoAim;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.vision.Vision;
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

	private final SwerveDrive drive;
	// private final Intake intake;
	// private final Shooter shooter;
	// private final Claw claw;
	// private final Vision vision;
	// private final Light light;
	public RobotContainer(
		SwerveDrive drive
		// Intake intake,
		// Shooter shooter
		// Claw claw,
		// Vision vision,
		// Light light
		) {
			this.drive = drive;
			// this.intake = intake;
			// this.shooter = shooter;
			// this.claw = claw;
			// this.vision = vision;
			// this.light = light;
			DriverStation.silenceJoystickConnectionWarning(true);
	}

	public void configureTeleOpBindings() {

	// 	DriverStation.silenceJoystickConnectionWarning(true);
	// 	light.setDefaultCommand(new LightCommand(intake, light, driver, operator));
	// 	intake.getIntakeWheel().setDefaultCommand(new IntakeElementInCommand(intake));

	// 	drive.setDefaultCommand(
	// 		new SwerveDriveTeleop(
	// 			drive,
	// 			driver::getLeftX,
	// 			driver::getLeftY,
	// 			driver::getRightX,
	// 			driver.rightBumper()
	// 			)
	// 		);

	// 	driver.rightTrigger().onTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
	// 		.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
	// 	driver.rightTrigger().onTrue(intake.setPositionCommand(Intake.Value.OUTTAKE))
	// 		.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));

	// 	operator.rightTrigger().onTrue(new AutoAim(drive, vision, light)
	// 		.andThen(shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT))
	// 		.andThen(intake.setPositionCommand(Intake.Value.INTAKE_GROUND)));

		
	// 		//Trap
	// 		// Climb

	}

	// public void configureShooterTestBindings(){
	// 	operator.rightTrigger().onTrue(shooter.setTargetVelocity(Shooter.ShooterSpeeds.AMP_SHOOT))
	// 		.onFalse(shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP));
	// 	operator.leftTrigger().onTrue(shooter.setTargetVelocity(Shooter.ShooterSpeeds.SPEAKER_SHOOT))
	// 		.onFalse(shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP));
	// }

	public void configureDriveBindings() {

			drive.setDefaultCommand(
				new SwerveDriveTeleop(
					drive,
					driver::getLeftX,
					driver::getLeftY,
					driver::getRightX,
					driver.rightBumper()
			));
				
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
