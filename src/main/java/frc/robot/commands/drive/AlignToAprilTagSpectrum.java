package frc.robot.commands.drive;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
// /https://github.com/Spectrum3847/2023-X-Ray/blob/main/src/main/java/frc/robot/swerve/commands/AlignToAprilTag.java
public class AlignToAprilTagSpectrum
extends PIDCommand 
{

    private static double lowKP = 0.035;
    private static double highKP = 0.06;
    private static double tolerance = 2;
    // Command driveCommand;
    DoubleSupplier fwdPositiveSupplier;
    private static double out;
    private Vision vision;
    private SwerveDrive drive;

    /** Creates a new AlignToAprilTag. */
    public AlignToAprilTagSpectrum(Vision vision, SwerveDrive drive,
        DoubleSupplier fwdPositiveSupplier) {
        super(
                // The controller that the command will use
                new PIDController(lowKP, 0, 0),
                // This should return the measurement
                () -> vision.gettX(),
                // This should return the setpoint (can also be a constant)
                () -> 0,//offset - 0
                // This uses the output
                output -> setOutput(output));
                //drive, vision);
            this.vision = vision;
            this.drive = drive;

        this.getController().setTolerance(tolerance);
        // driveCommand = new InstantCommand(()-> drive.drive(getOutput(), 0, getSteering()));
                // new SwerveDrive(
                //         fwdPositiveSupplier, // Allows pilot to drive fwd and rev
                //         () -> getOutput(), // Moves us center to the tag
                //         () -> getSteering(), // Aligns to grid
                //         () -> 1.0, // full velocity
                //         () -> true); // Field relative is true
        // Use addRequirements() here to declare subsystem dependencies.
        // Configure additional PID options by calling `getController` here.
        // this.setName("AlignToAprilTag");
    }

    public double getSteering() {
        return 0;
        // return drive.calculateRotationController(() -> Math.PI);
    }

    public static void setOutput(double output) {
        out = -1 * output;
        if (Math.abs(out) > 1) {
            out = 1 * Math.signum(out);
        }

        out = out  * 0.3;//* Maxvelocity
    }

    public static double getOutput() {
        return out;
    }

    @Override
    public void initialize() {
        super.initialize();
        out = 0;
        // getLedCommand(tagID).initialize();
        // Robot.swerve.resetRotationController();
        // driveCommand.initialize();
        if (vision.gettX() > 16) {
            this.getController().setP(highKP);
        } else {
            this.getController().setP(lowKP);
        }
    }

    @Override
    public void execute() {
        super.execute();
        // driveCommand.execute();
        new InstantCommand(()-> drive.drive(getOutput(), 0, getSteering()));
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