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

public class AmpMechElevator extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 15/1;//1/15
        public static final double GEAR_DIAMETER_INCHES = 2.058;//2.058
        public static final double COUNTS_PER_PULSE = 1; // 2048 bc rev through bore
        public static final double ROT_TO_INCHES = (COUNTS_PER_PULSE * GEAR_RATIO) / (GEAR_DIAMETER_INCHES * Math.PI);
        // public static final double MIN_POSITION = 0;
        // public static final double MAX_POSITION = 36;
    }
    private final SafeCanSparkMax motor;
    
    // private final PIDController controller = new PIDController(0, 0, 0.);//2.4
    private final PIDController controller = new PIDController(.52, 0, 0);
    // private final ElevatorFeedforward feedforward = new ElevatorFeedforward(0,0, 0, 0);
    // private final ElevatorFeedforward feedforward = new ElevatorFeedforward(0.15, .6, 0, 0);

    protected final ShuffleboardValue<Double> rawPosWriter = ShuffleboardValue
        .create(0.0, "Elevator/Pos/Raw", AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = 
        ShuffleboardValue.create(0.0, "Elevator/ Encoder Velocity (Radians per Second)", 
        AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    protected final ShuffleboardValue<Double> rawTargetPosWriter = ShuffleboardValue
        .create(0.0, "Elevator/Target/Raw", AmpMech.class.getSimpleName())
        .withSize(1, 2)
        .build();
    private final ShuffleboardValue<Double> voltage = ShuffleboardValue
        .create(0.0, "Elevator/ Elev Voltage", AmpMech.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Double> encoderPositionWriter = ShuffleboardValue
        .create(0.0, "Elevator/ Elev Encoder Position", AmpMech.class.getSimpleName())
        .withSize(1, 3)
        .build();
    protected final ShuffleboardValue<Boolean> isMovingManually = ShuffleboardValue
        .create(false, "Elevator/ Elev Moving manually", AmpMech.class.getSimpleName())
        .build();
;

    public AmpMechElevator(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            22, 
            MotorType.kBrushless,
            false,
            IdleMode.Coast,
            1,
            1.0,
            ShuffleboardValue.create(isEnabled, "Elevator/ Elev Is Enabled", AmpMech.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            voltage
        );
        
        controller.setTolerance(0.1);

        ComplexWidgetBuilder.create(controller, "Elevator/ Elev PID", AmpMech.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 1);
        ComplexWidgetBuilder.create(
            DisabledCommand.create(runOnce(this::resetEncoder)),
            "Elevator Reset Encoder", AmpMech.class.getSimpleName());
    }

    @Override
    public void periodic() {
        
        // if(getVoltage()>5){
        //     setTargetPosition(2);

        // } else{
            // setVoltage((controller.calculate(getEncoderPosition(),getTargetPosition())*1.1) + 
            //     feedforward.calculate(getTargetPosition()));
            setVoltage((controller.calculate(getEncoderPosition(),getTargetPosition())*1.1) + .37);
            // setVoltage(controller.calculate(getEncoderPosition(),getTargetPosition()) + 
            //     .2);
            // setVoltage(controller.calculate(getEncoderPosition(),getTargetPosition()));
        // }
    }
    @Override
    public void simulationPeriodic() {
        periodic();
    }

    
    public void setTargetPosition(double target) {
        // if (target < Constants.MIN_POSITION) return;
        if (target > 30) return;
        controller.setSetpoint(target);
        rawTargetPosWriter.set(target);
        rawTargetPosWriter.set(target);

    }
    protected void setVoltage(double voltage) {
        motor.setVoltage(voltage);
        // rightMotor.setVoltage(voltage);
    }
    public void setMovingManually(boolean value) {
        getIsMovingManually().set(value);
    }
    protected ShuffleboardValue<Boolean> getIsMovingManually() {
        return isMovingManually;
    }

    public void resetEncoder() {
        motor.getEncoder().setPosition(0);
    }

    public double getEncoderPosition() {
        double position = motor.getPosition();
        encoderPositionWriter.write(position);
        rawPosWriter.write(position);
        return position;
    }

    public double getTargetPosition(){
        return controller.getSetpoint();
    }

    public boolean isMovingManually() {
        return getIsMovingManually().get();
    }

    public SafeCanSparkMax getMotor(){
        return motor;
    }

    public double getVoltage(){
        return motor.getVoltage();
    }
}
