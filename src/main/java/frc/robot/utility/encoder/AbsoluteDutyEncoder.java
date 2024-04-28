package frc.robot.utility.encoder;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

// @Deprecated
public class AbsoluteDutyEncoder extends SubsystemBase {
    protected final DutyCycleEncoder encoder;
    protected final ShuffleboardValue<Double> encoderPosWriter = ShuffleboardValue.create
        (0.0, "Encoder Position", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Double> encoderPosFlip = ShuffleboardValue.create
        (0.0, "Encoder Position FlipTemporary", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Boolean> isConnectedWriter = ShuffleboardValue.create
        (true, "isConnected", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Double> offsetWriter = ShuffleboardValue.create
        (0.0, "Offset", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
        protected final ShuffleboardValue<Double> distanceWriter = ShuffleboardValue.create
        (0.0, "Distance", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    private boolean isInverted;
    public AbsoluteDutyEncoder(int channelNum, boolean isInverted, double distancePerRotation, double offset) {
        encoder = new DutyCycleEncoder(channelNum);
        encoder.setDistancePerRotation(distancePerRotation);//Can you do .5?
        this.isInverted=isInverted;

        encoder.setPositionOffset(offset);
        // encoder.reset();
        // encoder.isConnected();//use this to show disconnections of encoder
    }

    @Override
    public void periodic() {
        offsetWriter.set(encoder.getPositionOffset());

        isConnectedWriter.set(encoder.isConnected());
        // encoderPos.set((encoder.getAbsolutePosition()-encoder.getPositionOffset())+.75);
        encoderPosWriter.set(calculateAbsPos());
        encoderPosFlip.set(1-(encoder.getAbsolutePosition()-encoder.getPositionOffset()));


        // encoderPos.set(encoder.getDistance());
        distanceWriter.set(encoder.getDistancePerRotation());
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }

    public double calculateAbsPos(){
        double givenPos = encoder.getAbsolutePosition()-encoder.getPositionOffset();
        if(givenPos<0){ //To account for negative values/rollovers
            givenPos=1-Math.abs(givenPos);
            //givePose=1+givenPos
        }

        if(isInverted){ //To invert values based on direction
            givenPos = 1-givenPos;
        }
        return givenPos*encoder.getDistancePerRotation();
    }
}  