package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@Deprecated
public class LimitSwitch extends SubsystemBase {
    protected final DigitalInput intakeLimitSwitch; //Limit Switch?

    public LimitSwitch(Boolean isEnabled) {
        intakeLimitSwitch = new DigitalInput(5);//WHERE is it plugged in
    }

    @Override
    public void periodic() {
        // if(intakeLimitSwitch.get()){    //NEED to check whether to add !
        //     resetEncoder();
        // }
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }

    public boolean isLimitSwitch(){
        return intakeLimitSwitch.get();
    }
}  