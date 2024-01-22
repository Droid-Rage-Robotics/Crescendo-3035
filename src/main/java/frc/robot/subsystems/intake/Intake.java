package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.commands.LightCommand.IntakeState;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
import frc.robot.utility.shuffleboard.ShuffleboardValueEnum;

public class Intake extends SubsystemBase {
    public static class DropDownConstants {
        public static final double GEAR_RATIO = 1 / 180;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }
    public enum Velocity implements ShuffleboardValueEnum<Double> {
        INTAKE(-3000),
        OUTTAKE(2300),
        SHOOTER_TRANSFER(200),
        ELEV_TRANSFER(200),
        STOP(0)
        ;

        private final ShuffleboardValue<Double> velocityRPM;

        private Velocity(double velocityRPM) {
            this.velocityRPM = ShuffleboardValue.create(velocityRPM, Velocity.class.getSimpleName()+"/"+name()+": Velocity (RPM)", Intake.class.getSimpleName())
                .withSize(1, 3)
                .build();
        }

        @Override
        public ShuffleboardValue<Double> getNum() {
            return velocityRPM;
        }
    }

    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create
        (0.0, "Target Velocity", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity Error", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<String> intakeStateWriter = ShuffleboardValue.create
        ("Test", "Intake State", Intake.class.getSimpleName()).build();

    private final ShuffleboardValue<Boolean> isElementInWriter = ShuffleboardValue.create
            (false, "Is Element In", Intake.class.getSimpleName()).build();
    
    
    protected final SafeCanSparkMax intake;
    protected final PIDController intakeController;
    protected final SimpleMotorFeedforward intakeFeedforward;
    
    //TODO: CHeck this
    protected final ColorSensorV3 colorSensor;
    protected final ColorMatch colorMatcher;
    protected final Color orange = new Color(0.143, 0.427, 0.429);//TODO: Fix Color
    protected Color detectedColor;
    protected ColorMatchResult match;

    public Intake(Boolean isEnabled) {
        intake = new SafeCanSparkMax(
            19,
            MotorType.kBrushless,
            ShuffleboardValue.create(isEnabled, "Is Enabled", Intake.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Voltage", Intake.class.getSimpleName())
                    .build()
        );
        // intakeEncoder = intake.getEncoder();
        intake.setIdleMode(IdleMode.Coast);
        intake.setInverted(true);

        intakeController = new PIDController(
            0.0003,//0.0003 
            0,
            0);
        intakeController.setTolerance(5);
        intakeFeedforward = new SimpleMotorFeedforward(0.64, 0.000515, 0);

        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);//Not Sure What This Is
        colorMatcher = new ColorMatch();
        colorMatcher.addColorMatch(orange);
        detectedColor = colorSensor.getColor(); 
        match = colorMatcher.matchClosestColor(detectedColor);

        
        ComplexWidgetBuilder.create(intakeController, "Intake Controller", Intake.class.getSimpleName());
        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetIntakeEncoder)), 
            "Reset Intake Encoder", Intake.class.getSimpleName());
    }

    @Override
    public void periodic() {
        intake.setVoltage(calculateIntakePID(getIntakeTargetVelocity()) + 
            calculateIntakeFeedforward(getIntakeTargetVelocity()));
        updateColorSensor();
        isElementInWriter.set(isElementIn());
        
    }
    private void updateColorSensor(){
        colorMatcher.addColorMatch(orange);
        detectedColor = colorSensor.getColor(); 
        match = colorMatcher.matchClosestColor(detectedColor);
    }
    public boolean isElementIn(){
        return match.color == orange;
        // return encoderVelocityErrorWriter.get()<-2000;
    }
    public Command setTargetVelocityCommand(Velocity velocity){
        return new InstantCommand(()->setTargetVelocity(velocity));
    }
    protected void setTargetVelocity(Velocity velocity) {
        intakeController.setSetpoint(velocity.getNum().get());
        targetVelocityWriter.set(velocity.getNum().get());
    }
    protected double getIntakeEncoderVelocity() {
        double velocity = intake.getEncoder().getVelocity();
        encoderVelocityWriter.write(velocity);
        encoderVelocityErrorWriter.write(getIntakeTargetVelocity() - velocity);
        return velocity;
    }
    protected double getIntakeTargetVelocity() {
        return intakeController.getSetpoint();
    }
    protected double calculateIntakePID(double targetVelocity) {
        return intakeController.calculate(getIntakeEncoderVelocity(), targetVelocity);
    }
    protected double calculateIntakeFeedforward(double targetVelocity) {
        return intakeFeedforward.calculate(targetVelocity);
    }

    
    public void resetIntakeEncoder() {
        intake.getEncoder().setPosition(0);//TODO: Test
    }

    public void setIntakeState(IntakeState state){
        intakeStateWriter.set(state.name());
    }
}

