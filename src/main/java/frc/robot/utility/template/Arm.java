package frc.robot.utility.template;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Arm extends SubsystemBase {
    protected final CANMotorEx[] motors;
    protected final PIDController controller;
    protected final ArmFeedforward feedforward;
    protected final Control control;
    protected final double maxPosition;
    protected final double minPosition;
    protected final double offset;
    protected final ShuffleboardValue<Double> positionRadianWriter;
    protected final ShuffleboardValue<Double> positionDegreeWriter;
    protected final ShuffleboardValue<Double> targetRadianWriter;
    protected final ShuffleboardValue<Double> targetDegreeWriter;
    protected final int mainNum;

    public Arm(
        CANMotorEx[] motors,
        PIDController controller,
        ArmFeedforward feedforward,
        double maxPosition,
        double minPosition,
        double offset,
        Control control,
        String subsystemName,
        int mainNum
    ){
        this.motors=motors;
        this.controller=controller;
        this.feedforward=feedforward;
        this.control=control;
        this.maxPosition=maxPosition;
        this.minPosition=minPosition;
        this.offset=offset;
        this.mainNum=mainNum;

        positionDegreeWriter = ShuffleboardValue
            .create(0.0, subsystemName+"/PositionDegree", subsystemName)
            .build();
        targetDegreeWriter = ShuffleboardValue
            .create(0.0, subsystemName+"/TargetDegree", subsystemName)
            .build();
        positionRadianWriter = ShuffleboardValue
            .create(0.0, subsystemName+"/PositionRadian", subsystemName)
            .build();
        targetRadianWriter = ShuffleboardValue
            .create(0.0, subsystemName+"/TargetRadian", subsystemName)
            .build();
    }

    @Override
    public void periodic() {
        // targetDegreeWriter.set(Math.toDegrees(controller.getSetpoint()));
        // targetRadianWriter.set(controller.getSetpoint());
        switch(control){
            case PID:
                setVoltage(controller.calculate(getEncoderPosition(), targetRadianWriter.get()));
                // setVoltage((controller.calculate(getEncoderPosition(), getTargetPosition())) + .37);
                //.37 is kG ^^
                break;
            case FEEDFORWARD:
                setVoltage(controller.calculate(getEncoderPosition(), targetRadianWriter.get())
                +feedforward.calculate(1,1)); 
                //ks * Math.signum(velocity) + kg + kv * velocity + ka * acceleration; ^^
                break;
        };        
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }
    
    public Command setTargetPositionCommand(double degree){
        return new InstantCommand(()->setTargetPosition(degree));
    }

    /*
     * Use this for initialization
     */
    public void setTargetPosition(double degree) {
        if(degree>maxPosition||degree<minPosition) return;
        targetDegreeWriter.set(degree);
        targetRadianWriter.set(Math.toRadians(degree));
        controller.setSetpoint(Math.toRadians(degree));
    }

    protected void setVoltage(double voltage) {
        for (CANMotorEx motor: motors) {
            motor.setVoltage(voltage);
        }
    }
    
    public void resetEncoder() {
        for (CANMotorEx motor: motors) {
            // motor.getEncoder().setPosition(0);
            motor.resetEncoder(0);
        }
    }

    public double getEncoderPosition() {
        double radian = motors[mainNum].getPosition();
        positionRadianWriter.write(radian);
        positionDegreeWriter.write(Math.toDegrees(radian));
        return radian;
    }

    public CANMotorEx getMotors(){
        return motors[mainNum];
    }
}
