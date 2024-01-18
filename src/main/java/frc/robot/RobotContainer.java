package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class RobotContainer {
    // private final CycleTracker cycleTracker;
    // try{
    //     cycleTracker = new CycleTracker();
    // } catch(FileNotFoundException e){
        // System.err.println("BAD");
    // }
    private final CommandXboxController driver =
        new CommandXboxController(DroidRageConstants.Gamepad.DRIVER_CONTROLLER_PORT);
    private final CommandXboxController operator =
        new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);
    // private final Intake intake = new Intake(true);

    private ShuffleboardValue<Double> matchTime = ShuffleboardValue.create(0.0, "Match Time", "Misc")
        .withWidget(BuiltInWidgets.kTextView)
        .build();
    // SendableChooser<Command> autoChooser = new SendableChooser<Command>();

    private final Shooter shooter;// = new Shooter(true);
    // private  final Vision vision = new Vision();
    public RobotContainer(
        Shooter shooter
        ) {
        // ComplexWidgetBuilder.create(autoChooser, "Auto Chooser", "Misc")
        //     .withSize(1, 3);
        
this.shooter = shooter;
        // ComplexWidgetBuilder.create(CameraServer.startAutomaticCapture(), "USB Camera Stream", "Misc")   //Usb Streaming
        //     .withSize(5, 5);
        
    }

    public void configureTeleOpBindings(
        // SwerveDrive drive
        ) {
            
        DriverStation.silenceJoystickConnectionWarning(true);
        // light.setDefaultCommand(new LightCommand(intake, light, driver));
        
        // drive.setDefaultCommand(
        //     new SwerveDriveTeleop(
        //         drive, 
        //         driver::getLeftX, 
        //         driver::getLeftY, 
        //         driver::getRightX,
        //         driver.x()
        //         )
        //     );
        // driver.rightBumper()
        //     .whileTrue(drive.setSlowSpeed())
        //     .onFalse(drive.setNormalSpeed());
        // driver.leftTrigger().and(driver.leftBumper())
        //     .onTrue(new DropTeleopCone(arm, intake)) 
        //     .onFalse(intake.run(intake::stop))
        //     .onFalse(arm.setPositionCommand(Position.HOLD));
        // driver.leftTrigger().and(driver.leftBumper().negate())
        //     .onTrue(new DropTeleopCube(arm, intake))
        //     .onFalse(intake.run(intake::stop))
        //     .onFalse(arm.setPositionCommand(Position.HOLD));

            // driver.leftTrigger()
            //     .onTrue(new InstantCommand(()->cycleTracker.trackCycle(1)));
            // driver.rightTrigger()
            //     .onTrue(new InstantCommand(()->cycleTracker.trackCycle(1)));



        // driver.rightTrigger().onTrue(intake.setTargetVelocityCommand(Intake.Velocity.INTAKE));
        // driver.leftTrigger().onTrue(intake.setTargetVelocityCommand(Intake.Velocity.OUTTAKE));
        // driver.rightTrigger().onTrue(intake.setTargetVelocityCommand(Intake.Velocity.INTAKE));
        // driver.leftTrigger().onTrue(intake.setTargetVelocityCommand(Intake.Velocity.OUTTAKE));
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
