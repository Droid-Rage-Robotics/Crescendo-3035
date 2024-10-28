package frc.robot.utility.encoder.old;

import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.old.SafeCanSparkMax;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeSparkAbsoluteEncoder extends SubsystemBase {
    private SparkAbsoluteEncoder encoder;
    protected final ShuffleboardValue<Double> degreeWriter;
    protected final ShuffleboardValue<Double> radianWriter;
    protected final ShuffleboardValue<Double> rawWriter;

    public SafeSparkAbsoluteEncoder(SafeCanSparkMax motor, boolean isInverted,
        SubsystemBase base){
        encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
        encoder.setInverted(isInverted);
        encoder.setPositionConversionFactor(Math.PI*2);
        encoder.setVelocityConversionFactor((Math.PI*2)/60);
        // this.base = base;

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
    @Override
    public void periodic(){
        rawWriter.set(getPosition());
        degreeWriter.set(getDegrees());
        radianWriter.set(getRadian());
    }
    public double getPosition() {
        //Raw Position/Radian Position
        return encoder.getPosition();
    }
    public double getDegrees() {
        return encoder.getPosition()*(180/Math.PI);
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
}