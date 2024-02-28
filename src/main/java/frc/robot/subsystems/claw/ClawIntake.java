package frc.robot.subsystems.claw;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
import frc.robot.utility.shuffleboard.ShuffleboardValueEnum;

public class ClawIntake extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 180;//Old One is 240 // New is 180 (I think)
        public static final double READINGS_PER_REVOLUTION = 1;
        public static final double ROTATIONS_TO_RADIANS = (GEAR_RATIO * READINGS_PER_REVOLUTION) / (Math.PI * 2);
    }
    // public enum Velocity implements ShuffleboardValueEnum<Double> {
    //     OUTTAKE(2300),
    //     SHOOTER_TRANSFER(200),
    //     HUMAN_PLAYER(200),
    //     STOP(0)
    //     ;

    //     private final ShuffleboardValue<Double> velocityRPM;

    //     private Velocity(double velocityRPM) {
    //         this.velocityRPM = ShuffleboardValue.create(velocityRPM, Velocity.class.getSimpleName()+"/"+name()+": Velocity (RPM)", ClawIntake.class.getSimpleName())
    //             .withSize(1, 3)
    //             .build();
    //     }

    //     @Override
    //     public ShuffleboardValue<Double> getNum() {
    //         return velocityRPM;
    //     }
    // }

    protected final ShuffleboardValue<Double> targetVelocityWriter = ShuffleboardValue.create
        (0.0, "Target Velocity", ClawIntake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity", ClawIntake.class.getSimpleName()).build();
    protected final ShuffleboardValue<Double> encoderVelocityErrorWriter = ShuffleboardValue.create
        (0.0, "Encoder Velocity Error", ClawIntake.class.getSimpleName()).build();

    private final ShuffleboardValue<Boolean> isElementInClawWriter = ShuffleboardValue.create
            (false, "Is Element In", ClawIntake.class.getSimpleName()).build();
    
    
    protected final SafeCanSparkMax motor;
    protected final PIDController controller;
    protected final SimpleMotorFeedforward feedforward;

    public ClawIntake(Boolean isEnabled) {
        motor = new SafeCanSparkMax(
            19,
            MotorType.kBrushless,
            true,
            IdleMode.Coast,
            1,
            1,
            ShuffleboardValue.create(isEnabled, "Is Enabled", ClawIntake.class.getSimpleName())
                    .withWidget(BuiltInWidgets.kToggleSwitch)
                    .build(),
                ShuffleboardValue.create(0.0, "Voltage", ClawIntake.class.getSimpleName())
                    .build()
        );

        controller = new PIDController(
            0.0003,//0.0003 
            0,
            0);
        controller.setTolerance(5);
        feedforward = new SimpleMotorFeedforward(0.64, 0.000515, 0);

        ComplexWidgetBuilder.create(controller, "Claw Controller", ClawIntake.class.getSimpleName());
        // ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetIntakeEncoder)), 
        //     "Reset Claw Encoder", Claw.class.getSimpleName());
    }

    @Override
    public void periodic() {
        motor.setVoltage(controller.calculate(getEncoderVelocity(),getTargetPosition()) + 
            feedforward.calculate(getTargetPosition()));
        isElementInClawWriter.set(isElementInClaw());
        
    }
    
    
    protected void setTargetPosition(double target) {
        controller.setSetpoint(target);
        targetVelocityWriter.set(target);
    }
    protected double getEncoderVelocity() {
        double velocity = motor.getEncoder().getVelocity();
        encoderVelocityWriter.write(velocity);
        encoderVelocityErrorWriter.write(getTargetPosition() - velocity);
        return velocity;
    }
    public void resetEncoder() {
        motor.getEncoder().setPosition(0);//TODO: Test
    }

    protected boolean isElementInClaw(){
        return encoderVelocityErrorWriter.get()<-2000;
    }
    public double getTargetPosition(){
        return controller.getSetpoint();
    }
}

