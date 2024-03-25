package frc.robot.subsystems.climb;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.general.DisabledCommand;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
// @Deprecated
public class ClimbAlternate extends Climb{
    private SafeCanSparkMax sparkMax;
    public ClimbAlternate(Boolean isEnabledLeft, Boolean isEnabledRight, SafeCanSparkMax sparkMax) {
        super(isEnabledLeft,isEnabledRight);
        this.sparkMax = sparkMax;
    }

    @Override
    public void periodic() {
        setPower(controller.calculate(getEncoderPosition()));
    } 

    public void resetEncoder() {
        motorL.getEncoder().setPosition(0);
    }

    
    public double getEncoderPosition() {
        double position = sparkMax.getAlternateEncoder(1024).getPosition();
        // sparkMax.
        //2048, 8192, 4096
        // double position = motorL.getPosition(); //If You want to Change what motor is giving the values
        encoderPositionWriter.write(position);
        return position;
    }

    @Override
    public double getError(){
        return controller.getPositionError();
    }
}
