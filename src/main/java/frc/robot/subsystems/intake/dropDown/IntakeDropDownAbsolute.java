package frc.robot.subsystems.intake.dropDown;

import frc.robot.utility.encoder.AbsoluteDutyEncoderRIO;
import frc.robot.utility.encoder.old.SafeSparkAbsoluteEncoder;
import frc.robot.utility.motor.old.SafeCanSparkMax;

public class IntakeDropDownAbsolute extends IntakeDropDown {
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI * 2;
        public static double OFFSET = Math.PI / 2;  //90 Degree
    }
    // SparkAbsoluteEncoder absoluteEncoder;
    private AbsoluteDutyEncoderRIO absoluteEncoder;
    public IntakeDropDownAbsolute(Boolean isEnabled, SafeCanSparkMax sparkMax) {
        super(isEnabled);
        absoluteEncoder = AbsoluteDutyEncoderRIO.create(0).withDirection(true).withOffset(90).withSubsystemBase(IntakeDropDownAbsolute.class.getName());
        // absoluteEncoder = new SafeSparkAbsoluteEncoder(sparkMax,false, this);
        // absoluteEncoder = sparkMax.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        
        // absoluteEncoder.setPositionConversionFactor(Math.PI * 2);
        // absoluteEncoder.setVelocityConversionFactor(Math.PI * 2 / 60);
        // absoluteEncoder.setInverted(false);

        // setTargetPosition(Constants.OFFSET);
    }

    public void periodic(){
        // setVoltage(calculatePID(getEncoderPosition()));
        setVoltage((calculatePID(getEncoderPosition())*2.5)+(Math.cos(getEncoderPosition())*(.175))); //WORKS; DO NOT TOUCH! :)

        // setVoltage(calculatePID(getEncoderPosition())+calculateFeedforward(getEncoderPosition(), 0));
    }
    
    @Override
    public double getEncoderPosition() {
        double radianPos = (absoluteEncoder.getPosition() + Constants.OFFSET) % Constants.RADIANS_PER_ROTATION;
        // double radianPos = (absoluteEncoder.getPosition());
        radianPosWriter.write(radianPos);
        degreePosWriter.write(Math.toDegrees(radianPos));
        double raw = (absoluteEncoder.getPosition());
        rawPosWriter.write((raw));
        return radianPos;
    }

    @Override
    public double getEncoderVelocity() {
        double velocity =0;
        // double velocity = absoluteEncoder.getVelocity();
        encoderVelocityWriter.write(velocity);
        return velocity;
    }

    // @Override
    // public void resetEncoder() {
    //     absoluteEncoder.setZeroOffset(0);
    // }
}