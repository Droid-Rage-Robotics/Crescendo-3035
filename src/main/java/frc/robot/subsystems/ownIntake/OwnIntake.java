package frc.robot.subsystems.ownIntake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class OwnIntake extends SubsystemBase {
    
    private CANSparkMax intakeMotorOne = new CANSparkMax(10, MotorType.kBrushless);
    private CANSparkMax intakeMotorTwo = new CANSparkMax(11, MotorType.kBrushless);
    public OwnIntake(){
    }


    @Override
    public void periodic(){}

    public void setPower(double setPowerB){} 
    

}
