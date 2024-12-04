package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;

//Works
public class ResetPoseVision extends SequentialCommandGroup{
    public ResetPoseVision (SwerveDrive drive, Vision vision){
        addCommands(
            new ConditionalCommand(                
                new InstantCommand(()->drive.resetOdometry(vision.getPose())),
                new InstantCommand(),
                ()->vision.gettV()
            )
        );
    }
              
}
