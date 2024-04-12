package frc.robot.subsystems.vision;

import edu.wpi.first.wpilibj.I2C;

public class Pixy {

    private I2C i2c = new I2C(I2C.Port.kOnboard, 0);

    public Pixy(){
        
    }
    
}
