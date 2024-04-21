package frc.robot.subsystems.misc;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

// @Deprecated
public class AbsoluteDutyEncoder extends SubsystemBase {
    protected final DutyCycleEncoder encoder;
    protected final ShuffleboardValue<Double> encoderPos = ShuffleboardValue.create
        (0.0, "Encoder Position", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Boolean> isConnectedValue = ShuffleboardValue.create
        (true, "isConnectedValue", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Double> offset = ShuffleboardValue.create
        (0.0, "Offset", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
        protected final ShuffleboardValue<Double> distance = ShuffleboardValue.create
        (0.0, "Distance", AbsoluteDutyEncoder.class.getSimpleName())
        .build();
    public AbsoluteDutyEncoder(int channelNum, double distancePerRotation, double offset) {
        encoder = new DutyCycleEncoder(channelNum);
        encoder.setDistancePerRotation(distancePerRotation);

        // encoder.setPositionOffset(offset);
        // encoder.reset();
        // encoder.isConnected();//use this to show disconnections of encoder
    }

    @Override
    public void periodic() {
        offset.set(encoder.getPositionOffset());

        isConnectedValue.set(encoder.isConnected());
        // encoderPos.set((encoder.getAbsolutePosition()-encoder.getPositionOffset())+.75);
        encoderPos.set((encoder.getAbsolutePosition()-encoder.getPositionOffset())-.25);

        // encoderPos.set(encoder.getDistance());
        distance.set(encoder.getDistancePerRotation());
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }
}  