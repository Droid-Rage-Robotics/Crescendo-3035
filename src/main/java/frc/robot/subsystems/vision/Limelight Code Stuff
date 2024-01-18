//FRC 3847 limelight code: https://github.com/Spectrum3847/SpectrumLib/blob/main/src/main/java/frc/SpectrumLib/vision/LimeLight.java
tx Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
tv Whether the limelight has any valid targets (0 or 1)
ty Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
ta Target Area (0% of image to 100% of image)
ts Skew or rotation (-90 degrees to 0 degrees)

tl	The pipeline’s latency contribution (ms) Add at least 11ms for image capture latency.
tshort	Sidelength of shortest side of the fitted bounding box (pixels)
tlong	Sidelength of longest side of the fitted bounding box (pixels)
thoriz	Horizontal sidelength of the rough bounding box (0 - 320 pixels)
tvert	Vertical sidelength of the rough bounding box (0 - 320 pixels)

//List of things to do
Create an interface that takes in Limelight pose data (Finally, something I am comfortable with!)
Install PathPlanner and read docs on the GitHub to learn to make paths in code
Store AprilTag poses in code
Create a method that makes a trajectory from the current robot position to end position
Send that trajectory to a path following command (i.e. PPSwerveController)
Report when done
Put in sequential command group to move arm when path is finished

//limelight coordinates
X+ → Pointing to the right (if you were to embody the camera)
Y+ → Pointing downward
Z+ → Pointing out of the camera

Sonic Code: https://github.com/FRC-Sonic-Squirrels/2022-Robot-Code/blob/1e6545f15ac98b344048974a50ca9d79ac2ba357/src/main/java/frc/robot/subsystems/LimelightSubsystem.java#L141 - 