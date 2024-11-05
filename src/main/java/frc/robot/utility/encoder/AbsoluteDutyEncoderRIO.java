package frc.robot.utility.encoder;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.encoder.old.AbsoluteDutyEncoder;
import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class AbsoluteDutyEncoderRIO {
    protected final DutyCycleEncoder encoder;
    private boolean isInverted;

    // protected double positionOffset=0;
    protected final ShuffleboardValue<Double> degreeWriter;
    protected final ShuffleboardValue<Double> radianWriter;
    protected final ShuffleboardValue<Double> rawWriter;
    protected final ShuffleboardValue<Boolean> isConnectedWriter;
    protected String name;
    // protected final ShuffleboardValue<Double> encoderPosWriter = ShuffleboardValue.create
    //     (0.0, "Encoder Position", AbsoluteDutyEncoder.class.getSimpleName())
    //     .build();
    // protected final ShuffleboardValue<Boolean> isConnectedWriter;
    // protected final ShuffleboardValue<Double> offsetWriter = ShuffleboardValue.create
    //     (0.0, "Offset", AbsoluteDutyEncoder.class.getSimpleName())
    //     .build();
    // protected final ShuffleboardValue<Double> distanceWriter = ShuffleboardValue.create
    //     (0.0, "Distance", AbsoluteDutyEncoder.class.getSimpleName())
    //     .build();


    private AbsoluteDutyEncoderRIO(DutyCycleEncoder encoder){
        this.encoder=encoder;
        rawWriter = ShuffleboardValue
            .create(0.0, name+"/Pos/Raw", name)
            .withSize(1, 2)
            .build();
        degreeWriter = ShuffleboardValue
            .create(0.0, name+"/Pos/Degree", name)
            .withSize(1, 2)
            .build();
        radianWriter = ShuffleboardValue
            .create(0.0, name+"/Pos/Radian", name)
            .withSize(1, 2)
            .build();
        isConnectedWriter = ShuffleboardValue.create
            (true, "isConnected", AbsoluteDutyEncoder.class.getSimpleName())
            .build();
    }

    public static InverterBuilder create(int deviceID) {
        AbsoluteDutyEncoderRIO encoder = new AbsoluteDutyEncoderRIO(new DutyCycleEncoder(deviceID));
        return encoder. new InverterBuilder();
    }
    public class InverterBuilder {
        public OffsetWriter withDirection(boolean isInvertedBoolean) {
            isInverted = isInvertedBoolean;
            return new OffsetWriter();
        }
    }
    public class OffsetWriter {
        public BaseWriter withOffset(double offset) {
            encoder.setPositionOffset(offset/(2*Math.PI));
            return new BaseWriter();
        }
    }
    public class BaseWriter {
        @SuppressWarnings("unchecked")
        public <T extends AbsoluteDutyEncoderRIO> T withSubsystemBase(String subsystemBaseName) {
            name = subsystemBaseName;
            return (T) AbsoluteDutyEncoderRIO.this;
        }
    }

    /**
     * Make sure to put this periodic in 
     */
    public void periodic(){
        rawWriter.set(getPosition());//0-1
        degreeWriter.set(getDegrees());//0-360
        radianWriter.set(getRadian());//PI-2PI
        isConnectedWriter.set(encoder.isConnected());

    }

    
    public double getDegrees() {
        return getPosition()*(360);
    }
    public double getRadian() {
        return getPosition()*(2*Math.PI);
    }

    /**
     * 
     * @return The Absolute Position of the Encoder 
     */
    public double getPosition(){
        double givenPos = encoder.getAbsolutePosition();
        if(isInverted){ //To invert values based on direction - Works
            givenPos = 1-givenPos;
        }

        givenPos -= encoder.getPositionOffset();
        if(givenPos<0){ //To account for negative values/rollovers - no work
            givenPos=1+(givenPos); 
            //^^ In this case the givePos will be negative; or 1-Math.abs(givenPos)
        }

        
        return givenPos;
    }
}
