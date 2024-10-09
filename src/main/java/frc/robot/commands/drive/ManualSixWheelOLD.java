package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.drive.OwnSixWheel;
import frc.robot.subsystems.drive.SixWheel;

public class ManualSixWheelOLD extends Command {
    private SixWheel drive;
    private CommandXboxController driverController;

    public ManualSixWheelOLD(SixWheel drive, CommandXboxController driverController) {
        this.drive = drive;
        this.driverController = driverController;

        addRequirements(drive);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        drive.arcadeDrive(-driverController.getLeftX(), -driverController.getRightY());
    }
}
