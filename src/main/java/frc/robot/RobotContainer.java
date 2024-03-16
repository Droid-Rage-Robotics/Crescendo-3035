package frc.robot;

import edu.wpi.first.math.proto.Controller;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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
import frc.robot.commands.TeleopShoot;
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
		//AmpMech ampMech, Climb climb, 
		 CycleTracker cycleTracker){
		// drive.setYawCommand(-90);
		// climb.setDefaultCommand(new ManualClimb(climb, operator::getRightY, intake));

		
		// light.setDefaultCommand(new LightCommand(intake, light, driver, operator));
		// intake.getIntakeWheel().setDefaultCommand(new IntakeElementInCommand(intake));

		drive.setDefaultCommand(
			new SwerveDriveTeleop( //Slow Mode and Gyro Reset in the Default Command
				drive,
				driver::getLeftX,
				driver::getLeftY,
				driver::getRightX,
				driver.rightBumper(),
				driver.b()
				// 
				)
			);
		// drive.driveAutoReset();
		// driver.povUp().onTrue(new InstantCommand(()->drive.setYawCommand(0)));
		// driver.povDown().onTrue(new InstantCommand(()->drive.setYawCommand(90)));
		// driver.povLeft().onTrue(new InstantCommand(()->drive.setYawCommand(180)));
		// driver.povRight().onTrue(new InstantCommand(()->drive.setYawCommand(-90)));

		driver.rightTrigger().whileTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
			.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
		driver.leftTrigger().whileTrue(intake.setPositionCommand(Intake.Value.OUTTAKE))
			.onFalse(intake.setPositionCommand(Intake.Value.HOLD));

		driver.povUp().onTrue(
			new ParallelCommandGroup(
				intake.setPositionCommand(Intake.Value.CLIMB)
				// ampMech.setPositionCommand(AmpMech.Value.CLIMB)
			)
		);
		

		operator.rightTrigger()
			.onTrue(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_HOLD, shooter, ShooterSpeeds.SPEAKER_SHOOT))
			.onFalse(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_HOLD, shooter, ShooterSpeeds.STOP));
		operator.rightBumper()
			.onTrue(new TeleopShoot(intake, shooter));
			// .onFalse(new SetIntakeAndShooter(intake, Intake.Value.HOLD, shooter, ShooterSpeeds.HOLD));
		operator.leftTrigger()
			.onTrue(intake.setPositionCommand(Intake.Value.OUTTAKE_AMP))
			// .onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
			.onFalse(intake.setPositionCommand(Intake.Value.HOLD));

		
		// operator.y()			
		// .onTrue(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_TRANSFER, shooter, ShooterSpeeds.AMP_SHOOT))
		// 			.onFalse(new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_HOLD, shooter, ShooterSpeeds.STOP));


		// operator.leftTrigger()
		// 	.onTrue(ampMech.setPositionCommand(AmpMech.Value.AMP))
		// 	.onFalse(ampMech.setPositionCommand(AmpMech.Value.START));
		
		
		// operator.a()
		// 	.onTrue(new TransferToAmpMech(intake, shooter, ampMech))
		// 	.onFalse(new SetIntakeAndShooter(intake, Intake.Value.HOLD, shooter, ShooterSpeeds.HOLD))
		// 	.onFalse(ampMech.setPositionCommand(AmpMech.Value.HOLD));
		
		

		operator.povUp()
			.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(50)));
		operator.povDown()
			.onTrue(shooter.runOnce(()->shooter.addShooterSpeed(-50)));
		
		

		// operator.rightTrigger()
		// 	.onTrue(new AutoAim(drive, vision, light)
		// 		.alongWith(shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT))
		// 		.andThen(intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER)))
		// 	.onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
		// 		.alongWith(shooter.setTargetVelocity(ShooterSpeeds.HOLD))
		// 		.alongWith(new InstantCommand(()->new AutoAim(drive, vision, light).cancel())));//Not Sure if this Works
		// operator.leftTrigger()
		// 	.onTrue(ampMech.setPositionCommand(AmpMech.Value.INTAKE_SHOOTER)
		// 	.alongWith(shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT))
		// 	.alongWith(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
		// 	);
		// operator.start().onTrue(new ClimbAndScoreSequence(ampMech, climb, intake));
	}

	public void teleopPeriodic(Intake intake, Shooter shooter){
		if(intake.isElementInClaw()){
			driver.getHID().setRumble(RumbleType.kBothRumble, 1);
		}
		if(shooter.isShooterReadyToShootSpeaker()){
			driver.getHID().setRumble(RumbleType.kBothRumble, 1);
		}
		
	}
}
