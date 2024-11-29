package frc.robot.utility.template;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

//Works
public class Elevator extends SubsystemBase {
    private final CANMotorEx[] motors;
    private final PIDController controller;
    private final ElevatorFeedforward feedforward;
    private final Control control;
    private final double maxPosition;
    private final double minPosition;
    private final ShuffleboardValue<Double> positionWriter;
    private final ShuffleboardValue<Double> targetWriter;
    private final ShuffleboardValue<Double> voltageWriter;
    private final int mainNum;

    /**
     * @param motors - The Motors to Control
     * @param controller - PID Controller
     * @param feedforward - Feedforward
     * @param maxPosition 
     * @param minPosition
     * @param control - PID or FEEDFORWARD
     * @param name - Name of Subsystem
     * @param mainNum - Motor to use for Encoder
     */
    public Elevator(
        CANMotorEx[] motors,
        PIDController controller,
        ElevatorFeedforward feedforward,
        double maxPosition,
        double minPosition,
        Control control,
        String name,
        int mainNum
    ){
        this.motors=motors;
        this.controller=controller;
        this.feedforward=feedforward;
        this.control=control;
        this.maxPosition=maxPosition;
        this.minPosition=minPosition;
        this.mainNum=mainNum;

        positionWriter = ShuffleboardValue
            .create(0.0, name+"/Position", name)
            .build();
        targetWriter = ShuffleboardValue
            .create(0.0, name+"/Target", name)
            .build();
        voltageWriter = ShuffleboardValue
            .create(0.0, name+"/Voltage", name)
            .build();
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
                +feedforward.calculate(1,1)); //To Change #
                //ks * Math.signum(velocity) + kg + kv * velocity + ka * acceleration; ^^
                break;
        };        
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }

    public Command setTargetPositionCommand(double target){
        return new InstantCommand(()->setTargetPosition(target));
    }

    /*
     * Use this for initialization
     */
    public void setTargetPosition(double target) {
        if(target>maxPosition||target<minPosition) return;
        targetWriter.set(target);
        controller.setSetpoint(target);
    }

    protected void setVoltage(double voltage) {
        voltageWriter.set(voltage);
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
        double position = motors[mainNum].getPosition();
        positionWriter.write(position);
        return position;
    }

    public CANMotorEx getMotors(){
        return motors[mainNum];
    }
}
