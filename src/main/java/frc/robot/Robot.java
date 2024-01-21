// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.shooter.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
    // private final SwerveDrive drive = new SwerveDrive(true);
    // private final VerticalElevator verticalElevator = new VerticalElevator(true, true);
    // private final VerticalElevatorSetPower verticalElevatorSetPower = new VerticalElevatorSetPower();
    // private final HorizontalElevator horizontalElevator = new HorizontalElevator(true);
    // private final PivotAbsolute pivot = new PivotAbsolute(true);
    // private final Intake intake = new Intake(true);
    // private final Arm arm = new Arm(verticalElevator, horizontalElevator, pivot, intake);
    private final Shooter shooter = new Shooter(true);
    // private Field2d field = new Field2d(); //TODO:How does this work
    // private RobotContainer robotContainer = new RobotContainer(shooter);
    
    private final CommandXboxController operator =
        new CommandXboxController(DroidRageConstants.Gamepad.OPERATOR_CONTROLLER_PORT);
    // private AutoChooser autoChooser;
    private Command autonomousCommand;
    // private Light light = new Light( );
  
  /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     * Instantiate our RobotContainer.  This will perform all our button bindings, and put our
     * autonomous chooser on the dashboard.
     */
    @Override
    public void robotInit() {
        
        // PathPlannerServer.startServer(5811); // Use to see the Path of the robot on PathPlanner
    //    PathPlannerLogging.setLogActivePathCallback((poses) -> field.getObject("path").setPoses(poses));
        // autoChooser = new AutoChooser();
    }
    
    /**
     * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     * Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
     * commands, running already-scheduled commands, removing finished or interrupted commands,
     * and running subsystem periodic() methods.  This must be called from the robot's periodic
     * block in order for anything in the Command-based framework to work.
     */
    @Override
    public void robotPeriodic() {
        
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {

    }
    
    @Override
    public void disabledPeriodic() {
        // if(RobotController.getBatteryVoltage()<11.5){
        //     light.setAllColor(light.batteryBlue);
        // } else{
        //     light.flashingColors(light.yellow, light.blue);
        // }
        
        //TODO: Add lights to have the robot tell us any errors with can, etc.
        // light.rainbow();
        // light.orangeAndBlue();
        // light.switchLeds();
        // light.chaseLED( 1);
    }

    @Override
    public void autonomousInit() {
        CommandScheduler.getInstance().cancelAll();
        // autonomousCommand = autoChooser.getAutonomousCommand();
        autonomousCommand = new InstantCommand();

        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
        // if(DriverStation.isEStopped()){ //Robot Estopped
        //     light.flashingColors(light.red, light.white);
        // }
    }
    
    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll();
        // robotContainer.configureTeleOpBindings(
        //     // drive
        //     );

            operator.rightTrigger().onTrue(shooter.setTargetVelocity(Shooter.ShooterSpeeds.AMP))
        .onFalse(shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP));
        operator.leftTrigger().onTrue(shooter.setTargetVelocity(Shooter.ShooterSpeeds.SPEAKER))
        .onFalse(shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP));
        // robotContainer.configureTeleOpDriverOnlyBindings();
    }

    @Override
    public void teleopPeriodic() {
        // robotContainer.teleopPeriodic();
        // if(DriverStation.isEStopped()){ //Robot Estopped
        //     light.flashingColors(light.red, light.white);
        // }
    }
    
    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
        // robotContainer.configureTestBindings();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}
}
