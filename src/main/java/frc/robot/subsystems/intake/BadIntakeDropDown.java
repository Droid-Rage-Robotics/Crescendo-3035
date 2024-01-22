package frc.robot.subsystems.intake;


import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
@Deprecated
public class BadIntakeDropDown extends SubsystemBase {
    public static class DropDownConstants {
        public static final double GEAR_RATIO = 1 / 180;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }
    private enum Position 
    // implements ShuffleboardValueEnum<Double> 
    {
        INTAKE( 0),
        OUTTAKE(0),
        SHOOTER_TRANSFER(0),
        ELEV_TRANSFER(0),
        STOP(0)
        ;

        private final ShuffleboardValue<Double>  dropPos;

        private Position(double dropPos) {
            this.dropPos = ShuffleboardValue.create(dropPos, Position.class.getSimpleName()+"/"+name()+": dropPos (RPM)", Intake.class.getSimpleName())
                .withSize(1, 3)
                .build();
        }

        // @Override
        // public ShuffleboardValue<Double> getPos() {
        //     return dropPos;
        // }

    }

    protected final ShuffleboardValue<Double> targetPosWriter = ShuffleboardValue.create
        (0.0, "Target Pos", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderPosWriter = ShuffleboardValue.create
        (0.0, "Encoder Pos", Intake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderPosErrorWriter = ShuffleboardValue.create
        (0.0, "Encoder Pos Error", Intake.class.getSimpleName()).build();

    
    protected final SafeCanSparkMax dropDown;
    protected final PIDController  dropController;
    protected final ArmFeedforward dropFeedforward;


    public BadIntakeDropDown(Boolean isEnabled) {
        dropDown = new SafeCanSparkMax(
            9,
            MotorType.kBrushless,
            ShuffleboardValue.create(isEnabled, "Is Enabled", Intake.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Voltage", Intake.class.getSimpleName())
                    .build()
        );
        dropDown.getEncoder().setPositionConversionFactor(DropDownConstants.ROTATIONS_TO_RADIANS);
        dropDown.getEncoder().setVelocityConversionFactor(DropDownConstants.ROTATIONS_TO_RADIANS);
        dropDown.setIdleMode(IdleMode.Coast);
        dropDown.setInverted(true);

        dropController = new PIDController(
            0.0003,//0.0003 
            0,
            0);
        dropController.setTolerance(1);
        dropFeedforward = new ArmFeedforward(0.079284, 0.12603, 2.3793, 0.052763); //TODO: Fix #s

        ComplexWidgetBuilder.create(dropController, "Drop Controller", Intake.class.getSimpleName());
        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetDropDownEncoder)), 
            "Reset DropDown Encoder", Intake.class.getSimpleName());
    }

    @Override
    public void periodic() {
        dropDown.setVoltage(dropFeedforward.calculate(dropController.getSetpoint(), dropDown.getEncoder().getVelocity()) + 
            dropController.calculate(dropDown.getEncoder().getPosition(),dropController.getSetpoint()));
        encoderPosWriter.write(dropDown.getEncoder().getVelocity());
        encoderPosErrorWriter.write(dropController.getSetpoint() - dropDown.getEncoder().getVelocity());
        
    }

    protected void setTargetVelocity(Position pos) {
        dropController.setSetpoint(pos.dropPos.get());
        targetPosWriter.set(pos.dropPos.get());
    }
    public void resetDropDownEncoder() {
        dropDown.getEncoder().setPosition(0);//TODO: Test
    }
}

