package frc.robot.utility.template;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.encoder.AbsoluteDutyEncoderRIO;
import frc.robot.utility.motor.better.CANMotorEx;

public class ArmAbsolute extends Arm {
    protected AbsoluteDutyEncoderRIO encoder;
    public ArmAbsolute(
        CANMotorEx[] motors,
        PIDController controller,
        ArmFeedforward feedforward,
        double maxPosition,
        double minPosition,
        double offset,
        Control control,
        String subsystemName,
        int mainNum,
        AbsoluteDutyEncoderRIO encoder
    ){
        super(motors, controller, feedforward, 
        maxPosition, minPosition, offset, control, 
        subsystemName, mainNum);
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
        positionRadianWriter.write(encoder.getRadian());
        positionDegreeWriter.write(encoder.getDegrees());
        return encoder.getDegrees();
    }
    
}
