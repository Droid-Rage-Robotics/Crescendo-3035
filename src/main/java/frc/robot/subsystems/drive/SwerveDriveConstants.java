package frc.robot.subsystems.drive;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
import frc.robot.utility.shuffleboard.ShuffleboardValueEnum;

public class SwerveDriveConstants {
    public enum SwerveDriveConfig implements ShuffleboardValueEnum<Double> {
        PHYSICAL_MAX_ANGULAR_SPEED_RADIANS_PER_SECOND(2 * (2 * Math.PI)),
        TRACK_WIDTH(Units.inchesToMeters(28.5)),
        WHEEL_BASE(Units.inchesToMeters(28.5)),

        MAX_ACCELERATION_UNITS_PER_SECOND(10),
        MAX_ANGULAR_ACCELERATION_UNITS_PER_SECOND(10),

        MAX_SPEED_METERS_PER_SECOND(SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND / 4),
        MAX_ANGULAR_SPEED_RADIANS_PER_SECOND(PHYSICAL_MAX_ANGULAR_SPEED_RADIANS_PER_SECOND.get() / 10),
        MAX_ACCELERATION_METERS_PER_SECOND_SQUARED(1),
        MAX_ANGULAR_ACCELERATION_RADIANS_PER_SECOND_SQUARED(1), // 1 / 8 of a full rotation per second per second),

        TRANSLATIONAL_KP(0), //1.8
        TRANSLATIONAL_KI(0),
        TRANSLATIONAL_KD(0),

        THETA_KP(0.555), //0.7
        THETA_KI(0),
        THETA_KD(0),

        // use witth 4096 reading
        // FRONT_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS(0),
        // FRONT_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS(0),
        // BACK_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS(0),
        // BACK_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS(00),

        FRONT_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS(-5.39),
        FRONT_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS(-.638),
        BACK_LEFT_ABSOLUTE_ENCODER_OFFSET_RADIANS(-2.24),
        BACK_RIGHT_ABSOLUTE_ENCODER_OFFSET_RADIANS(-0.24),//-5.7

        DEFAULT_HEADING_OFFSET(0),
        ;
        private final ShuffleboardValue<Double> shuffleboardValue;
        private SwerveDriveConfig(double value) {
            shuffleboardValue = ShuffleboardValue.create(value, 
                "Constants/"+SwerveDriveConfig.class.getSimpleName()+"/"+name(), SwerveDrive.class.getSimpleName())
                // .withWidget(BuiltInWidgets.kAccelerometer)
                .build();
        }
        @Override 
        public ShuffleboardValue<Double> getNum() { return shuffleboardValue; }
    }

    public enum DriveOptions implements ShuffleboardValueEnum<Boolean> { 
        IS_FIELD_ORIENTED(true),
        IS_SQUARED_INPUTS(true),
        IS_ENABLED(true)
        ;
        private final ShuffleboardValue<Boolean> shuffleboardValue;
        private DriveOptions(boolean value) {
            shuffleboardValue = ShuffleboardValue.create(value, "Constants/"+
                DriveOptions.class.getSimpleName()+"/"+name(), SwerveDrive.class.getSimpleName()).build();
        } 
        @Override 
        public ShuffleboardValue<Boolean> getNum() { return shuffleboardValue; }
    }

    public enum Speed {
        TURBO(1, 1),
        NORMAL(4, .45),//3.5, 1 //1,.4
        SLOW(0.2, 0.2),
        SUPER_SLOW(0.05, 0.05),
        ;
        private final ShuffleboardValue<Double> shuffleboardTranslationalValue;
        private final ShuffleboardValue<Double> shuffleboardAngularValue;
        private Speed(double translationalSpeed, double angularSpeed) {
            shuffleboardTranslationalValue = ShuffleboardValue.create(translationalSpeed, 
                "Constants/"+Speed.class.getSimpleName()+"/"+name()+": Translational Speed", SwerveDrive.class.getSimpleName())
                .withSize(3, 3)
                .build();
            shuffleboardAngularValue = ShuffleboardValue.create(translationalSpeed, 
                "Constants/"+Speed.class.getSimpleName()+"/"+name()+": Angular Speed", SwerveDrive.class.getSimpleName())
                .withSize(3, 3)
                .build();
        }
        public double getTranslationalSpeed() {
            return shuffleboardTranslationalValue.get();
        }
        public double getAngularSpeed() {
            return shuffleboardAngularValue.get();
        }
    }
}
