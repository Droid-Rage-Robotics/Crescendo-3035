package frc.robot.utility.template;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Arm extends SubsystemBase {
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
    protected final ShuffleboardValue<Double> positionRadianWriter;
    protected final ShuffleboardValue<Double> positionDegreeWriter;
    protected final ShuffleboardValue<Double> targetRadianWriter;
    protected final ShuffleboardValue<Double> targetDegreeWriter;
    private final ShuffleboardValue<Double> voltageWriter;
    int mainNum;

    public Arm(
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

        positionDegreeWriter = ShuffleboardValue
            .create(0.0, name+"/PositionDegree", name)
            .build();
        targetDegreeWriter = ShuffleboardValue
            .create(0.0, name+"/TargetDegree", name)
            .build();
        positionRadianWriter = ShuffleboardValue
            .create(0.0, name+"/PositionRadian", name)
            .build();
        targetRadianWriter = ShuffleboardValue
            .create(0.0, name+"/TargetRadian", name)
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
        positionRadianWriter.write(position);
        positionDegreeWriter.write(Math.toDegrees(position));
        return position;
    }

    public SafeCanSparkMax getMotors(){
        return motors[mainNum];
    }
}
