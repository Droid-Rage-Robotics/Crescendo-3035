// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;
// @Deprecated
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoBalancetoAutoAim extends ProfiledPIDCommand {//TODO: Add a TImeout to lockwheels
  /** Creates a new AutoBalance. */
  // private static DriverStation driverStation;
  // private SwerveDrive drive;
  // private Light light;
  // private Timer timer;
  // private WriteOnlyBoolean atSetpointWriter = new WriteOnlyBoolean(false, "PID Auto balance at positionn", Drive.class.getSimpleName());
  SwerveDrive drive;
  Vision vision;
  public AutoBalancetoAutoAim(SwerveDrive drive, Vision vision) {//TODO:Test
    
    super(
        new ProfiledPIDController(
            1.2, //.034
            0,
            0.0000,
            new TrapezoidProfile.Constraints(1.525, 1)),

            // drive::getPitch,
            vision::gettX,
        0,
        (output, setpoint) -> {
            // Use the output (and setpoint, if desired) here
            // drive.drive(0, 0, output);
            //Horizontal
            if(vision.gettX()<0){
              drive.drive(0, 0, -output);
            } else if(vision.gettX()>0){
              drive.drive(0, 0, output);
            }
          });

    addRequirements(drive);
    this.drive = drive;
    this.vision=vision;
    // this.light = light;
    getController().setTolerance(0.5); //degrees
    // ComplexWidgetBuilder.create(getController(), "PID Auto balance controller", Drive.class.getSimpleName());
  }
  
  @Override
  public void initialize() {
    System.out.println("autobalance start");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
      // light.setAllColor(light.red);
    // final int time = driverStation.getMatchTime();
    // if(isMatchTime()){
    //   return true;
    // }

    // atSetpointWriter.set(getController().atSetpoint());
    if(getController().atSetpoint()){
      // light.setAllColor(light.green);
    }
    return getController().atSetpoint();
  }

  // public boolean isMatchTime(){//TODO:test
  //   return DriverStation.getMatchTime()<2;
  // }

  //https://github.com/frc3175/Scorpion-2024/blob/main/src/main/java/frc/robot/subsystems/Limelight.java
  public double getDistanceToTarget()  {

    double targetOffsetAngle_Vertical = vision.gettY();

    // how many degrees back is your limelight rotated from perfectly vertical?
    double limelightMountAngleDegrees = 30;//TODO: Measure 

    // distance from the center of the Limelight lens to the floor
    double limelightLensHeightInches = 1; //TODO Measure

    // distance from the target to the floor
    double goalHeightInches = 53.88; 

    double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
    double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0); //Turing Degree to Radian

    //calculate distance
    double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);

    return distanceFromLimelightToGoalInches;
  }
}