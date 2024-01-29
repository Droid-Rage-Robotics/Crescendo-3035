package frc.robot.subsystems.claw;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
import frc.robot.utility.shuffleboard.ShuffleboardValueEnum;

public class Claw extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 180;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }
    public enum Velocity implements ShuffleboardValueEnum<Double> {
        OUTTAKE(2300),
        SHOOTER_TRANSFER(200),
        HUMAN_PLAYER(200),
        STOP(0)
        ;

        private final ShuffleboardValue<Double> velocityRPM;

        private Velocity(double velocityRPM) {
            this.velocityRPM = ShuffleboardValue.create(velocityRPM, Velocity.class.getSimpleName()+"/"+name()+": Velocity (RPM)", Claw.class.getSimpleName())
                .withSize(1, 3)
                .build();
        }

        @Override
        public ShuffleboardValue<Double> getNum() {
            return velocityRPM;
        }
    }

    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create
        (0.0, "Target Velocity", Claw.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity", Claw.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity Error", Claw.class.getSimpleName()).build();

    private final ShuffleboardValue<Boolean> isElementInClawWriter = ShuffleboardValue.create
            (false, "Is Element In", Claw.class.getSimpleName()).build();
    
    
    protected final SafeCanSparkMax motor;
    protected final PIDController controller;
    protected final SimpleMotorFeedforward feedforward;

    public Claw(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            19,
            MotorType.kBrushless,
            ShuffleboardValue.create(isEnabled, "Is Enabled", Claw.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Voltage", Claw.class.getSimpleName())
                    .build()
        );
        motor.setIdleMode(IdleMode.Coast);
        motor.setInverted(true);

        controller = new PIDController(
            0.0003,//0.0003 
            0,
            0);
        controller.setTolerance(5);
        feedforward = new SimpleMotorFeedforward(0.64, 0.000515, 0);

        ComplexWidgetBuilder.create(controller, "Claw Controller", Claw.class.getSimpleName());
        // ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetIntakeEncoder)), 
        //     "Reset Claw Encoder", Claw.class.getSimpleName());
    }

    @Override
    public void periodic() {
        motor.setVoltage(calculateIntakePID(getIntakeTargetVelocity()) + 
            calculateIntakeFeedforward(getIntakeTargetVelocity()));
        isElementInClawWriter.set(isElementInClaw());
        
    }
    
    public Command setTargetVelocityCommand(Velocity velocity){
        return new InstantCommand(()->setTargetVelocity(velocity));
    }
    protected void setTargetVelocity(Velocity velocity) {
        controller.setSetpoint(velocity.getNum().get());
        targetVelocityWriter.set(velocity.getNum().get());
    }
    protected double getIntakeEncoderVelocity() {
        double velocity = motor.getEncoder().getVelocity();
        encoderVelocityWriter.write(velocity);
        encoderVelocityErrorWriter.write(getIntakeTargetVelocity() - velocity);
        return velocity;
    }
    protected double getIntakeTargetVelocity() {
        return controller.getSetpoint();
    }
    protected double calculateIntakePID(double targetVelocity) {
        return controller.calculate(getIntakeEncoderVelocity(), targetVelocity);
    }
    protected double calculateIntakeFeedforward(double targetVelocity) {
        return feedforward.calculate(targetVelocity);
    }

    
    public void resetEncoder() {
        motor.getEncoder().setPosition(0);//TODO: Test
    }

    public boolean isElementInClaw(){
        return encoderVelocityErrorWriter.get()<-2000;
    }
}

