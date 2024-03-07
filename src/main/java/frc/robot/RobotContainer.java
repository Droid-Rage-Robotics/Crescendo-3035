package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.SysID.SysID;
import frc.robot.commands.ClimbAndScoreSequence;
import frc.robot.commands.IntakeElementInCommand;
import frc.robot.commands.LightCommand;
import frc.robot.commands.autos.AutoChooser;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.InfoTracker.CycleTracker;
import frc.robot.utility.InfoTracker.CycleTracker3;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeTalonFX;
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

	public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(true);
	}

	//Add Reset encoder buttons
	//Add Manual Control
	public void configureTeleOpBindings(SwerveDrive drive, Intake intake, Shooter shooter, 
		Claw claw, Climb climb, Vision vision, Light light, CycleTracker cycleTracker){
		light.setDefaultCommand(new LightCommand(intake, light, driver, operator));
		intake.getIntakeWheel().setDefaultCommand(new IntakeElementInCommand(intake));

		drive.setDefaultCommand(
			new SwerveDriveTeleop( //Slow Mode and Gyro Reset in the Default Command
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver.rightBumper(),
				driver.start()
				)
			);

		driver.rightTrigger().onTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		driver.leftTrigger().onTrue(intake.setPositionCommand(Intake.Value.OUTTAKE))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		

		
		// operator.rightTrigger()
		// 	.onTrue(new AutoAim(drive, vision, light)
		// 		.alongWith(shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT))
		// 		.andThen(intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)))
		// 	.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
		// 		.alongWith(shooter.setTargetVelocity(ShooterSpeeds.HOLD))
		// 		.alongWith(new InstantCommand(()->new AutoAim(drive, vision, light).cancel())));//Not Sure if this Works
		// operator.leftTrigger()
		// 	.onTrue(claw.setPositionCommand(Claw.Value.INTAKE_SHOOTER)
		// 	.alongWith(shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT))
		// 	.alongWith(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
		// 	);

		operator.start().onTrue(new ClimbAndScoreSequence(claw, climb, intake));

		
		// Trap
		// Climb

	}



	public void configureDriveBindings(SwerveDrive drive) {
		drive.setDefaultCommand(
			new SwerveDriveTeleop(
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver.rightBumper(),
				driver.start()
		));
	}
	
	public void configureIntakeTestBindings(Intake intake){
		// operator.rightTrigger()
		// 	.onTrue(new InstantCommand(()->intake.getIntakeWheel().setPower(.6)))
		// 	.onFalse(new InstantCommand(()->intake.getIntakeWheel().setPower(0)));
		operator.rightTrigger()
			.onTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
			.onFalse(intake.setPositionCommand(Intake.Value.START));
		operator.leftTrigger()
			.onTrue(intake.setPositionCommand(Intake.Value.INTAKE_HUMAN))
			.onFalse(intake.setPositionCommand(Intake.Value.START));
	}
	
	public void configureShooterTestBindings(Shooter shooter){
		operator.rightTrigger().onTrue(shooter.runOnce(() -> shooter.setTargetVelocity(Shooter.ShooterSpeeds.AMP_SHOOT)))
			.onFalse(shooter.runOnce(() ->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)));
		operator.leftTrigger().onTrue(shooter.runOnce(() -> shooter.setTargetVelocity(Shooter.ShooterSpeeds.SPEAKER_SHOOT)))
			.onFalse(shooter.runOnce(() ->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)));
	}

	public void configureClimbTestBindings(Climb climb){
		operator.rightTrigger().onTrue(climb.runOnce(() -> climb.setTargetPosition(Climb.Position.CLIMB)))
			.onFalse(climb.runOnce(() ->climb.setTargetPosition(Climb.Position.START)));
		operator.leftTrigger().onTrue(climb.runOnce(() -> climb.setTargetPosition(Climb.Position.TRAP)))
			.onFalse(climb.runOnce(() ->climb.setTargetPosition(Climb.Position.START)));
	}

	public void configureIntakeAndShooterTestBindings(Intake intake, Shooter shooter){
		operator.rightBumper()
			.onTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
			.onFalse(intake.setPositionCommand(Intake.Value.START));
		operator.leftBumper()
			.onTrue(intake.setPositionCommand(Intake.Value.INTAKE_HUMAN))
			.onFalse(intake.setPositionCommand(Intake.Value.START));
		operator.rightTrigger().onTrue(shooter.runOnce(() -> shooter.setTargetVelocity(Shooter.ShooterSpeeds.AMP_SHOOT)))
			.onFalse(shooter.runOnce(() ->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)));
		operator.leftTrigger().onTrue(shooter.runOnce(() -> shooter.setTargetVelocity(Shooter.ShooterSpeeds.SPEAKER_SHOOT)))
			.onFalse(shooter.runOnce(() ->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)));
	}
	public void configureClawTestBindings(Claw claw){
		operator.rightBumper()
			.onTrue(claw.setPositionCommand(Claw.Value.START));
		operator.leftBumper()
			.onTrue(claw.setPositionCommand(Claw.Value.INTAKE_HUMAN));
	}



	public void configureSparkMaxMotorBindings(SafeCanSparkMax motor){
		operator.rightTrigger().onTrue(new InstantCommand(()->motor.setPower(.6)))
			.onFalse(new InstantCommand(()->motor.setPower(0)));
		operator.leftTrigger().onTrue(new InstantCommand(()->motor.setPower(-.6)))
			.onFalse(new InstantCommand(()->motor.setPower(0)));
	}

	public void configureCycleTrackerBindings(CycleTracker3 cycleTracker){
		operator.rightTrigger().onTrue(new InstantCommand(()->cycleTracker.trackCycle(Shooter.ShooterSpeeds.AMP_SHOOT)));
		operator.leftTrigger()
			.onTrue(new InstantCommand(()->cycleTracker.trackCycle(Shooter.ShooterSpeeds.HOLD)));
	}
	
	public void configureTalonMotorBindings(SafeTalonFX motor){
		operator.rightTrigger()
			.onTrue(new InstantCommand(()-> motor.setPower(.4)))
			.onFalse(new InstantCommand(()-> motor.setPower(0)));
		operator.leftTrigger()
			.onTrue(new InstantCommand(()-> motor.setPower(-.3)))
			.onFalse(new InstantCommand(()-> motor.setPower(0)));

		operator.a().onTrue(new InstantCommand(()->motor.playMusic(3)));
		operator.b().onTrue(new InstantCommand(()->motor.stopMusic()));
	}



	public Command getAutonomousCommand(AutoChooser autoChooser) {
		// return autoChooser.getSelected();
		return new InstantCommand();
	}

	public void teleopPeriodic() {
		matchTime.set(DriverStation.getMatchTime());
		// vision.periodic();
	}

	public void configureSysIDBindings(SysID sysID){
		operator.x().onTrue(sysID.sysIdDynamic(Direction.kForward));
		operator.a().onTrue(sysID.sysIdDynamic(Direction.kReverse));
		operator.b().onTrue(sysID.sysIdQuasistatic(Direction.kForward));
		operator.y().onTrue(sysID.sysIdQuasistatic(Direction.kReverse));
		//	ONFALSE NO WORK, REMOVE ASAP @LUCKY
	}
}
