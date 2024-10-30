package frc.robot.utility.encoder.old;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

/***Used to plug in directly to the Roborio */
public class AbsoluteDutyEncoder extends SubsystemBase {
    // DigitalInput
    protected final DutyCycleEncoder encoder;
    protected double positionOffset=0;
    protected final ShuffleboardValue<Double> degreeWriter;
    protected final ShuffleboardValue<Double> radianWriter;
    protected final ShuffleboardValue<Double> rawWriter;
    protected final ShuffleboardValue<Double> encoderPosWriter = ShuffleboardValue.create
        (0.0, "Encoder Position", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Double> encoderPosFlip = ShuffleboardValue.create
        (0.0, "Encoder Position FlipTemporary", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Boolean> isConnectedWriter;
    protected final ShuffleboardValue<Double> offsetWriter = ShuffleboardValue.create
        (0.0, "Offset", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Double> distanceWriter = ShuffleboardValue.create
        (0.0, "Distance", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    private boolean isInverted;

    public AbsoluteDutyEncoder(int channelNum, boolean isInverted, 
        double distancePerRotation, double offset, SubsystemBase base) {
        encoder = new DutyCycleEncoder(channelNum);
        if(!(distancePerRotation<1)){
            encoder.setDistancePerRotation(distancePerRotation);
        } else{
            encoder.setDistancePerRotation(Math.PI*2);//Default should be Math.PI*2 or 1
        }
        this.isInverted=isInverted;
        this.positionOffset = offset;
        encoder.setPositionOffset(offset);
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
        isConnectedWriter = ShuffleboardValue.create
            (true, "isConnected", AbsoluteDutyEncoder.class.getSimpleName())
            .build();
        // encoder.isConnected();//use this to show disconnections of encoder
    }

    @Override
    public void periodic() {
        offsetWriter.set(encoder.getPositionOffset());
        isConnectedWriter.set(encoder.isConnected());
        encoderPosWriter.set(calculateAbsPos());
        
        encoderPosFlip.set(testFlip(true));
        distanceWriter.set(encoder.getDistancePerRotation());
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }

    // if (getInverted()) {
    //     absoluteOffset = 1 - encoder.getPositionOffset();
    //   }
    /**
     * 
     * @return The Absolute Position of the Encoder 
     */
    public double calculateAbsPos(){
        double givenPos = encoder.getAbsolutePosition()-encoder.getPositionOffset();
        if(givenPos<0){ //To account for negative values/rollovers - no work
            givenPos=1-Math.abs(givenPos);
        }

        if(isInverted){ //To invert values based on direction - Works
            givenPos = 1-givenPos;
        }
        return givenPos*encoder.getDistancePerRotation();
    }
    // public double testFlip(boolean test){
    //     double givenPos = encoder.getAbsolutePosition()-encoder.getPositionOffset();
    //     if(givenPos<0){ //To account for negative values/rollovers
    //         givenPos=1-Math.abs(givenPos);
    //     }

    //     if(test){ //To invert values based on direction - Work
    //         givenPos = 1-givenPos;
    //     }
    //     return givenPos*encoder.getDistancePerRotation();
    // }
    //totest
    public double testFlip(boolean test){
        double givenPos = encoder.getAbsolutePosition();
        

        if(test){ //To invert values based on direction - Work
            givenPos = 1-givenPos;
        }
        givenPos = givenPos-(positionOffset/encoder.getDistancePerRotation());

        if(givenPos<0){ //To account for negative values/rollovers
            givenPos=1-Math.abs(givenPos);
        }
        return givenPos*encoder.getDistancePerRotation();
    }
}  