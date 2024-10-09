// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.LimelightHelpers;
import frc.robot.subsystems.vision.Vision;

public class TestLimelight extends Command {
  /** Creates a new Test. */
  SwerveDrive drive;
   Vision vision;
   CommandXboxController controller;
   double rot_limelight=0;
   double forward_limelight=0;
  public TestLimelight(SwerveDrive drive, Vision vision, CommandXboxController controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drive = drive;
    this.vision= vision;
    this.controller = controller;
  }
   

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // drive(true);
    // drive.drive(xSpeed, ySpeed, rot);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // limelight_aim_proportional();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
  
   // simple proportional turning control with Limelight.
  // "proportional control" is a control algorithm in which the output is proportional to the error.
  // in this case, we are going to return an angular velocity that is proportional to the 
  // "tx" value from the Limelight.
  double limelight_aim_proportional()
  {    
    // kP (constant of proportionality)
    // this is a hand-tuned number that determines the aggressiveness of our proportional control loop
    // if it is too high, the robot will oscillate around.
    // if it is too low, the robot will never reach its target
    // if the robot never turns in the correct direction, kP should be inverted.
    double kP = .035;

    // tx ranges from (-hfov/2) to (hfov/2) in degrees. If your target is on the rightmost edge of 
    // your limelight 3 feed, tx should return roughly 31 degrees.
    double targetingAngularVelocity = LimelightHelpers.getTX("limelight") * kP;

    // convert to radians per second for our drive method
    targetingAngularVelocity *= .1;

    //invert since tx is positive when the target is to the right of the crosshair
    targetingAngularVelocity *= -1.0;

    return targetingAngularVelocity;
  }

  // simple proportional ranging control with Limelight's "ty" value
  // this works best if your Limelight's mount height and target mount height are different.
  // if your limelight and target are mounted at the same or similar heights, use "ta" (area) for target ranging rather than "ty"
  double limelight_range_proportional()
  {    
    double kP = .1;
    double targetingForwardSpeed = LimelightHelpers.getTY("limelight") * kP;
    targetingForwardSpeed *= .1;
    targetingForwardSpeed *= -1.0;
    return targetingForwardSpeed;
  }

  private void drive(boolean fieldRelative) {
    // // Get the x speed. We are inverting this because Xbox controllers return
    // // negative values when we push forward.
    // double xSpeed =
    //     -MathUtil.applyDeadband(controller.getLeftY(), 0.02)
    //         * .1;

    // // Get the y speed or sideways/strafe speed. We are inverting this because
    // // we want a positive value when we pull to the left. Xbox controllers
    // // return positive values when you pull to the right by default.
    // double ySpeed =
    //     -(MathUtil.applyDeadband(controller.getLeftX(), 0.02))
    //         * .1;

    // // Get the rate of angular rotation. We are inverting this because we want a
    // // positive value when we pull to the left (remember, CCW is positive in
    // // mathematics). Xbox controllers return positive values when you pull to
    // // the right by default.
    // double rot =
    //     -(MathUtil.applyDeadband(controller.getRightX(), 0.02))
    //         * .1;

    // while the A-button is pressed, overwrite some of the driving values with the output of our limelight methods
    if(controller.rightTrigger().getAsBoolean())
    {
         rot_limelight = limelight_aim_proportional();
        // rot = rot_limelight;

         forward_limelight = limelight_range_proportional();
        // xSpeed = forward_limelight;

        //while using Limelight, turn off field-relative driving.
        fieldRelative = false;
    }

    drive.drive(forward_limelight, 0, rot_limelight);
  }
}
