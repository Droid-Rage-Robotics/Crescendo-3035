package frc.robot.commands.autos;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.ResetPoseVision;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.drive.SwerveDriveConstants;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;

public class AutoChooser {
    public static final SendableChooser<Command> autoChooser = new SendableChooser<Command>();

    public AutoChooser(
        SwerveDrive drive, Vision vision   //, Intake intake, Shooter shooter, AmpMech ampMech//, Claw claw, Climb climb, Light light
    ) {
        NamedCommands.registerCommand("resetPose",
        new ResetPoseVision(drive, vision)
            
        );
//         //Put Named Commands HERE
//         NamedCommands.registerCommand("shoot",
//             intake.setPositionCommand(Intake.Value.AUTO_SHOOTER_TRANSFER)
//         );
//         NamedCommands.registerCommand("intake",
//              new SequentialCommandGroup(
//                 intake.setPositionCommand(Intake.Value.AUTO_INTAKE_GROUND)
// //                    new WaitCommand(.3)
//          ));
//         NamedCommands.registerCommand("pickUp", new SequentialCommandGroup(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)));

        createAutoBuilder(drive);
        ComplexWidgetBuilder.create(autoChooser, "Auto Chooser", "Misc")
            .withSize(1, 3);
        addTuningAuto(drive);

        autoChooser.addOption("NothingAuto", new InstantCommand());
        autoChooser.setDefaultOption("VisionTest", Autos.testVision(drive, vision));

    }
    public  Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public static void addTuningAuto(SwerveDrive drive){
        autoChooser.addOption("BackTest", TuningAutos.backTest(drive));
        autoChooser.addOption("ForwardTest", TuningAutos.forwardTest(drive));
        // // autoChooser.addOption("ForwardThenTurnTest", TuningAutos.forwardThenTurnTest(drive));
        autoChooser.addOption("TurnTest", TuningAutos.turnTest(drive));
        autoChooser.addOption("SplineTest", TuningAutos.splineTest(drive));
        // // autoChooser.addOption("LineToLinearTest", TuningAutos.lineToLinearTest(drive));
        autoChooser.addOption("StrafeRight", TuningAutos.strafeRight(drive));
        autoChooser.addOption("StrafeLeft", TuningAutos.strafeLeft(drive));
        // // autoChooser.addOption("ForwardAndBack", TuningAutos.forwardAndBackTest(drive));
    }

    public static void createAutoBuilder(SwerveDrive drive){
        // AutoBuilder
        AutoBuilder.configureHolonomic(
            drive::getPose, // Robot pose supplier
            drive::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
            drive::getSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            // drive::drive, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
            drive::setFeedforwardModuleStates,//To Use Feedforward
            new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
                new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KP.get(), 
                    SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KI.get(), 
                    SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KD.get()),  // Translation PID constants
                new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.THETA_KP.get(), 
                    SwerveDriveConstants.SwerveDriveConfig.THETA_KI.get(), 
                    SwerveDriveConstants.SwerveDriveConfig.THETA_KD.get()),  // Rotation PID constants
                4.5, // Max module speed, in m/s 4.5
                0.4, // Drive base radius in meters. Distance from robot center to furthest module.
                new ReplanningConfig() // Default path replanning config. See the API for the options here; Ba
            ),
            () -> {
                // Boolean supplier that controls when the path will be mirrored for the red alliance
                // This will flip the path being followed to the red side of the field.
                // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
                //Is this working?
                var alliance = DriverStation.getAlliance();
                if (alliance.isPresent()) {
                    return alliance.get() == DriverStation.Alliance.Red;
                }
                return false;//To Test-true
            },
            drive // Reference to this subsystem to set requirements
        );
    }
}
