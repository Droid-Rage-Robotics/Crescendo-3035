package frc.robot.subsystems.climb;

import com.revrobotics.RelativeEncoder;

// @Deprecated
public class ClimbAlternate extends Climb{
    // private SafeCanSparkMax sparkMax;
    private RelativeEncoder encoder;
    public ClimbAlternate(Boolean isEnabledLeft, Boolean isEnabledRight) {
        super(isEnabledLeft,isEnabledRight);
        encoder = motorL.getAlternateEncoder(1024);
        encoder.setInverted(false);
        // this.sparkMax = sparkMax;
    }

    @Override
    public void periodic() {
        setPower(controller.calculate(getEncoderPosition()));
    } 


    @Override
    public void resetEncoder() {
        motorL.getEncoder().setPosition(0);
        encoder.setPosition(0);
    }

    @Override
    public double getEncoderPosition() {
        double position = encoder.getPosition();
        encoderPositionWriter.write(position);
        return position;
    }

    @Override
    public double getError(){
        return controller.getPositionError();
    }
}
