package frc.robot.subsystems.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

// Visit Limelight Web interface at http://10.30.35.11:5801
public class Vision extends SubsystemBase {
    // public enum LedMode implements ShuffleboardValueEnum<Double> {
    //     kpipeLine(0), // 0 use the LED Mode set in the current pipeline
    //     kforceOff(1), // 1 force off
    //     kforceBlink(2), // 2 force blink
    //     kforceOn(3); // 3 force on
    //     private final ShuffleboardValue<Double> mode;

    //     private LedMode(double mode) {
    //         this.mode = ShuffleboardValue.create(mode, Vision.class.getSimpleName()+"/"+name()+": LEDModes", 
    //             Vision.class.getSimpleName())
    //             .withSize(1, 3)
    //             .build();
    //     }

    //     @Override
    //     public ShuffleboardValue<Double> getNum() {
    //         return mode;
    //     }
    // }
    protected final ShuffleboardValue<Double> tAWriter = ShuffleboardValue.create
            (0.0, "tA", Vision.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> tXWriter = ShuffleboardValue.create
        (0.0, "tX", Vision.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> tYWriter = ShuffleboardValue.create
        (0.0, "tY", Vision.class.getSimpleName()).build();
    protected final ShuffleboardValue<Boolean> tVWriter = ShuffleboardValue.create
        (false, "tV", Vision.class.getSimpleName()).build();
    
    // LimelightHelpers.LimelightResults llresults = LimelightHelpers.getLatestResults("");

    // Initialize Limelight network tables
    public Vision() {
        LimelightHelpers.setLEDMode_PipelineControl("");
        LimelightHelpers.setLEDMode_ForceOff("");
        LimelightHelpers.setCropWindow("",-1,1,-1,1);
    }

    @Override
    public void periodic() {
        // llresults = LimelightHelpers.getLatestResults("");
        // // Post to smart dashboard periodically
        // SmartDashboard.putNumberArray("botpose", llresults.targetingResults.botpose);
        // SmartDashboard.putNumberArray("botpose_wpiblue", llresults.targetingResults.botpose_wpiblue);
        // SmartDashboard.putNumberArray("botpose_wpired", llresults.targetingResults.botpose_wpired);
        // SmartDashboard.putData("LimelightX", llresults.targetingResults.targets_Classifier);
        // SmartDashboard.putNumberArray("LimelightX", llresults.targetingResults.botpose);
        // SmartDashboard.putNumberArray("LimelightX", llresults.targetingResults.botpose);
        
        tAWriter.set(LimelightHelpers.getTA(""));
        tXWriter.set(LimelightHelpers.getTX(""));
        tYWriter.set(LimelightHelpers.getTY(""));
        tVWriter.set(LimelightHelpers.getTV(""));
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }

    //tx Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
    public double gettX(){
        return tXWriter.get();
    }
    //ta Target Area (0% of image to 100% of image)
    public double gettA(){
        return tAWriter.get();
    }
    //ty Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
    public double gettY(){
        return tYWriter.get();
    }
    //tv Whether the limelight has any valid targets (0 or 1)
    public boolean gettV(){
        return tVWriter.get();
    }
    public double[] getPose(){
        return LimelightHelpers.getBotPose("");
    }

}
