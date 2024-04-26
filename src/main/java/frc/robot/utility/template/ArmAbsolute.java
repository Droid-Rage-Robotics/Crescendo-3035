package frc.robot.utility.template;

import com.revrobotics.AbsoluteEncoder;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.utility.encoder.AbsoluteDutyEncoder;
import frc.robot.utility.motor.SafeCanSparkMax;

public class ArmAbsolute extends Arm {
    // public enum Location{
    //     RIO,
    //     CONTROLLER
    // }
    protected AbsoluteDutyEncoder encoder;
    public ArmAbsolute(
        SafeCanSparkMax[] motors,
        PIDController controller,
        ArmFeedforward feedforward,
        double maxPosition,
        double minPosition,
        Control control,
        String name,
        int mainNum,
        AbsoluteDutyEncoder encoder
    ){
        super(motors, controller, feedforward, 
        maxPosition, minPosition, control, 
        name, mainNum);
        this.encoder=encoder;

    }

    @Override
    public void periodic() {
        switch(control){
            case PID:
                setVoltage(controller.calculate(getEncoderPosition(), controller.getSetpoint()));
                // setVoltage((controller.calculate(getEncoderPosition(), getTargetPosition())) + .37);
                //.37 is kG ^^
                break;
            case FEEDFORWARD:
                setVoltage(controller.calculate(getEncoderPosition(), controller.getSetpoint())
                +feedforward.calculate(1,1)); 
                //ks * Math.signum(velocity) + kg + kv * velocity + ka * acceleration; ^^
                break;
        };
    }

    @Override
    public double getEncoderPosition() {
        double position = encoder.calculateAbsPos();
        positionRadianWriter.write(position);
        positionDegreeWriter.write(Math.toDegrees(position));
        return position;
    }
    
}
