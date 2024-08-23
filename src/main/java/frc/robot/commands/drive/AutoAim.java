// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;
// @Deprecated
public class AutoAim extends Command {//TODO: Test this
  private SwerveDrive drive;
  // private Light light;
  private Vision vision;
  private ProfiledPIDController turnController, distanceController; // Or a normal PID Control
  public AutoAim(SwerveDrive drive, Vision vision//, Light light
  ) {
        turnController = new ProfiledPIDController(
            0.5, 
            0,
            0.0000,
            new TrapezoidProfile.Constraints(1.525, 1));
        turnController.setTolerance(.03);//-27 degrees to 27 degrees
        
        distanceController = new ProfiledPIDController(
            0., //.034
            0,
            0.0000,
            new TrapezoidProfile.Constraints(1.525, 1));
        distanceController.setTolerance(0.01);//Some sort of distance thing
        
        

    addRequirements(drive, vision);
    this.drive = drive;
    // this.light = light;
    this.vision = vision;
  }
  
  @Override
  public void initialize() {
    // System.out.println("AutoAiming Start");
  }

  @Override
  public void execute(){
    drive.drive(distanceController.calculate(vision.gettY(), 0), 
            0, 
            turnController.calculate(vision.gettX(),0));
  }
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Should be Moved to LightCommand
    //   light.setAllColor(light.red);
    // if(turnController.atSetpoint()&&distanceController.atSetpoint()){
    //   light.setAllColor(light.green);
    // }
    return turnController.atSetpoint()&&distanceController.atSetpoint();
  }
}