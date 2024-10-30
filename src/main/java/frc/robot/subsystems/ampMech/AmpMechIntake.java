package frc.robot.subsystems.ampMech;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.general.DisabledCommand;
import frc.robot.utility.motor.old.SafeCanSparkMax;
import frc.robot.utility.motor.old.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

// @Deprecated
public class AmpMechIntake extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 3/1;//3:1
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }

    protected final ShuffleboardValue<Double> targetWriter = ShuffleboardValue.create
        (0.0, "Intake/ Claw Intake Target", AmpMech.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderWriter = ShuffleboardValue.create
        (0.0, "Intake/ Claw Intake Encoder", AmpMech.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderErrorWriter = ShuffleboardValue.create
        (0.0, "Intake/ Claw Intake Encoder Error", AmpMech.class.getSimpleName()).build();

    // private final ShuffleboardValue<Boolean> isElementInClawWriter = ShuffleboardValue.create
    //         (false, "Intake/ Claw Intake Is Element In", AmpMech.class.getSimpleName()).build();
    
    
    protected final SafeCanSparkMax motor;
    protected final PIDController controller;

    public AmpMechIntake(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            24,
            MotorType.kBrushless,
            false,
            IdleMode.Coast,
            Constants.GEAR_RATIO,
            1,
            ShuffleboardValue.create(isEnabled, "Intake/ Claw Intake Is Enabled", AmpMech.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Intake/ Claw Intake Voltage", AmpMech.class.getSimpleName())
                    .build()
        );

        controller = new PIDController(
            0.3,
            0,
            0);
        controller.setTolerance(1);
        ComplexWidgetBuilder.create(controller, "Claw Intake", AmpMech.class.getSimpleName());
        ComplexWidgetBuilder.create(
            DisabledCommand.create(runOnce(this::resetEncoder)),
            "Claw Intake Reset Claw Encoder", AmpMech.class.getSimpleName());
    }

    @Override
    public void periodic() {
        motor.setVoltage(controller.calculate(getEncoder()));        
    }
    
    
    protected void setTargetPosition(double target) {
        controller.setSetpoint(target);
        targetWriter.set(target);
    }
    protected double getEncoder() {
        double pos = motor.getPosition();
        encoderWriter.write(pos);
        encoderErrorWriter.write(getTargetPosition() - pos);
        return pos;
    }
    public void resetEncoder() {
        motor.getEncoder().setPosition(0);//TODO: Test
    }

    // protected boolean isElementInClaw(){
    //     return encoderErrorWriter.get()<-2000;
    // }

    public double getTargetPosition(){
        return controller.getSetpoint();
    }

    public SafeCanSparkMax getMotor(){
        return motor;
    }
}

