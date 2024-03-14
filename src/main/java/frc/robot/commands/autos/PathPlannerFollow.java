package frc.robot.commands.autos;

import java.util.HashMap;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.drive.SwerveDriveConstants;

public class PathPlannerFollow {
    private final SwerveDrive drive;
    private final String autoName;
    private double maxVelocity = 0.3;
    private double acceleration = 0.5;
    private HashMap<String, Command> eventMap = new HashMap<>();

    

    private PathPlannerFollow(SwerveDrive drive, String pathName, double maxVelocity, double acceleration, HashMap<String, Command> eventMap) {
        this.drive = drive;
        this.autoName = pathName;
        this.maxVelocity = maxVelocity;
        this.acceleration = acceleration;
        this.eventMap = eventMap;
    }

    private PathPlannerFollow(SwerveDrive drive, String pathName) {
        this.drive = drive;
        this.autoName = pathName;
    }

    public static PathPlannerFollow create(SwerveDrive drive, String pathName) {
        return new PathPlannerFollow(drive, pathName);
    }

    public PathPlannerFollow setMaxVelocity(double maxVelocity) {
        return new PathPlannerFollow(drive, autoName, maxVelocity, acceleration, eventMap);
    }

    public PathPlannerFollow setAcceleration(double acceleration) {
        return new PathPlannerFollow(drive, autoName, maxVelocity, acceleration, eventMap);
    }

    public PathPlannerFollow addMarker(String name, Command toRun) {
        eventMap.put(name, toRun);
        return new PathPlannerFollow(drive, autoName, maxVelocity, acceleration, eventMap);
    }
    public PathPlannerFollow addMarker(String name, ParallelCommandGroup toRun) {
        eventMap.put(name, toRun);
        return new PathPlannerFollow(drive, autoName, maxVelocity, acceleration, eventMap);
    }
    public Command build(){
        // PathPlannerPath path = new PathPlannerPath(drive, pathName)
        // PathPlannerTrajectory pathGroup = new PathPlannerTrajectory(pathName);
        
        // AutoBuilder.configureHolonomic(
        //     drive::getPose, // Robot pose supplier
        //         drive::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
        //         drive::getSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
        //         drive::drive, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
        //         //TODO:Test WILL THIS WORK? IS IT ROBOT RELATIVE OR FIELD CENTRIC
        //         new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
        //             new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KP.get(), 
        //                 SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KI.get(), 
        //                 SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KD.get()),  // Translation PID constants
        //             new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.THETA_KP.get(), 
        //                 SwerveDriveConstants.SwerveDriveConfig.THETA_KI.get(), 
        //                 SwerveDriveConstants.SwerveDriveConfig.THETA_KD.get()),  // Rotation PID constants
        //             4.5, // Max module speed, in m/s
        //             0.4, // Drive base radius in meters. Distance from robot center to furthest module.
        //             new ReplanningConfig() // Default path replanning config. See the API for the options here; Ba
        //         ),
        //         () -> {
        //             // Boolean supplier that controls when the path will be mirrored for the red alliance
        //             // This will flip the path being followed to the red side of the field.
        //             // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
        //             var alliance = DriverStation.getAlliance();
        //             if (alliance.isPresent()) {
        //                 return alliance.get() == DriverStation.Alliance.Red;
        //             }
        //             return false;
        //         },
        //         drive // Reference to this subsystem to set requirements\
        //     );
        return AutoBuilder.buildAuto(autoName);

    }
    // public Command getAuto(String name){
    //     return AutoBuilder.buildAuto(name);
    // }
    //Translation PID constants - PID constants to correct for translation error (used to create the X and Y PID controllers)
    //Rotation PID constants - PID constants to correct for rotation error (used to create the rotation controller)
    
}
