package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.IntakeElementInCommand;
import frc.robot.commands.climbAndAmp.ClimbAndTrap;
import frc.robot.commands.climbAndAmp.DropAmp;
import frc.robot.commands.climbAndAmp.TransferToAmpMech;
import frc.robot.commands.manual.ManualClimb;
import frc.robot.commands.manual.ManualElevator;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.commands.shooter.SetIntakeAndShooter;
import frc.robot.commands.shooter.TeleopShoot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.climb.ClimbAlternate;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.InfoTracker.CycleTracker;

public class RobotContainer {
	private final CommandXboxController driver =
		new CommandXboxController(DroidRageConstants.Gamepad.DRIVER_CONTROLLER_PORT);
	private final CommandXboxController operator =
		new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);
	// PS4Controller controller = new PS4Controller(0);
	// CommandPS5Controller;
	// CommandStadiaController

	public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(true);
	}

	//Add Reset encoder buttons
	//Add Manual Control
	public void configureTeleOpBindings(SwerveDrive drive, Intake intake, Shooter shooter, 
		AmpMech ampMech, ClimbAlternate climb, 
		 CycleTracker cycleTracker){
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
				new DropAmp(ampMech, cycleTracker),//true
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

		operator.rightTrigger()
			.onTrue(new TeleopShoot(intake, shooter, cycleTracker, ampMech)
			
			)
			.onFalse(new SequentialCommandGroup(
				new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_HOLD, shooter, ShooterSpeeds.HOLD)
                // ampMech.setPositionCommand(AmpMech.Value.START)

			));
		operator.leftTrigger()
			.onTrue(ampMech.setPositionCommand(AmpMech.Value.HOLD_AMP));
		operator.a()
			.onTrue(new TransferToAmpMech(intake, shooter, ampMech));
		operator.povUp()
			.onTrue(climb.runOnce(()->climb.setTargetPosition(Climb.Position.CLIMB)))
			.onTrue(intake.setPositionCommand(Intake.Value.CLIMB_DOWN));
		operator.povDown()
			// .onTrue(intake.setDropIdleMode(frc.robot.utility.motor.SafeMotor.IdleMode.Brake))
			.onTrue(
				new SequentialCommandGroup(
					new ClimbAndTrap(intake, shooter, ampMech, climb)
					// new InstantCommand(()->intake.getDropDown().setMotorMode(frc.robot.utility.motor.SafeMotor.IdleMode.Brake))
				)
				
			);
		operator.povRight().onTrue(
			new InstantCommand(()->intake.getDropDown().setMotorMode(frc.robot.utility.motor.SafeMotor.IdleMode.Brake))

		);

		operator.povLeft()
		.onTrue(climb.runOnce(()->climb.setTargetPosition(Climb.Position.START)));
		
		// operator.rightBumper()
		// 	.onTrue(	

		// 	new SequentialCommandGroup(
		// 	ampMech.setPositionCommand(AmpMech.Value.OUT),//STAGE
		// 	shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT))

		// 	)
		// 	);
		operator.y()
			.onTrue(

			new SequentialCommandGroup(
			ampMech.setPositionCommand(AmpMech.Value.OUT),
			shooter.runOnce(()->shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT))

			)
			);
		// operator.povUp()
		// 	.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(50)));
		// operator.povDown()
		// 	.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(-50)));

		operator.start()
			.onTrue(new InstantCommand(()->climb.resetEncoder()));
		operator.back()
			.onTrue(ampMech.resetElevator());

		// operator.rightTrigger()
		// 	.onTrue(new AutoAim(drive, vision, light)
		// 		.alongWith(shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT))
		// 		.andThen(intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)))
		// 	.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
		// 		.alongWith(shooter.setTargetVelocity(ShooterSpeeds.HOLD))
		// 		.alongWith(new InstantCommand(()->new AutoAim(drive, vision, light).cancel())));//Not Sure if this Works
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
}
