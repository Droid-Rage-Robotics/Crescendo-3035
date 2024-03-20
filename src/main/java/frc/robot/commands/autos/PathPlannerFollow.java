package frc.robot.commands.autos;

import java.util.HashMap;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.drive.SwerveDrive;

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
        return AutoBuilder.buildAuto(autoName);

    }
    //Translation PID constants - PID constants to correct for translation error (used to create the X and Y PID controllers)
    //Rotation PID constants - PID constants to correct for rotation error (used to create the rotation controller)
    
}
