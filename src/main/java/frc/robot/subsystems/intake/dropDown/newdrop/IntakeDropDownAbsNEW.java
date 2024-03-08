package frc.robot.subsystems.intake.dropDown.newdrop;

import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class IntakeDropDownAbsNEW extends IntakeDropDownNEW {
    public enum Thing{
        DEGREE,
        RADIAN
    }
    public static class Constants {
        public static double RADIANS_PER_ROTATION = Math.PI*2;
        public static double DEGREES_PER_ROTATION = 360;
        public static double OFFSET = 0.66;
    }

    DutyCycleEncoder absoluteEncoder;

    public IntakeDropDownAbsNEW(Boolean isEnabled) {
        super(isEnabled);
        /*//If It is connected to a Spark Max
        absoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
        absoluteEncoder.setPositionConversionFactor(Constants.RADIANS_PER_ROTATION);
        absoluteEncoder.setVelocityConversionFactor(Constants.RADIANS_PER_ROTATION / 60); // the 60 is seconds cause the encoder position gets updated once a second
        absoluteEncoder.setInverted(true);*/

        absoluteEncoder = new DutyCycleEncoder(0); //What chnnel is it plugged into
        // absoluteEncoder.setPositionOffset(Constants.OFFSET);
    }
    
    @Override
    public void periodic(){
        getRadianPos();
        setVoltage(calculatePID(getTargetPosition()));   
        // setVoltage(calculatePID(getTargetPosition()+calculateFeedforward(getTargetPosition(), getEncoderVelocity())));        

    }

    @Override
    public double getRadianPos() {
        double degreePos = (absoluteEncoder.getAbsolutePosition()*(Constants.DEGREES_PER_ROTATION));
        degreePosWriter.write(((degreePos)));
        double radianPos = (absoluteEncoder.getAbsolutePosition()*(Constants.RADIANS_PER_ROTATION));
        radianPosWriter.write(((radianPos)));
        double rawPos = absoluteEncoder.getAbsolutePosition();
        rawPosWriter.set(rawPos);
        return radianPos;
    }

    // @Override
    // public double getEncoderVelocity() {
    //     // double velocity = absoluteEncoder.getVelocity();
    //     double velocity = motor.getVelocity();  //Encoder doesn't read velocity
    //     encoderVelocityWriter.write(velocity);
    //     return velocity;
    // }

    // @Override
    // public void resetEncoder() {
    //     absoluteEncoder.setPositionOffset(0);
    // }

    @Override 
    public void setTargetPosition(double posDegree){
        radianTargetPosWriter.set(Math.toRadians(posDegree));
        degreeTargetPosWriter.set(posDegree);
        // rawTargetPosWriter.set(posDegree); //for motor
        rawTargetPosWriter.set(posDegree/Constants.DEGREES_PER_ROTATION); // Not for motor encoder
        controller.setSetpoint(Math.toRadians(posDegree));
    }

    @Override
    protected double calculatePID(double radian) {
        // return controller.calculate(getEncoderPosition(),Math.toRadians(positionDegree));
        return controller.calculate(getRadianPos(),(radian));

    }
}