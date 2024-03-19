package frc.robot.utility.motor;

import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeSparkAbsoluteEncoder {
    private SparkAbsoluteEncoder encoder;
    private final SubsystemBase base;
    protected final ShuffleboardValue<Double> degreeWriter;
    protected final ShuffleboardValue<Double> radianWriter;
    protected final ShuffleboardValue<Double> rawWriter;



    //Math.PI*2 = 360 Degrees
    //Math.PI*2/60
    public SafeSparkAbsoluteEncoder(SafeCanSparkMax motor, boolean isInverted, 
        double positionConversionFactor, double velocityConversionFactor, SubsystemBase base){
        encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
        encoder.setInverted(isInverted);
        encoder.setPositionConversionFactor(positionConversionFactor);
        encoder.setVelocityConversionFactor(velocityConversionFactor);
        this.base = base;

        rawWriter = ShuffleboardValue
            .create(0.0, base.getName()+"/Pos/Raw", base.getClass().getSimpleName())
            .withSize(1, 2)
            .build();
        degreeWriter = ShuffleboardValue
            .create(0.0, base.getName()+"/Pos/Degree", base.getClass().getSimpleName())
            .withSize(1, 2)
            .build();
        radianWriter = ShuffleboardValue
            .create(0.0, base.getName()+"/Pos/Radian", base.getClass().getSimpleName())
            .withSize(1, 2)
            .build();
    } 
    public double getPosition() {
        return encoder.getPosition();
    }
    public double getDegrees() {
        return encoder.getPosition()*(Math.PI*2);
    }
    public double getRadian() {
        return encoder.getPosition();
    }
    public double getVelocity() {
        return encoder.getVelocity();  
    }
    public void setPositionConversionFactor(double positionConversionFactor) {
        encoder.setPositionConversionFactor(positionConversionFactor);
    }
    public void setVelocityConversionFactor(double velocityConversionFactor) {
        encoder.setVelocityConversionFactor(velocityConversionFactor);
    }
    public void setInverted(boolean b) {
    }
}