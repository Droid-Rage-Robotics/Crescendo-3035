
TODO:
- Trapezoid Profile: https://github.com/wpilibsuite/allwpilib/blob/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/elevatortrapezoidprofile/Robot.java
On ALL Subsystems:
- Have the 2 commands and one protected
- Positions/Velocity
- Min/Max Position
- Check FeedForward/PID calculations
Non Urgent ToDos:
- Make and extended Subsystems to have protected instead of private

Extra Ideas:
- Lights: Add lights to have the robot tell us any errors with can, etc; Make LEDs show Errors
- Driver.getGameSpecificMessage()
- SubsystemCheck Command??
- Extension automatically commit any code when pushing code to bot (added it) - need to see if 
- Robot Tracking on Field
- Field Centric Turning
- Changing Shooter Speed According to Distance: InterpolatingMatrixTreeMap
- Make a DroidRagelib - search how to do it
- Limelight Tracking AutoAim.java

Test:
- Make all of the writes that are not necessary during a match be in a practice only writers (Helps prevent the likelihood of loop overruns)
- Test Software Switch on Shuffleboard to see if it work
- Test Dpad Resets/Heading Direction of Drivetrain after Auto 
- Test Lights
- Figure out voltage writer stuff

Make where the absolute encoders discconnect then the mechanism should just turn off

Done:
April Tag - https://github.com/Mechanical-Advantage/RobotCode2023/blob/main/src/main/java/org/littletonrobotics/frc2023/subsystems/apriltagvision/AprilTagVision.java#L38


When transfertoampmech, make it where the ampmach transfers when shooter stalls out
 
MAKE ROBOT CONTAINER TRACK CYCLES

make a framework to make all of the arms to be implemented similairly 
Shooter Stall
3 Cycle Auto
limelight camera thing
TEST PID POSITIONSon the trap arm

3-11
Make a function that forces all of the subsystems to set a start position in configure telopbuttons
write out which direction the -/+ is on all arms
Download Pathplanner wiht Windows
Seeif you put multiple Commands in one file called Commands that is static like TransferToAmpMech
make all bots in start pos
Redo Shooter Dashboard Stuff
Implement Safe Hardware Encoders
Add IsEnabled Button to all!!!!!
Make the imu update rate faster - Can't do
battery voltage usage

Notes:
Connecting to Roborio using FileZilla
Host: sftp://roboRIO-3035-frc.local
Username:lvuser
Port:22
Location of SysID Logs: /home/lvuser/logs 
Without any PID, the arm shoudl stay upright, basically sayign that the kg is good
System.out.println("setting command."+ targetPosition.name()); <-- Use this to troubleshoot>
Connect in Pathplanner with Robot: 10.30.35.2
Color
5V - Multi COlor Strip
12V - Single COlor Strip
CycleTracker: https://docs.wpilib.org/en/stable/docs/software/telemetry/datalog.html; https://www.chiefdelphi.com/t/roborio-wpilib-logging/159434/17



Get broken motors
do climb
figure out led
soldering kit
encoder fixing?



// drive.setYawCommand(
//     switch (DriverStation.getRawAllianceStation()) {
//         case Unknown -> 0;//180
//         case Blue1,Blue2,Blue3 -> 0;
//         case Red1,Red2,Red3 -> 0;
//     }
// );


// testButton.configureSparkMaxMotorBindings(
//     new SafeCanSparkMax(
//             25,
//             MotorType.kBrushless,
//             true,
//             IdleMode.Coast,
//             1,
//             1,
//             ShuffleboardValue.create(true, "Claw Intake Is Enabled", Claw.class.getSimpleName())
//                     .withWidget(BuiltInWidgets.kToggleSwitch)
//                     .build(),
//                 ShuffleboardValue.create(0.0, "Claw Intake Voltage", Claw.class.getSimpleName())
//                     .build()
//         )
// );

// testButton.configureTalonMotorBindings(
//     new SafeTalonFX(
//     16,
//     true,
//     IdleMode.Coast,
//     1,
//     1,
//     ShuffleboardValue.create(true, "Is Enabled Wheel", Intake.class.getSimpleName())
//         .withWidget(BuiltInWidgets.kToggleSwitch)
//         .build(),
//     ShuffleboardValue.create(0.0, "Voltage Wheel", Intake.class.getSimpleName())
//         .build()
// )
// );


// testButton.configureDriverOperatorBindings(drive,intake);
// testButton.configureCycleTrackerBindings(cycleTracker);

// testButton.configureClimbTestBindings(climb, intake);
// testButton.configureIntakeAndShooterTestBindings(intake, shooter);
// testButton.configureShooterTestBindings(shooter);


// testButton.configureDriveBindings(drive);
// testButton.configureSysIDBindings(sysID);
// testButton.configureAmpMechTestBindings(ampMech);


Test the rotation of the drive  moror
2910 for drivetrain
 public static final int DRIVE_CURRENT_LIMIT = 70;
    public static final int STEER_CURRENT_LIMIT = 30;