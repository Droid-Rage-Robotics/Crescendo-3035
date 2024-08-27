// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.RotationController;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AlignToAprilTag extends PIDCommand {

    private static double lowKP = 0.035;
    private static double highKP = 0.06;
    private static double tolerance = 1;
    SwerveDrive drive;
    Vision vision;
    Command driveCommand;
    DoubleSupplier fwdPositiveSupplier;
    private final RotationController rotationController;
    private static double out;

    /** Creates a new AlignToAprilTag. */
    public AlignToAprilTag(DoubleSupplier fwdPositiveSupplier, double offset, SwerveDrive drive, Vision vision) {
        super(
                // The controller that the command will use
                new PIDController(lowKP, 0, 0),
                // This should return the measurement
                () -> vision.gettX(),
                // This should return the setpoint (can also be a constant)
                () -> offset, //0
                // This uses the output
                output -> setOutput(output),
                drive);
        this.drive = drive;
        this.vision = vision;
        this.getController().setTolerance(tolerance);
        this.rotationController = new RotationController(drive);
        driveCommand =
                new InstantCommand(()->drive.drive(
                        fwdPositiveSupplier.getAsDouble(), // Allows pilot to drive fwd and rev
                        getOutput(), // Moves us center to the tag
                        getSteering())); // Aligns to grid
                        // () -> 1.0, // full velocity
                        // () -> true); // Field relative is true
        // Use addRequirements() here to declare subsystem dependencies.
        // Configure additional PID options by calling `getController` here.
        this.setName("AlignToAprilTag");
    }

    public double getSteering() {
        return rotationController.calculate(Math.PI);
        // return drive.calculateRotationController(() -> Math.PI);
    }

    public static void setOutput(double output) {
        out = -1 * output;
        if (Math.abs(out) > 1) {
            out = 1 * Math.signum(out);
        }

        out = out * 1 * 0.3;
    }

    public static double getOutput() {
        return out;
    }

    @Override
    public void initialize() {
        super.initialize();
        out = 0;
        // getLedCommand(tagID).initialize();
        // drive.resetRotationController();
        driveCommand.initialize();
        if (0 > 16) {
            this.getController().setP(highKP);
        } else {
            this.getController().setP(lowKP);
        }
    }

    @Override
    public void execute() {
        super.execute();
        driveCommand.execute();
        // getLedCommand(tagID).execute();
    }

    @Override
    public void end(boolean interrupted) {
        // getLedCommand(tagID).end(interrupted);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // private Command getLedCommand() {
    //     double tagID = Robot.vision.getClosestTagID();
    //     switch ((int) tagID) {
    //         case 1:
    //         case 6:
    //             return LEDCommands.leftGrid();
    //         case 2:
    //         case 7:
    //             return LEDCommands.midGrid();
    //         case 3:
    //         case 8:
    //             return LEDCommands.rightGrid();
    //     }
    //     return new OneColorLEDCommand((2 / 3), 1, new Color(130, 103, 185), "Full Wrong", 80);
    // }
}