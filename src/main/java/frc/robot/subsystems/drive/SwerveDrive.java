package frc.robot.subsystems.drive;

import com.ctre.phoenix6.configs.MountPoseConfigs;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.Pigeon2FeaturesConfigs;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.SwerveDriveConstants.SwerveDriveConfig;
import frc.robot.subsystems.drive.SwerveModuleKraken.POD;
import frc.robot.subsystems.drive.SwerveDriveConstants.Speed;
import frc.robot.subsystems.drive.SwerveDriveConstants.DriveOptions;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

//Set Voltage instead of set Power
//Set them to 90 to 100%
public class SwerveDrive extends SubsystemBase {
    public enum TippingState {
        NO_TIP_CORRECTION,
        ANTI_TIP,
        AUTO_BALANCE,
        AUTO_BALANCE_ANTI_TIP,
        ;
    }

    public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
        new Translation2d(SwerveDriveConfig.WHEEL_BASE.get() / 2, SwerveDriveConfig.TRACK_WIDTH.get() / 2),  // Front Left --
        new Translation2d(SwerveDriveConfig.WHEEL_BASE.get() / 2, -SwerveDriveConfig.TRACK_WIDTH.get() / 2),   // Front Right +-
        new Translation2d(-SwerveDriveConfig.WHEEL_BASE.get() / 2, SwerveDriveConfig.TRACK_WIDTH.get() / 2), // Back Left -+
        new Translation2d(-SwerveDriveConfig.WHEEL_BASE.get() / 2, -SwerveDriveConfig.TRACK_WIDTH.get() / 2)   // Back Right ++
    );

    
    
    private final SwerveModuleKraken frontRight = new SwerveModuleKraken(
        3,
        2,

        false, 
        true,

        10, 
        SwerveDriveConfig.FRONT_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS::get,
        true,
        DriveOptions.IS_ENABLED.get(),POD.FR
    );
    private final SwerveModuleKraken backRight = new SwerveModuleKraken(
        5,
        4,

        false, 
        true,

        11, 
        SwerveDriveConfig.BACK_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS::get,
        true,
        DriveOptions.IS_ENABLED.get(),POD.BR
    );
    private final SwerveModuleKraken backLeft = new SwerveModuleKraken(
        7,
        6,

        false, 
        true,

        12, 
        SwerveDriveConfig.BACK_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS::get,
        true,
        DriveOptions.IS_ENABLED.get(),POD.BL
    );
    private final SwerveModuleKraken frontLeft = new SwerveModuleKraken(
        9,
        8,

        false, 
        true,

        13, 
        SwerveDriveConfig.FRONT_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS::get,
        true,
        DriveOptions.IS_ENABLED.get(),POD.FL
    );
    private final SwerveModuleKraken[] swerveModules = { frontLeft, frontRight, backLeft, backRight };

    private final Pigeon2 pigeon2 = new Pigeon2(14);
    private final MountPoseConfigs poseConfigs  = new MountPoseConfigs();;

    private final SwerveDriveOdometry odometery = new SwerveDriveOdometry (
        DRIVE_KINEMATICS, 
        new Rotation2d(0), 
        getModulePositions()
    );

    private volatile Speed speed = Speed.NORMAL;
    private volatile TippingState tippingState = TippingState.NO_TIP_CORRECTION;

    private final ShuffleboardValue<String> tippingStateWriter = 
        ShuffleboardValue.create(tippingState.name(), "State/Tipping State", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<String> speedStateWriter = 
        ShuffleboardValue.create(speed.name(), "State/Speed", SwerveDrive.class.getSimpleName()).build();
    
    private final ShuffleboardValue<Double> headingWriter = 
        ShuffleboardValue.create(0.0, "Gyro/Heading-Yaw (Degrees)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Double> rollWriter = 
        ShuffleboardValue.create(0.0, "Gyro/Roll (Degrees)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Double> pitchWriter =   
        ShuffleboardValue.create(0.0, "Gyro/Pitch (Degrees)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<String> locationWriter = 
        ShuffleboardValue.create("", "Robot Location", SwerveDrive.class.getSimpleName()).build();

    private final ShuffleboardValue<Double> frontLeftTurnPositionWriter = 
        ShuffleboardValue.create(0.0, "Swerve Modules/Front Left/Turn Position (Radians)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Double> frontLeftDriveDistanceWriter = 
        ShuffleboardValue.create(0.0, "Swerve Modules/Front Left/Drive Position (Radians)", SwerveDrive.class.getSimpleName()).build();

    private final ShuffleboardValue<Double> frontRightTurnPositionWriter = 
        ShuffleboardValue.create(0.0, "Swerve Modules/Front Right/Turn Position (Radians)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Double> frontRightDriveDistanceWriter = 
        ShuffleboardValue.create(0.0, "Swerve Modules/Front Right/Drive Position (Radians)", SwerveDrive.class.getSimpleName()).build();
    
    private final ShuffleboardValue<Double> backLeftTurnPositionWriter = 
        ShuffleboardValue.create(0.0, "Swerve Modules/Back Left/Turn Position (Radians)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Double> backLeftDriveDistanceWriter = 
        ShuffleboardValue.create(0.0, "Swerve Modules/Back Left/Drive Position (Radians)", SwerveDrive.class.getSimpleName()).build();

    private final ShuffleboardValue<Double> backRightTurnPositionWriter = 
        ShuffleboardValue.create(0.0, "Swerve Modules/Back Right/Turn Position (Radians)", SwerveDrive.class.getSimpleName()).build();
    private final ShuffleboardValue<Double> backRightDriveDistanceWriter = 
        ShuffleboardValue.create(0.0, "Swerve Modules/Back Right/Drive Position (Radians)", SwerveDrive.class.getSimpleName()).build();

    private final ShuffleboardValue<Boolean> isEnabled = 
        ShuffleboardValue.create(true, "Is Drive Enabled", SwerveDrive.class.getSimpleName())
        .withWidget(BuiltInWidgets.kToggleSwitch)
        .build();

    private final ShuffleboardValue<Double> forwardVelocityWriter = 
        ShuffleboardValue.create(0.0, "Forward Velocity Writer", SwerveDrive.class.getSimpleName()).build();

    private boolean isBreakMode = false;
    private final Field2d field2d = new Field2d();

    public SwerveDrive(Boolean isEnabled) {
        for (SwerveModuleKraken swerveModule: swerveModules) {
            swerveModule.brakeMode();
            // swerveModule.coastMode();
            // swerveModule.brakeAndCoast^Mode();
        }
        //TODO: Figure Out Mounting Config
        poseConfigs.MountPosePitch = 0;//Up-Down//0
        poseConfigs.MountPoseRoll = 90;//Side-Side//90
        poseConfigs.MountPoseYaw = 180;//Heading//180
        // pigeon2.configMountPose();
        pigeon2.getConfigurator().apply(poseConfigs);
        pigeon2.setYaw(0);
        
        // pigeon2.configMountPose(AxisDirection.NegativeX, AxisDirection.PositiveZ);
        setYawCommand(SwerveDriveConfig.DEFAULT_HEADING_OFFSET.get());

        ComplexWidgetBuilder.create(field2d, "Field", SwerveDrive.class.getSimpleName());
        this.isEnabled.set(isEnabled);
        
    }

    
    @Override
    public void periodic() {
        odometery.update(
            getRotation2d(),
            getModulePositions()
        );

        field2d.setRobotPose(getPose());

        headingWriter.set(getHeading());
        rollWriter.set(getRoll());
        pitchWriter.set(getPitch());
        locationWriter.set(getPose().getTranslation().toString());


        // frontLeftTurnAbsolutePositionWriter.set(frontLeft.getTurnEncoderRad());
        frontLeftTurnPositionWriter.set(frontLeft.getTurningPosition());
        frontLeftDriveDistanceWriter.set(frontLeft.getDrivePos());

        // frontRightTurnAbsolutePositionWriter.set(frontRight.getTurnEncoderRad());
        frontRightTurnPositionWriter.set(frontRight.getTurningPosition());
        frontRightDriveDistanceWriter.set(frontRight.getDrivePos());

        // backLeftTurnAbsolutePositionWriter.set(backLeft.getTurnEncoderRad());
        backLeftTurnPositionWriter.set(backLeft.getTurningPosition());
        backLeftDriveDistanceWriter.set(backLeft.getDrivePos());

        // backRightTurnAbsolutePositionWriter.set(backRight.getTurnEncoderRad());
        backRightTurnPositionWriter.set(backRight.getTurningPosition());
        backRightDriveDistanceWriter.set(backRight.getDrivePos());

        forwardVelocityWriter.write(getForwardVelocity());
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }

    public SwerveModulePosition[] getModulePositions() {
        return new SwerveModulePosition[] {
            frontLeft.getPosition(),
            frontRight.getPosition(),
            backLeft.getPosition(),
            backRight.getPosition()
        };
    }

    public TippingState getTippingState() {
        return tippingState;
    }

    public double getHeading() {//Yaw
        return Math.IEEEremainder(pigeon2.getYaw().getValueAsDouble(), 360);
    }

    public double getPitch() {
        return Math.IEEEremainder(pigeon2.getPitch().getValueAsDouble(), 360);
    }

    public double getRoll() {
        return Math.IEEEremainder(pigeon2.getRoll().getValueAsDouble(), 360);
    }

    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getHeading());//TODO:Make

    }

    public Pose2d getPose() {
        return odometery.getPoseMeters();
    }

    public boolean isFieldOriented() {
        return DriveOptions.IS_FIELD_ORIENTED.get();
    }

    public boolean isSquaredInputs() {
        return DriveOptions.IS_SQUARED_INPUTS.get();
    }

    public double getTranslationalSpeed() {
        return speed.getTranslationalSpeed();
    }

    public double getAngularSpeed() {
        return speed.getAngularSpeed();
    }

    public double getForwardVelocity() {
        return (frontLeft.getDriveVelocity() + frontRight.getDriveVelocity()) / 2;
    }

    // public double getHeadingOffset() {
    //     return SwerveConfig.HEADING_OFFSET.value.get();
    // }

    public void resetOdometry(Pose2d pose) {
        odometery.resetPosition(getRotation2d(), getModulePositions(), pose);
    }

    public void drive(double xSpeed, double ySpeed, double turnSpeed) {
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turnSpeed);
        drive(chassisSpeeds);
    }
    public void drive(ChassisSpeeds chassisSpeeds) {
        SwerveModuleState[] states = SwerveDrive.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        setModuleStates(states);
    }

    public void setModuleStates(SwerveModuleState[] states) {
        if (!isEnabled.get()) return;
        SwerveDriveKinematics.desaturateWheelSpeeds(
            states, 
            SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND
        );

        for (int i = 0; i < 4; i++) {
            swerveModules[i].setState(states[i]);
        }
    }

    public void setFeedforwardModuleStates(SwerveModuleState[] states) {
        if (!isEnabled.get()) return;
        SwerveDriveKinematics.desaturateWheelSpeeds(
            states, 
            SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND
        );

        for (int i = 0; i < 4; i++) {
            swerveModules[i].setFeedforwardState(states[i]);
        }
    }

    public void stop() {
        for (SwerveModuleKraken swerveModule: swerveModules) {
            swerveModule.stop();
        }
    }

    public void setTippingState(TippingState tippingState) {
        this.tippingState = tippingState;
        tippingStateWriter.set(tippingState.name());
    }

    public Command setSpeed(Speed speed) {
        return runOnce(() -> {
            this.speed = speed;
            speedStateWriter.set(speed.name());
        });
    }


    public Command resetEncoders() {
        return runOnce(() -> {
            for (SwerveModuleKraken swerveModule: swerveModules) {
                swerveModule.resetDriveEncoder();
                // pigeon2.setYaw(getAngularSpeed())
            }
        });
    }


    public Command setYawCommand(double degrees) {
        return runOnce(
            () -> pigeon2.setYaw(degrees, 5)
        );
    }
    // public Command resetHeadingDriving() {
    //     return setYawCommand(-90);
    // }

    private void coastMode() {
        for (SwerveModuleKraken swerveModule: swerveModules) {
            swerveModule.coastMode();
        }
    }

    private void breakMode() {
        for (SwerveModuleKraken swerveModule: swerveModules) {
            swerveModule.brakeMode();
        }
    }

    // public Command toggleBreakMode() {
    //     return runOnce(() -> {
    //         if (isBreakMode) {
    //             coastMode();
    //             isBreakMode = false;
    //         } else {
    //             breakMode();
    //             isBreakMode = true;
    //         }
    //     });
    // }


    public Command toggleFieldOriented() {
        return runOnce(() -> DriveOptions.IS_FIELD_ORIENTED.set(
                !isFieldOriented()
            )
        );
    }

    public Command toggleSquareInputs() {
        return runOnce(() -> DriveOptions.IS_SQUARED_INPUTS.set(
                !isSquaredInputs()
            )
        );
    }

    public Command toggleAntiTipping() {
        // TODO: if you can upgrade java version to 17 in gradle and it works, you could simplify this switch statement using switch expression
        return runOnce(() -> setTippingState(
            TippingState.NO_TIP_CORRECTION
            // switch (tippingState) {
            //     case ANTI_TIP -> TippingState.NO_TIP_CORRECTION;
            //     case AUTO_BALANCE -> TippingState.AUTO_BALANCE_ANTI_TIP;
            //     case AUTO_BALANCE_ANTI_TIP -> TippingState.AUTO_BALANCE;
            //     case NO_TIP_CORRECTION ->TippingState.ANTI_TIP;
            // }
        ));
    }

    public Command toggleAutoBalance() {
        return runOnce(() -> setTippingState(
            TippingState.NO_TIP_CORRECTION
            // switch (tipp ingState) {
            //     case ANTI_TIP -> TippingState.AUTO_BALANCE_ANTI_TIP;
            //     case AUTO_BALANCE -> TippingState.NO_TIP_CORRECTION;
            //     case AUTO_BALANCE_ANTI_TIP -> TippingState.ANTI_TIP;
            //     case NO_TIP_CORRECTION ->TippingState.AUTO_BALANCE;
            // }
        ));
    }

    public Command runStop() {
        return runOnce(this::stop);
    }

    public TrapezoidProfile.Constraints getThetaConstraints() { //TODO when do you use this? I think for turning
        return new TrapezoidProfile.Constraints(
            SwerveDriveConfig.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND.get(),
            SwerveDriveConfig.MAX_ANGULAR_ACCELERATION_RADIANS_PER_SECOND_SQUARED.get());
    }

    public Command driveAutoReset(){
        return runOnce(()->setYawCommand(getRotation2d().rotateBy(Rotation2d.fromDegrees(0)).getDegrees()));
    }      
    public ChassisSpeeds getSpeeds() {//Is this Roboto Relative
        return DRIVE_KINEMATICS.toChassisSpeeds(getModuleStates());
    }
    public SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[swerveModules.length];
        for (int i = 0; i < swerveModules.length; i++) {
            states[i] = swerveModules[i].getState();
        }
        return states;
    }
    public void playMusic(int num){
        for (SwerveModuleKraken swerveModule: swerveModules) {
            // swerveModule.brakeMode();
            swerveModule.playMusic(num);
        }
    }

    public SafeCanSparkMax getFRTurnCanSparkMax(){
        return frontLeft.getTurnMotor();
    }
}
