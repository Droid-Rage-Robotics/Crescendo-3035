package frc.robot.utility.template;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Elevator extends SubsystemBase {
    public enum Control{
        PID,
        FEEDFORWARD
    }
    SafeCanSparkMax[] motors;
    PIDController controller;
    ElevatorFeedforward feedforward;
    Control control;
    double maxPosition;
    double minPosition;
    protected final ShuffleboardValue<Double> positionWriter;
    protected final ShuffleboardValue<Double> targetWriter;
    private final ShuffleboardValue<Double> voltageWriter;
    int mainNum;

    public Elevator(
        SafeCanSparkMax[] motors,
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
    }

    protected void setVoltage(double voltage) {
        voltageWriter.set(voltage);
        for (SafeCanSparkMax motor: motors) {
            motor.setVoltage(voltage);
        }
    }
    
    public void resetEncoder() {
        for (SafeCanSparkMax motor: motors) {
            // motor.getEncoder().setPosition(0);
            motor.reset(0);
        }
    }

    public double getEncoderPosition() {
        double position = motors[mainNum].getPosition();
        positionWriter.write(position);
        return position;
    }

    public SafeCanSparkMax getMotors(){
        return motors[mainNum];
    }
}
