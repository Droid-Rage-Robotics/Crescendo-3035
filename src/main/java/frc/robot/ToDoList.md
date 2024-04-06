TO DO:
- Trapezoid Profile: https://github.com/wpilibsuite/allwpilib/blob/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/elevatortrapezoidprofile/Robot.java
On ALL Subsystems:
- Have the 2 commands and one protected
- Positions/Velocity
- Min/Max Position
- Check FeedForward/PID calculations
- Make and extended Subsystems to have protected instead of private
- Make where the absolute encoders discconnect then the mechanism should just turn off
- When transfertoampmech, make it where the ampmach transfers when shooter stalls out: figure Out how to indicate stall; Shooter Stall
- Make a framework to make all of the arms to be implemented similairly 
- Limelight Tracking, Aligning, etc
- Make a function that forces all of the subsystems to set a start position in configure 
- Implement Safe Hardware Encoders
- Add IsEnabled Button to all!!!!!
- Track Battery Voltage?
- How to convert from rotation to distance and rotations through gears to get rotation
- make the motors create their own writers for us to use with; Write out in a file
- Make better Field Image with Values
-Safety Things: CPR, First Aid, etc
- find Husky stwist handle thing


Buy:
- Small Ethernet Cords
NEw Battery Cart
Improve Pit
Storage and pit effiency
Clen Wiring Bin
-Elevator Nuts



Extra Ideas:
- Lights: Add lights to have the robot tell us any errors with can, etc; Make LEDs show Errors
- Driver.getGameSpecificMessage()
- SubsystemCheck Command??
- Extension automatically commit any code when pushing code to bot (added it)
- Robot Tracking on Field
- Field Centric Turning
- Changing Shooter Speed According to Distance: InterpolatingMatrixTreeMap
- Make a DroidRagelib - search how to do it
- Limelight Tracking AutoAim.java
- Redo Shooter Dashboard Stuff



Test:
- Make all of the writes that are not necessary during a match be in a practice only writers (Helps prevent the likelihood of loop overruns): Instead of relying on the Driver, maybe make a universal switch to turn it all off
- Test Software Switch on Shuffleboard to see if it work - It does for isEnabled, implement on all
- Test Dpad Resets/Heading Direction of Drivetrain after Auto - Works
- Test Lights
- Figure out voltage writer stuff
- MAKE ROBOT CONTAINER TRACK CYCLES
- See if you put multiple Commands in one file called Commands that is static like in Autos.java; To Test



Done:
April Tag - https://github.com/Mechanical-Advantage/RobotCode2023/blob/main/src/main/java/org/littletonrobotics/frc2023/subsystems/apriltagvision/AprilTagVision.java#L38



Notes:
- Connecting to Roborio using FileZilla
   Host: sftp://roboRIO-3035-frc.local
   Username:lvuser
   Port:22
   Location of SysID Logs: /home/lvuser/logs 
- Without any PID, the arm should stay upright, basically sayign that the kg is good
- System.out.println("setting command."+ targetPosition.name()); <-- Use this to troubleshoot>
- Connect in Pathplanner with Robot: 10.30.35.2
- Color: 5V - Multi COlor Strip/12V - Single COlor Strip
-CycleTracker: https://docs.wpilib.org/en/stable/docs/software/telemetry/datalog.html; https://www.chiefdelphi.com/t/roborio-wpilib-logging/159434/17
-Drive Feedforward: kS - Increase until the robot very slowly moves forward (this means too much,so lower) Then kV until the robot goes forward the amount needed