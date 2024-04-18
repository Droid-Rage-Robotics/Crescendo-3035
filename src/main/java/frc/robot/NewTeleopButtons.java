package frc.robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;

public final class NewTeleopButtons {
    CommandXboxController controllerOne = new CommandXboxController(0);
    CommandXboxController controllerTwo = new CommandXboxController(1);

    public void newTeleopButtons(Climb climb, Intake intake, 
    Shooter shooter, AmpMech amp , SwerveDrive drive){
        // controllerOne.y()
        // .onTrue(
        //     new InstantCommand(()->climb.setTargetPosition(Climb.Position.CLIMB))
        // );
            controllerOne.y().onTrue(new InstantCommand(()->climb.setTargetPosition(Climb.Position.CLIMB)));
            controllerOne.a().onTrue(new InstantCommand(()->climb.setTargetPosition(Climb.Position.TRAP)));

            controllerOne.rightTrigger().onTrue(intake.setPositionCommand(Intake.Value.INTAKE_GROUND))
                .onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD));
            controllerOne.leftTrigger().onTrue(intake.setPositionCommand(Intake.Value.OUTTAKE))
                .onFalse(intake.setPositionCommand(Intake.Value.HOLD));

            controllerOne.rightBumper().onTrue(new InstantCommand(()->shooter.setTargetVelocity(Shooter.ShooterSpeeds.SPEAKER_SHOOT)))
                .onTrue(amp.setPositionCommand(AmpMech.Value.SHOOT))
                .onTrue(intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER))
                .onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD))
                .onFalse (new InstantCommand(()->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)))
                .onFalse(amp.setPositionCommand(AmpMech.Value.HOLD));

            controllerOne.leftBumper().onTrue(amp.setPositionCommand(AmpMech.Value.INTAKE_SHOOTER))
            .onTrue(intake.setPositionCommand(Intake.Value.SHOOTER_TRANSFER))
            .onTrue(new InstantCommand(()->shooter.setTargetVelocity(Shooter.ShooterSpeeds.AMP_SHOOT)))
            .onFalse(intake.setPositionCommand(Intake.Value.SHOOTER_HOLD))
            .onFalse (new InstantCommand(()->shooter.setTargetVelocity(Shooter.ShooterSpeeds.STOP)))
            ;

           
    }
}