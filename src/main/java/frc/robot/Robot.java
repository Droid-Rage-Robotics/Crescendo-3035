// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeWheel;
import frc.robot.subsystems.intake.dropDown.IntakeDropDown;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
    // private final SwerveDrive drive = new SwerveDrive(true);
    private final IntakeWheel intakeWheel = new IntakeWheel(true);
    private final IntakeDropDown dropDown = new IntakeDropDown(false);
    private final Intake intake = new Intake(dropDown, intakeWheel);
    // private final Shooter shooter = new Shooter(true);
    // private final ClawElevator clawElevator = new ClawElevator(false, false);
    // private final ClawPivot clawPivot = new ClawPivot(false);
    // private final ClawIntake clawIntake = new ClawIntake(false);
    // private final Claw claw = new Claw(clawElevator, clawPivot, clawIntake);
    // private final Climb climb = new Climb(true, true);
    // private final Vision vision = new Vision();
    // private final Light light = new Light();
    // private AutoChooser autoChooser = new AutoChooser();
    
    // private Field2d field = new Field2d(); //TODO:How does this work
    private RobotContainer robotContainer = new RobotContainer();
        
    private Command autonomousCommand;
  
  /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     * Instantiate our RobotContainer.  This will perform all our button bindings, and put our
     * autonomous chooser on the dashboard.
     */
    @Override
    public void robotInit() {
        // PathPlannerServer.startServer(5811); // Use to see the Path of the robot on PathPlanner
        // PathPlannerLogging.setLogActivePathCallback((poses) -> field.getObject("path").setPoses(poses));
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
        // drive.setYawCommand(
        //     switch (DriverStation.getRawAllianceStation()) {
        //         case Unknown -> 0;//180
        //         case Blue1,Blue2,Blue3 -> 0;
        //         case Red1,Red2,Red3 -> 0;
        //     }
        // );


        // robotContainer.configureTestMotorBindings(
        //     new SafeCanSparkMax(
        //         2, 
        //         MotorType.kBrushless,
        //         ShuffleboardValue.create(true, "Is Enabled", Shooter.class.getSimpleName())
        //             .withWidget(BuiltInWidgets.kToggleSwitch)
        //             .build(),
        //         ShuffleboardValue.create(0.0, "VoltageL", Shooter.class.getSimpleName())
        //             .build())
        // );
        // robotContainer.configureTalonMotorBindings(
        //     new SafeTalonFX(
        //     54,
        //     true,
        //     IdleMode.Coast,
        //     ShuffleboardValue.create(true, "Is Enabled", IntakeWheel.class.getSimpleName())
        //             .withWidget(BuiltInWidgets.kToggleSwitch)
        //             .build(),
        //         ShuffleboardValue.create(0.0, "Voltage", IntakeWheel.class.getSimpleName())
        //             .build()
        // )
        // );
        // robotContainer.configureTeleOpBindings(drive, intake, shooter, claw, climb, vision, light);
        robotContainer.configureIntakeTestBindings(intake);
        // robotContainer.configureShooterTestBindings(shooter);
        // robotContainer.configureDriveBindings(drive);
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
