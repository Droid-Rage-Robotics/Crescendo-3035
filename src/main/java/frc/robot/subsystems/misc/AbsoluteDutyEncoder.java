package frc.robot.subsystems.misc;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Shooter;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

@Deprecated
public class AbsoluteDutyEncoder extends SubsystemBase {
    protected final DutyCycleEncoder encoder;
    protected final ShuffleboardValue<Double> encoderPos = ShuffleboardValue.create
        (0.0, "Encoder Position", Shooter.class.getSimpleName())
        .build();
    public AbsoluteDutyEncoder(int channelNum, double distancePerRotation, double offset) {
        encoder = new DutyCycleEncoder(channelNum);
        encoder.setDistancePerRotation(distancePerRotation);
        encoder.setPositionOffset(offset);
    }

    @Override
    public void periodic() {
        encoderPos.set(encoder.getAbsolutePosition());
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }
}  