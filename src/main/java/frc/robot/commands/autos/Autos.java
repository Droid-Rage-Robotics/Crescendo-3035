package frc.robot.commands.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ResetPoseVision;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;

public final class Autos {
   
    public static Command testVision(SwerveDrive drive, Vision vision) {//Top Red/Bottom Blue
        return new SequentialCommandGroup(
            // PathPlannerFollow.
            PathPlannerFollow.create(drive, "onePlusFour")
                .setMaxVelocity(6)
                .setAcceleration(6)
                .build()
            // PathPlannerFollow.create(drive, "ForwardTest")
            // .setMaxVelocity(6)
            // .setAcceleration(6)
            //     .build()
            // // new ResetPoseVision(drive, vision),
            // // new InstantCommand(()->drive.resetOdometry(new Pose2d(15,6, new Rotation2d(3)))),

            // PathPlannerFollow.create(drive, "BackwardTest")
            // .setMaxVelocity(6)
            // .setAcceleration(6)
            //     .build()
        );

    }
    private Autos () {}
}