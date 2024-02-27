package frc.robot.commands.autos;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.claw.Claw;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;

public class AutoChooser {
    public static final SendableChooser<Command> autoChooser = new SendableChooser<Command>();

    public AutoChooser(
        SwerveDrive drive, Intake intake, Shooter shooter, Claw claw, Climb climb, Vision vision, Light light
    ) {
        //Put Named Commands HERE
        NamedCommands.registerCommand("test", intake.setPositionCommand(Intake.Value.HOLD));

        ComplexWidgetBuilder.create(autoChooser, "Auto Chooser", "Misc")
            .withSize(1, 3);
        autoChooser.setDefaultOption("NothingAuto", new InstantCommand());
        // autoChooser = AutoBuilder.buildAutoChooser();
        addCloseAuto();
        addFarAuto();
        addTuningAuto(drive);
    }
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public void addCloseAuto(){
        

    }
    
    public void addFarAuto(){

    }
    public void addTuningAuto(SwerveDrive drive){
        autoChooser.addOption("BackTest", TuningAutos.backTest(drive));
        autoChooser.addOption("ForwardTest", TuningAutos.forwardTest(drive));
        // autoChooser.addOption("ForwardThenTurnTest", TuningAutos.forwardThenTurnTest(drive));
        autoChooser.addOption("TurnTest", TuningAutos.turnTest(drive));
        autoChooser.addOption("SplineTest", TuningAutos.splineTest(drive));
        autoChooser.addOption("LineToLinearTest", TuningAutos.lineToLinearTest(drive));
        autoChooser.addOption("StrafeRight", TuningAutos.strafeRight(drive));
        autoChooser.addOption("StrafeLeft", TuningAutos.strafeLeft(drive));
    }
}
