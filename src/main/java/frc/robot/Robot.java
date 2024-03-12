// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.logging.Logger;

import com.ctre.phoenix6.SignalLogger;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.SysID.SysID;
import frc.robot.SysID.SysID.Measurement;
import frc.robot.commands.autos.AutoChooser;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.claw.ClawElevator;
import frc.robot.subsystems.claw.PowerClawIntake;
import frc.robot.subsystems.claw.clawArm.ClawArm;
import frc.robot.subsystems.claw.clawArm.ClawArmAbsolute;
import frc.robot.subsystems.claw.clawPivot.ClawPivot;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeWheel;
import frc.robot.subsystems.intake.dropDown.IntakeDropDown;
import frc.robot.subsystems.intake.dropDown.IntakeDropDownAbsolute;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.InfoTracker.CycleTracker;
import frc.robot.utility.InfoTracker.CycleTracker3;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeTalonFX;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
//CAN 15 is skipped
public class Robot extends TimedRobot {
    //15 missing
    // private final SwerveDrive drive = new SwerveDrive(false);//2-10
    // private final Shooter shooter = new Shooter(true);//18.19

    private final Climb climb = new Climb(false, false);//20,21^
    private final IntakeWheel intakeWheel = new IntakeWheel(false);//16
    private final IntakeDropDownAbsolute dropDown = new IntakeDropDownAbsolute(false, climb.getMotorL());//17-could use drive motor instead
    private final Intake intake = new Intake(dropDown, intakeWheel);
    
    // private final ClawElevator clawElevator = new ClawElevator(false);//22
    // private final ClawArmAbsolute clawArm = new ClawArmAbsolute(false);//23 - not connected: temp is 60
    // private final ClawPivot clawPivot = new ClawPivot(false);//24 - not connected
    // private final PowerClawIntake clawIntake = new PowerClawIntake(false);//25       
    // private final Claw claw = new Claw(clawElevator, clawArm, clawPivot, clawIntake);
    
    
    // private final Vision vision = new Vision();
    // private final Light light = new Light();
    // private AutoChooser autoChooser = new AutoChooser(
    //     drive//, intake, shooter, claw, climb, vision, light
    // );
    // private final CycleTracker3 cycleTracker = new CycleTracker3();

    // private final SysID sysID = new SysID(climb.getMotorL(), climb.getMotorR(), Measurement.ANGLE);
    // private final SysID sysID = new SysID(claw.getClawIntake().getMotor(), Measurement.DISTANCE);
    // private final SysID sysID = new SysID(clawElevator.getMotor(), Measurement.DISTANCE);
    // private final SysID sysID = new SysID(clawIntake.getMotor(), Measurement.DISTANCE);
    // private final SysID sysID = new SysID(dropDown.getMotor(), Measurement.ANGLE);



    private Field2d field = new Field2d(); //TODO:How does this work
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
        PathPlannerLogging.setLogCurrentPoseCallback((pose) -> {
            // Do whatever you want with the pose here
            field.setRobotPose(pose);
        });
        PathPlannerLogging.logCurrentPose(field.getRobotPose()); 
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
        // if(DriverStation.isEStopped()){ //Robot Estopped
        //     light.flashingColors(light.red, light.white);
        // }
    }

    @Override
    public void disabledInit() {
        // cycleTracker.printAllData();

    }
    
    @Override
    public void disabledPeriodic() {
        if(RobotController.getBatteryVoltage()<11.5){
            // light.setAllColor(light.batteryBlue);
            // drive.playMusic(2);
        } else{
            // light.flashingColors(light.yellow, light.blue);
        }
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
        // claw.setPositionCommand(Claw.Value.START);
        // drive.setYawCommand(
        //     switch (DriverStation.getRawAllianceStation()) {
        //         case Unknown -> 0;//180
        //         case Blue1,Blue2,Blue3 -> 0;
        //         case Red1,Red2,Red3 -> 0;
        //     }
        // );


        // robotContainer.configureSparkMaxMotorBindings(
        //     new SafeCanSparkMax(
        //             25,
        //             MotorType.kBrushless,
        //             true,
        //             IdleMode.Coast,
        //             1,
        //             1,
        //             ShuffleboardValue.create(true, "Claw Intake Is Enabled", Claw.class.getSimpleName())
        //                     .withWidget(BuiltInWidgets.kToggleSwitch)
        //                     .build(),
        //                 ShuffleboardValue.create(0.0, "Claw Intake Voltage", Claw.class.getSimpleName())
        //                     .build()
        //         )
        // );

        // robotContainer.configureTalonMotorBindings(
        //     new SafeTalonFX(
        //     16,
        //     true,
        //     IdleMode.Coast,
        //     1,
        //     1,
        //     ShuffleboardValue.create(true, "Is Enabled Wheel", Intake.class.getSimpleName())
        //         .withWidget(BuiltInWidgets.kToggleSwitch)
        //         .build(),
        //     ShuffleboardValue.create(0.0, "Voltage Wheel", Intake.class.getSimpleName())
        //         .build()
        // )
        // );

        // robotContainer.configureTeleOpBindings(drive, intake, shooter, claw, climb, vision, light, cycleTracker);
        // robotContainer.configureIntakeTestBindings(intake);
        // robotContainer.configureCycleTrackerBindings(cycleTracker);

        robotContainer.configureClimbTestBindings(climb, intake);
        // robotContainer.configureIntakeAndShooterTestBindings(intake, shooter);
        // robotContainer.configureShooterTestBindings(shooter);
        // robotContainer.configureClawTestBindings(claw);

		// new InstantCommand(()->intake.setPositionCommand(Intake.Value.START));
		// new InstantCommand(()->claw.setPositionCommand(Claw.Value.START));//no work


        // robotContainer.configureDriveBindings(drive);
        // robotContainer.configureSysIDBindings(sysID);
    }

    @Override
    public void teleopPeriodic() {
        // robotContainer.teleopPeriodic();
    }
    
    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }
    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}

    @Override
    public void teleopExit(){
        // cycleTracker.printAllData();
    }
}
