package frc.robot.commands.drive;

import java.util.HashMap;
import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.controllers.PathFollowingController;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPlannerTrajectory;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.drive.SwerveDriveConstants;

public class PathPlannerFollow {
    private final SwerveDrive drive;
    private final String pathName;
    private double maxVelocity = 0.3;
    private double acceleration = 0.5;
    private HashMap<String, Command> eventMap = new HashMap<>();

    private PathPlannerFollow(SwerveDrive drive, String pathName, double maxVelocity, double acceleration, HashMap<String, Command> eventMap) {
        this.drive = drive;
        this.pathName = pathName;
        this.maxVelocity = maxVelocity;
        this.acceleration = acceleration;
        this.eventMap = eventMap;
    }

    private PathPlannerFollow(SwerveDrive drive, String pathName) {
        this.drive = drive;
        this.pathName = pathName;
    }

    public static PathPlannerFollow create(SwerveDrive drive, String pathName) {
        return new PathPlannerFollow(drive, pathName);
    }

    public PathPlannerFollow setMaxVelocity(double maxVelocity) {
        return new PathPlannerFollow(drive, pathName, maxVelocity, acceleration, eventMap);
    }

    public PathPlannerFollow setAcceleration(double acceleration) {
        return new PathPlannerFollow(drive, pathName, maxVelocity, acceleration, eventMap);
    }

    public PathPlannerFollow addMarker(String name, Command toRun) {
        eventMap.put(name, toRun);
        return new PathPlannerFollow(drive, pathName, maxVelocity, acceleration, eventMap);
    }
    // public PathPlannerFollow addMarker(String name, ParallelCommandGroup toRun) {
    //     eventMap.put(name, toRun);
    //     return new PathPlannerFollow(drive, pathName, maxVelocity, acceleration, eventMap);
    // }

    // public Command build3() {
    //     List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup(
    //         pathName, 
    //         new PathConstraints(maxVelocity, acceleration)
    //     );
        

    //     // Create the AutoBuilder. This only needs to be created once when robot code starts, 
    //     // not every time you want to create an auto command. A good place to put this is in 
    //     // RobotContainer along with your subsystems.
    //     AutoBuilder.
    //     SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(
    //         drive::getPose, // Pose2d supplier
    //         drive::resetOdometry, // Pose2d consumer, used to reset odometry at the beginning of auto
    //         SwerveDrive.DRIVE_KINEMATICS, // SwerveDriveKinematics
    //         new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KP.get(), 
    //             SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KI.get(), 
    //             SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KD.get()), 
    //             // PID constants to correct for translation error (used to create the X and Y PID controllers)
    //         new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.THETA_KP.get(), 
    //             SwerveDriveConstants.SwerveDriveConfig.THETA_KI.get(), 
    //             SwerveDriveConstants.SwerveDriveConfig.THETA_KD.get()), 
    //             // PID constants to correct for rotation error (used to create the rotation controller)
    //         drive::setFeedforwardModuleStates, // Module states consumer used to output to the drive subsystem
    //         eventMap,
    //         true, // Should the path be automatically mirrored depending on alliance color. Optional, defaults to true
    //         drive // The drive subsystem. Used to properly set the requirements of path following commands
    //     );
        
    //     //TODO check if there are any other better constructors ^^

    //     return autoBuilder.fullAuto(pathGroup);
    // }
    
//     public Command build(){
//         List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup(
//             pathName, 
//             new PathConstraints(maxVelocity, Math.pow(maxVelocity, 2), acceleration, Math.pow(acceleration,2))
//         );
// PathConstraints;
// PathFollowingController;
// PathPlannerLogging;
// PathPlannerAuto;
// PathPlannerPath;
//         PathPlannerTrajectory.State.;
//         AutoBuilder autoBuilder = new AutoBuilder();
//         // autoBuilder =
//         AutoBuilder.configureHolonomic(
//                 drive::getPose, // Robot pose supplier
//                 drive::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
//                 drive::getSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
//                 drive::drive, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
//                 //TODO:Test WILL THIS WORK? IS IT ROBOT RELATIVE OR FIELD CENTRIC
//                 new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
//                     new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KP.get(), 
//                         SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KI.get(), 
//                         SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KD.get()),  // Translation PID constants
//                     new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.THETA_KP.get(), 
//                         SwerveDriveConstants.SwerveDriveConfig.THETA_KI.get(), 
//                         SwerveDriveConstants.SwerveDriveConfig.THETA_KD.get()),  // Rotation PID constants
//                     4.5, // Max module speed, in m/s
//                     0.4, // Drive base radius in meters. Distance from robot center to furthest module.
//                     new ReplanningConfig() // Default path replanning config. See the API for the options here; Ba
//                 ),
//                 () -> {
//                     // Boolean supplier that controls when the path will be mirrored for the red alliance
//                     // This will flip the path being followed to the red side of the field.
//                     // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
//                     var alliance = DriverStation.getAlliance();
//                     if (alliance.isPresent()) {
//                         return alliance.get() == DriverStation.Alliance.Red;
//                     }
//                     return false;
//                 },
//                 drive // Reference to this subsystem to set requirements
//         );
//         return autoBuilder.followPath(pathGroup)
//     }
    //Translation PID constants - PID constants to correct for translation error (used to create the X and Y PID controllers)
    //Rotation PID constants - PID constants to correct for rotation error (used to create the rotation controller)

    
}
