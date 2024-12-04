package frc.robot;

import edu.wpi.first.math.controller.PIDController;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.ClimbAndAmpCommands;
import frc.robot.commands.IntakeElementInCommand;
import frc.robot.commands.drive.AutoBalancetoAutoAim;
import frc.robot.commands.drive.SimpleAim;
import frc.robot.commands.manual.ManualClimb;
import frc.robot.commands.manual.ManualElevator;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.climb.ClimbAlternate;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.newSub.IntakeTest;
import frc.robot.subsystems.newSub.TestSubsystem;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.InfoTracker.CycleTracker;

public class RobotContainer {
	private final CommandXboxController driver =
		new CommandXboxController(DroidRageConstants.Gamepad.DRIVER_CONTROLLER_PORT);
	private final CommandXboxController operator =
		new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);
		// PIDController controller = new PIDController(.001, 0, 0);
		
	// CommandList commandList = new CommandList();
	
	// private SixWheel sixWheel = new SixWheel(false);

	public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(true);
	}

	//Add Reset encoder buttons
	//Add Manual Control
	public void configureTeleOpBindings(SwerveDrive drive, Intake intake, Shooter shooter, 
		AmpMech ampMech, ClimbAlternate climb, 
		 CycleTracker cycleTracker, Vision vision
		 ){

		
		// drive.setYawCommand(-90);
		climb.setDefaultCommand(new ManualClimb(climb, operator::getRightY, intake));
		ampMech.getElevator().setDefaultCommand(new ManualElevator(operator::getLeftY, ampMech));
		drive.setDefaultCommand(
			new SwerveDriveTeleop( //Slow Mode and Gyro Reset in the Default Command
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver,
				false//No Work; Do no use this
				)
			);
		driver.rightTrigger()
			.whileTrue(new IntakeElementInCommand(driver, intake))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));

		driver.rightTrigger().and(driver.leftBumper())
			.whileTrue(intake.setPositionCommand(Intake.Value.INTAKE_HOLD))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		driver.leftTrigger().whileTrue(
			new ConditionalCommand(
				ClimbAndAmpCommands.dropAmp(ampMech, cycleTracker),//true
				intake.setPositionCommand(Intake.Value.OUTTAKE), //false
				()->(AmpMech.Value.HOLD_AMP==ampMech.getPosition()||
					AmpMech.Value.AMP==ampMech.getPosition())))	//Check
			.onFalse(new SequentialCommandGroup(
				intake.setPositionCommand(Intake.Value.HOLD),				
				ampMech.setPositionCommand(AmpMech.Value.START)
				));
		driver.y().onTrue(
			ampMech.setPositionCommand(AmpMech.Value.OUT)
		);
		driver.x().onTrue(
				ampMech.setPositionCommand(AmpMech.Value.START)
			);
		operator.leftTrigger()
			.onTrue(ampMech.setPositionCommand(AmpMech.Value.HOLD_AMP));
		operator.a()
			.onTrue(ClimbAndAmpCommands.transferToAmpMech(intake, shooter, ampMech));
		operator.povUp()
			.onTrue(climb.runOnce(()->climb.setTargetPosition(Climb.Position.CLIMB)))
			.onTrue(intake.setPositionCommand(Intake.Value.CLIMB_DOWN));
		operator.povDown()
			// .onTrue(intake.setDropIdleMode(frc.robot.utility.motor.SafeMotor.IdleMode.Brake))
			.onTrue(
				new SequentialCommandGroup(
					ClimbAndAmpCommands.climbAndTrap(intake, shooter, ampMech, climb)
					// new InstantCommand(()->intake.getDropDown().setMotorMode(frc.robot.utility.motor.SafeMotor.IdleMode.Brake))
				)
		);
		operator.povRight().onTrue(
			new InstantCommand(()->intake.getDropDown().setMotorMode(frc.robot.utility.motor.old.SafeMotor.IdleMode.Brake))

		);
		operator.povLeft()
			.onTrue(climb.runOnce(()->climb.setTargetPosition(Climb.Position.START)));
		operator.y()
			.onTrue(
				new SequentialCommandGroup(
				ampMech.setPositionCommand(AmpMech.Value.OUT),
				shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT)),
				intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)
				)
			)
			.onFalse(
				new SequentialCommandGroup(
				// ampMech.setPositionCommand(AmpMech.Value.OUT),
				shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.STOP)),
				intake.setPositionCommand(Intake.Value.HOLD)
				)
			);
		operator.start()
			.onTrue(new InstantCommand(()->climb.resetEncoder()));
		operator.back()
			.onTrue(ampMech.resetElevator());

		operator.rightTrigger()
			.onTrue(new AutoBalancetoAutoAim(drive, vision));
		driver.rightTrigger();
			// .onTrue(new AlignToAprilTagSpectrum(vision, drive, ()->1));
			// .onTrue(new AutoAim(drive, vision));
			// .alongWith(new InstantCommand(()->new AutoAim(drive, vision, light).cancel())));//Not Sure if this Works
		// operator.start().onTrue(new ClimbAndScoreSequence(ampMech, climb, intake));

		// light.setDefaultCommand(new LightCommand(intake, light, driver, operator));
		// intake.getIntakeWheel().setDefaultCommand(new IntakeElementInCommand(intake));
	}

	public void teleopPeriodic(Intake intake, Shooter shooter){
		if(intake.isElementInClaw()){
			driver.getHID().setRumble(RumbleType.kBothRumble, 1);
		}
		if(shooter.isShooterReadyToShootSpeaker()){
			operator.getHID().setRumble(RumbleType.kBothRumble, 1);
		}
		
	}

	public void testCommands(
		// Vision vision, SwerveDrive drive
	// IntakeTest test
	TestSubsystem test
	){
		//setPower
		// driver.rightTrigger().whileTrue(test.setTargetPowerCommand(-.4))
		// .onFalse(test.setTargetPowerCommand(0));

		//arm
		driver.rightTrigger()
			.whileTrue(test.setTargetPositionCommand(AmpMech.Value.AMP.getElevatorInches()))
			.onFalse(test.setTargetPositionCommand(AmpMech.Value.START.getElevatorInches()));

		driver.leftTrigger().whileTrue(
			test.setTargetPositionCommand(10))
			.onFalse(
				test.setTargetPositionCommand(AmpMech.Value.START.getElevatorInches())
			);


		// controller.setTolerance(1);
		// drive.setDefaultCommand(
		// 	new SwerveDriveTeleop( //Slow Mode and Gyro Reset in the Default Command
		// 		drive,
		// 		driver::getLeftX,
		// 		driver::getLeftY,
		// 		driver::getRightX,
		// 		driver,
		// 		false//No Work; Do no use this
		// 		)
		// 	);
		// driver.rightTrigger()
		// 	.whileTrue(new SimpleAim(drive, vision))
		// 	.onFalse(new InstantCommand(()->drive.stop()));
		// 	// .onTrue(new AlignToAprilTag(()->driver.getLeftX(), 0, drive, vision));
		// 	// .onTrue(new InstantCommand(()->drive.drive(0, 0, controller.calculate(vision.gettX(), 0))
		// 	// ));
		// 	// driver.a()
		// 	// .onTrue(new InstantCommand());
		// 	// .onTrue(new Test(drive, vision, driver));
		// 	// .onTrue(new AlignToAprilTagSpectrum(vision, drive, ()->1));
		// 	// .onTrue(new AutoAim(drive, vision));
	}

	public void testDrive(SwerveDrive drive, Vision vision
	){
		drive.setDefaultCommand(
			new SwerveDriveTeleop( //Slow Mode and Gyro Reset in the Default Command
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver,
				false//No Work; Do no use this
				)
			);
		// drive.setDefaultCommand(new ManualSixWheel(drive, driver));
		driver.a().onTrue(new InstantCommand(()->drive.resetOdometry(vision.getPose())));
	}
}
