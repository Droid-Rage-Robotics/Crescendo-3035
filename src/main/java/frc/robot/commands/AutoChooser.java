package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;

public class AutoChooser {
    public static final SendableChooser<Command> autoChooser = new SendableChooser<Command>();

    public AutoChooser() {
        ComplexWidgetBuilder.create(autoChooser, "Auto Chooser", "Misc")
            .withSize(1, 3);
        addCloseAuto();
        addFarAuto();
    }
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public void addCloseAuto(){
        autoChooser.setDefaultOption("NothingAuto", new InstantCommand());

    }
    public void addFarAuto(){

    }
}
