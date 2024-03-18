package frc.robot;

import edu.wpi.first.math.proto.Controller;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.CommandStadiaController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.DroidRageConstants.Gamepad;
import frc.robot.SysID.SysID;
import frc.robot.commands.ClimbAndScoreSequence;
import frc.robot.commands.IntakeElementInCommand;
import frc.robot.commands.LightCommand;
import frc.robot.commands.SetIntakeAndShooter;
import frc.robot.commands.TransferToAmpMech;
import frc.robot.commands.autos.AutoChooser;
import frc.robot.commands.manual.ManualClimb;
import frc.robot.commands.manual.SwerveDriveTeleop;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSpeeds;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.InfoTracker.CycleTracker;
import frc.robot.utility.InfoTracker.CycleTracker3;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class TestButton {
	private final CommandXboxController driver =
		new CommandXboxController(DroidRageConstants.Gamepad.DRIVER_CONTROLLER_PORT);
	private final CommandXboxController operator =
		new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);

	public TestButton() {
	}
	
	public void configureDriveBindings(SwerveDrive drive) {
		drive.setDefaultCommand(
			new SwerveDriveTeleop(
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver,
				true
		));
	}

	public void configureDriverOperatorBindings(SwerveDrive drive, Intake intake) {
		drive.setDefaultCommand(
			new SwerveDriveTeleop(
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver,
				true
		));

		driver.rightTrigger().whileTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		driver.leftTrigger().whileTrue(intake.setPositionCommand(Intake.Value.OUTTAKE))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
	}
	
	public void configureIntakeTestBindings(Intake intake){
		intake.setPositionCommand(Intake.Value.START);
		operator.rightTrigger()
			.whileTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
			.onFalse(intake.setPositionCommand(Intake.Value.START));
		operator.leftTrigger()
			.whileTrue(intake.setPositionCommand(Intake.Value.INTAKE_HUMAN))
			.onFalse(intake.setPositionCommand(Intake.Value.START));
	}
	
	public void configureShooterTestBindings(Shooter shooter){
		operator.rightTrigger().onTrue(shooter.runOnce(() -> shooter.setTargetVelocity(Shooter.ShooterSpeeds.AMP_SHOOT)))
			.onFalse(shooter.runOnce(() ->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)));
		operator.leftTrigger().onTrue(shooter.runOnce(() -> shooter.setTargetVelocity(Shooter.ShooterSpeeds.SPEAKER_SHOOT)))
			.onFalse(shooter.runOnce(() ->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)));
		
		operator.povUp()
			.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(50)));
		operator.povDown()
			.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(-50)));
	}

	public void configureClimbTestBindings(Climb climb, Intake intake){
		// operator.rightTrigger().onTrue(climb.runOnce(() -> climb.setTargetPosition(Climb.Position.CLIMB)))
		// 	.onFalse(climb.runOnce(() ->climb.setTargetPosition(Climb.Position.START)));
		// operator.leftTrigger().onTrue(climb.runOnce(() -> climb.setTargetPosition(Climb.Position.TRAP)))
		// 	.onFalse(climb.runOnce(() ->climb.setTargetPosition(Climb.Position.START)));

		climb.setDefaultCommand(new ManualClimb(climb, operator::getRightY, intake));
		
	}

	public void configureIntakeAndShooterTestBindings(Intake intake, Shooter shooter//, SwerveDrive drive
	){
		// operator.rightBumper()
		// 	.onTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
		// 	.onFalse(intake.setPositionCommand(Intake.Value.START));
		// operator.leftBumper()
		// 	.onTrue(intake.setPositionCommand(Intake.Value.INTAKE_HUMAN))
		// 	.onFalse(intake.setPositionCommand(Intake.Value.START));
		// operator.rightTrigger().onTrue(shooter.runOnce(() -> shooter.setTargetVelocity(Shooter.ShooterSpeeds.AMP_SHOOT)))
		// 	.onFalse(shooter.runOnce(() ->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)));
		// operator.leftTrigger().onTrue(shooter.runOnce(() -> shooter.setTargetVelocity(Shooter.ShooterSpeeds.SPEAKER_SHOOT)))
		// 	.onFalse(shooter.runOnce(() ->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)));

	// drive.setDefaultCommand(new SwerveDriveTeleop(drive, driver::getLeftX, 
	// 	driver::getLeftY, driver::getRightX, driver.rightBumper(), driver.a()));

		operator.rightBumper().whileTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		operator.leftBumper().whileTrue(intake.setPositionCommand(Intake.Value.OUTTAKE))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		

		operator.rightTrigger()
			.onTrue(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.SPEAKER_SHOOT))
			.onFalse(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_HOLD, shooter, ShooterSpeeds.HOLD));
		operator.leftTrigger()
			// .onTrue(new TransferToAmpMech(intake, shooter, ampMech))
			.onFalse(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_HOLD, shooter, ShooterSpeeds.HOLD));

			operator.povUp()
			.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(50)));
		operator.povDown()
			.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(-50)));
	}

	public void configureAmpMechTestBindings(AmpMech ampMech){
		// ampMech.setPositionCommand(AmpMech.Value.START);
		operator.rightTrigger()
		.onTrue(ampMech.setPositionCommand(AmpMech.Value.INTAKE_SHOOTER))
			.onFalse(ampMech.setPositionCommand(AmpMech.Value.START));
		operator.leftTrigger()
			.onTrue(ampMech.setPositionCommand(AmpMech.Value.HOLD))
			.onFalse(ampMech.setPositionCommand(AmpMech.Value.START));

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



	// public Command getAutonomousCommand(AutoChooser autoChooser) {
	// 	// return autoChooser.getSelected();
	// 	return new InstantCommand();
	// }

	public void configureSysIDBindings(SysID sysID){
		operator.x().onTrue(sysID.sysIdDynamic(Direction.kForward));
		operator.a().onTrue(sysID.sysIdDynamic(Direction.kReverse));
		operator.b().onTrue(sysID.sysIdQuasistatic(Direction.kForward));
		operator.y().onTrue(sysID.sysIdQuasistatic(Direction.kReverse));
		//	ONFALSE NO WORK, REMOVE ASAP @LUCKY
	}
	public void test(SwerveDrive drive, Intake intake, Shooter shooter, 
		Climb climb){
		// intake.setPositionCommand(Intake.Value.START);
		// ampMech.setPositionCommand(AmpMech.Value.START);//No work
		// new InstantCommand(()->intake.setPositionCommand(Intake.Value.START));
		climb.setDefaultCommand(new ManualClimb(climb, operator::getRightY, intake));


		drive.setDefaultCommand(
			new SwerveDriveTeleop( //Slow Mode and Gyro Reset in the Default Command
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver,
				true
		));

		driver.rightTrigger().whileTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		driver.leftTrigger().whileTrue(intake.setPositionCommand(Intake.Value.OUTTAKE))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		 

		operator.rightTrigger()
			.onTrue(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.SPEAKER_SHOOT))
			.onFalse(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_HOLD, shooter, ShooterSpeeds.HOLD));
		
		operator.povUp()
			.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(50)));
		operator.povDown()
			.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(-50)));
		
		

	}
}
