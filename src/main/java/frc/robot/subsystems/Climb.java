package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
// @Deprecated
public class Climb extends SubsystemBase{
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 1;
        public static final double GEAR_DIAMETER_INCHES = 1.88;
        public static final double COUNTS_PER_PULSE = 1; // 2048 bc rev through bore
        public static final double ROT_TO_INCHES = (COUNTS_PER_PULSE * GEAR_RATIO) / (GEAR_DIAMETER_INCHES * Math.PI);
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = 16.2;
    }public enum Position{
        CLIMB(0),
        START(0),
        TRAP(0),
        ;

        private final ShuffleboardValue<Double>  climbPos;

        private Position(double climbPos) {
            this.climbPos = ShuffleboardValue.create(climbPos, Position.class.getSimpleName()+"/"+name()+
                ": dropPos (Position)", Climb.class.getSimpleName())
                .withSize(1, 3)
                .build();
        }
    } 
    private final PIDController controller = new PIDController(2., 0, 0);
    // private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.1, 0.2, 0);
    private final ShuffleboardValue<Double> voltageWriter = ShuffleboardValue.create
        (0.0, "Climb/Voltage", Climb.class.getSimpleName())
        .build();

    private final SafeCanSparkMax motorL, motorR;
    protected final ShuffleboardValue<Double> encoderPositionWriter = ShuffleboardValue
        .create(0.0, "Climb/Encoder Position", Climb.class.getSimpleName())
        .withSize(1, 3)
        .build();
    protected final ShuffleboardValue<Boolean> isMovingManually = ShuffleboardValue
        .create(false, "Climb/Moving manually", Climb.class.getSimpleName())
        .build();


    public Climb(Boolean isEnabledLeft, Boolean isEnabledRight) {
        motorL = new SafeCanSparkMax(
            21, 
            MotorType.kBrushless,
            false,
            IdleMode.Coast,
            Constants.ROT_TO_INCHES,
            1.0,
            ShuffleboardValue.create(isEnabledLeft, "Climb/Is Enabled Left", 
                Climb.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            voltageWriter
        );

        motorR = new SafeCanSparkMax(
            20, 
            MotorType.kBrushless,
            true,
            IdleMode.Coast,
            Constants.ROT_TO_INCHES,
            1.0,
            ShuffleboardValue.create(isEnabledRight, "Climb/Is Enabled Right", 
                Climb.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            voltageWriter
        );

        ComplexWidgetBuilder.create(controller, "PID", 
            Climb.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 2);

        ComplexWidgetBuilder.create(DisabledCommand
            .create(runOnce(this::resetEncoder)), 
            "Reset Encoder", Climb.class.getSimpleName());
    }

    @Override
    public void periodic() {
        // setPower(controller.calculate(getEncoderPosition()));
        // setPower(controller.calculate(getEncoderPosition())+
        //     feedforward.calculate(getVelocity(), getVelocity()));
    } 
    public void setTargetPosition(Position position){
        setTargetPosition(position.climbPos.get());
        // controller.setSetpoint(position.climbPos.get());
    }
    public void setTargetPosition(double positionRadians){
        controller.setSetpoint(positionRadians);
    }
    
    
    public void setPower(double power) {
        // if (!isEnabled.get()) return;
        motorL.setPower(power);
        motorR.setPower(power);
    }
    protected void setVoltage(double voltage) {
        voltageWriter.set(voltage);    
        motorL.setVoltage(voltage);
        // rightMotor.setVoltage(voltage);
    }

    public double getVelocity() {
        return motorL.getEncoder().getVelocity();
        // return motorR.getEncoder().getVelocity();
    }

    
    protected ShuffleboardValue<Boolean> getIsMovingManually() {
        return isMovingManually;
    }

    
    public double getMaxPosition() {
        return Constants.MAX_POSITION;
    }

    
    public double getMinPosition() {
        return Constants.MIN_POSITION;
    }

    
    public void resetEncoder() {
        motorL.getEncoder().setPosition(0);
    }

    
    public double getEncoderPosition() {
        double position = motorL.getPosition(); //If You want to Change what motor is giving the values
        encoderPositionWriter.write(position);
        return position;
    }

    // public void setTargetPosition(double positionRadians) {
    //     if (positionRadians < getMinPosition()) return;
    //     if (positionRadians > getMaxPosition()) return;
    //     controller.setSetpoint(positionRadians);
    // }

    public double getTargetPosition() {
        return controller.getSetpoint();
    }

    public void setMovingManually(boolean value) {
        getIsMovingManually().set(value);
    }

    public boolean isMovingManually() {
        return getIsMovingManually().get();
    }

    // protected double calculateFeedforward(double targetVelocity) {
    //     return feedforward.calculate(targetVelocity);
    // }

    protected double calculatePID(double targetVelocity) {
        return controller.calculate(getEncoderPosition(), targetVelocity);
    }

    public SafeCanSparkMax getMotorL(){
        return motorL;
    }
    public SafeCanSparkMax getMotorR(){
        return motorR;
    }
}
