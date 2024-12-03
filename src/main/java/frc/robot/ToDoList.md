TO DO:
- Trapezoid Profile: https://github.com/wpilibsuite/allwpilib/blob/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/elevatortrapezoidprofile/Robot.java
- On ALL Subsystems:
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
- Safety Things: CPR, First Aid, etc
- find Husky stwist handle thing
- Move most Commands into 1 File


Buy:
- Small Ethernet Cords
- Improve Pit
- Storage and pit effiency
- order hex shafts from thrifty bot they are cheaper
- https://app.smartsheet.com/b/form/e800cb5efe0742ca8ccdf610e8047005 Harbor Freight Card
- ZipTies - https://www.cabletiesandmore.com/natural-zip-ties-nylon: 5.5 and 8 in length with 2.5mm width
- Pool Noodles

Find:
Limit Switch


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
- Implemented CANivore



Notes:
- Connecting to Roborio using FileZilla
   Host: sftp://roboRIO-3035-frc.local
   Username:lvuser
   Port:22
   Location of SysID Logs: /home/lvuser/logs 
- Without any PID, the arm should stay upright, basically sayign that the kg is good
- System.out.println("setting command."+ targetPosition.name()); <-- Use this to troubleshoot>
- Connect in Pathplanner with Robot: 10.30.35.2
- Color: 5V - Multi COlor; Strip/12V - Single COlor Strip
- CycleTracker: https://docs.wpilib.org/en/stable/docs/software/telemetry/datalog.html; https://www.chiefdelphi.com/t/roborio-wpilib-logging/159434/17
- Drive Feedforward: kS - Increase until the robot very slowly moves forward (this means too much,so lower) Then kV until the robot goes forward the amount needed
- /** Max RPM of NEO */
  public static final int NEO_MAX_RPM = 5676;
  /** Max RPM of Vortex */
  public static final int VORTEX_MAX_RPM = 6784;
  /** Ticks per revolution of NEO built-in encoder */
  public static final int NEO_ENCODER_TICKS_PER_ROTATION = 42;
  /** Ticks per rotation of Vortex built-in encoder */
  public static final int VORTEX_ENCODER_TICKS_PER_ROTATION = 7168;
  /** Ticks per revolution of REV through bore encoder */
  public static final int REV_ENCODER_TICKS_PER_ROTATION = 8192;


Auto Setup
- Align wheels straight or in direction of robot travel
- 


Wiring
- Kraken has a 12V40A
- Talon FX has a 12V30A
- Drive Kraken has a 12V40A
- Drive NEO has a 12V40A
- CTR Absolute encoder has a 5A mini fuse
- Pigeon has a 5A mini fuse
- Limelight is powered by a 10A mini fuse
- mini power modual powered my a 12V 20A fuse
- ethernet switch is powered by a 10A mini fuse
- Radio power modual is powered by a 10A mini fuse
- RoboRio has a 10A mini fuse
 