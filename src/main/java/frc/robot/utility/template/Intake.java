package frc.robot.utility.template;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Intake extends SubsystemBase{
    private final CANMotorEx[] motors;
    private final PIDController controller;
    private final SimpleMotorFeedforward feedforward;
    private final Control control;
    private final double maxSpeed;
    private final double minSpeed;
    private final ShuffleboardValue<Double> speedWriter;
    private final ShuffleboardValue<Double> targetWriter;
    private final ShuffleboardValue<Double> voltageWriter;
    private final int mainNum;

    public Intake(
        CANMotorEx[] motors,
        PIDController controller,
        SimpleMotorFeedforward feedforward,
        double maxSpeed,
        double minSpeed,
        Control control,
        String name,
        int mainNum
    ){
        this.motors=motors;
        this.controller=controller;
        this.feedforward=feedforward;
        this.control=control;
        this.maxSpeed=minSpeed;
        this.minSpeed=minSpeed;
        this.mainNum=mainNum;

        speedWriter = ShuffleboardValue
            .create(0.0, name+"/Speed", name)
            .build();
        targetWriter = ShuffleboardValue
            .create(0.0, name+"/TargetSpeed", name)
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
        if(target>maxSpeed||target<minSpeed) return;
        targetWriter.set(target);
    }

    protected void setVoltage(double voltage) {
        voltageWriter.set(voltage);
        for (CANMotorEx motor: motors) {
            motor.setVoltage(voltage);
        }
    }
    
    public void resetEncoder() {
        for (CANMotorEx motor: motors) {
            motor.resetEncoder(0);
        }
    }

    public double getEncoderPosition() {
        double position = motors[mainNum].getPosition();
        speedWriter.write(position);
        return position;
    }

    public CANMotorEx getMotors(){
        return motors[mainNum];
    }
}
