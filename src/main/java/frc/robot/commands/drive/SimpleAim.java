package frc.robot.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;

public class SimpleAim extends Command{
    private SwerveDrive drive;
    private Vision vision;
    private PIDController controller, distanceController;
    //  private final ShuffleboardValue<Double> writer = 
        // ShuffleboardValue.create(0.0, "SImple AIm Test number", 
        // SwerveDrive.class.getSimpleName()).build();
    
    public SimpleAim(SwerveDrive drive, Vision vision){
        this.drive = drive;
        this.vision = vision;
        controller = new PIDController(.06, 0, 0);
        distanceController = new PIDController(.001, 0,0);
        controller.setTolerance(1.5);
        controller.setTolerance(1);
    }

    @Override
    public void initialize() {
        controller.reset();
    }

    @Override
    public void execute() {
        //x/y can be controller
        // writer.set(getTurn());

        // drive.drive(0,0, controller.calculate(vision.gettX(), 0));
        
        // drive.drive(distanceController.calculate(estimateDistance(), 1), 
        //     distanceController.calculate(estimateDistance(), 1), 
        //     controller.calculate(vision.gettX(), 0));

        drive.drive(distanceController.calculate(estimateDistance(), 0), 
            distanceController.calculate(estimateDistance(), 0), 0);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private double getTurn(){
        double num = controller.calculate(vision.gettX(), 0);
        if (num > 1){
            return 1;
        } else if(num < .05){
            return 0;
        }
        else return num;
    }

    public double estimateDistance(){
        double targetOffsetAngle_Vertical = 0;

        // how many degrees back is your limelight rotated from perfectly vertical?
        double limelightMountAngleDegrees = 25.0; 

        // distance from the center of the Limelight lens to the floor
        double limelightLensHeightInches = 20.0; 

        // distance from the target to the floor
        double goalHeightInches = 60.0; 

        double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        double angleToGoalRadians = angleToGoalDegrees * (Math.PI / 180.0);

        //calculate distance; distanceFromLimelightToGoalInches
        return (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
    }
}

