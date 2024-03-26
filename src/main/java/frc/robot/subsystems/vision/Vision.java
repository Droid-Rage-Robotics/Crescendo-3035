package frc.robot.subsystems.vision;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Visit Limelight Web interface at http://10.30.35.11:5801
public class Vision extends SubsystemBase {
    // protected final ShuffleboardValue<Double> tAWriter = ShuffleboardValue.create
    //         (0.0, "Vision/tA", Vision.class.getSimpleName()).build();
    // protected final ShuffleboardValue<Double> tXWriter = ShuffleboardValue.create
    //     (0.0, "Vision/tX", Vision.class.getSimpleName()).build();
    // protected final ShuffleboardValue<Double> tYWriter = ShuffleboardValue.create
    //     (0.0, "Vision/tY", Vision.class.getSimpleName()).build();
    // protected final ShuffleboardValue<Boolean> tVWriter = ShuffleboardValue.create
    //     (false, "Vision/tV", Vision.class.getSimpleName()).build();
    HttpCamera httpCamera = new HttpCamera("Limelight", "http://roborio-3035-FRC.local:5801");
    // http://roborio-2928-FRC.local:5801 - Works
    
    // http://limelight.local:5801
    // http://10.30.35.56:5801/
    // http://10.30.35.106:5801/
    // http://frcvision.local:1181/stream.mjpg
    // http://frcvision.local:1181/stream.mjpg

    // CameraServer.getInstance().startAutomaticCapture(limelightFeed);
    // LimelightHelpers.LimelightResults llresults = LimelightHelpers.getLatestResults("");

    // Initialize Limelight network tables
    public Vision() {
        // LimelightHelpers.
        LimelightHelpers.setLEDMode_PipelineControl("");
        LimelightHelpers.setLEDMode_ForceOff("");
        LimelightHelpers.setCropWindow("",-1,1,-1,1);
        CameraServer.addCamera(httpCamera);
        Shuffleboard.getTab("Misc").add(httpCamera).withSize(3, 3);

        for (int port = 5800; port <= 5809; port++) {
            PortForwarder.add(port, "limelight.local", port);
        }
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
        
        // tAWriter.set(LimelightHelpers.getTA(""));
        // tXWriter.set(LimelightHelpers.getTX(""));
        // tYWriter.set(LimelightHelpers.getTY(""));
        // tVWriter.set(LimelightHelpers.getTV(""));
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }

    // //tx Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
    // public double gettX(){
    //     return tXWriter.get();
    // }
    // //ta Target Area (0% of image to 100% of image)
    // public double gettA(){
    //     return tAWriter.get();
    // }
    // //ty Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
    // public double gettY(){
    //     return tYWriter.get();
    // }
    // //tv Whether the limelight has any valid targets (0 or 1)
    // public boolean gettV(){
    //     return tVWriter.get();
    // }
    // public double[] getPose(){
    //     return LimelightHelpers.getBotPose("");
    // }

}
