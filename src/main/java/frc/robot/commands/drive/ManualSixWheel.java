package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.drive.OwnSixWheel;

public class ManualSixWheel extends Command {
    private OwnSixWheel drive;
    private CommandXboxController driverController;

    public ManualSixWheel(OwnSixWheel drive, CommandXboxController driverController) {
        this.drive = drive;
        this.driverController = driverController;

        addRequirements(drive);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        drive.drive(-driverController.getLeftX(), -driverController.getRightY());
    }
}
