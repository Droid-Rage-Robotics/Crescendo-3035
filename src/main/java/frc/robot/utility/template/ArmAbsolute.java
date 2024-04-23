package frc.robot.utility.template;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.utility.motor.SafeCanSparkMax;

public class ArmAbsolute extends Arm {
    public ArmAbsolute(
        SafeCanSparkMax[] motors,
        PIDController controller,
        ArmFeedforward feedforward,
        double maxPosition,
        double minPosition,
        Control control,
        String name,
        int mainNum
    ){
        super(motors, controller, feedforward, 
        maxPosition, minPosition, control, 
        name, mainNum);
        //TODO: Need to add encoder

    }
    
}
