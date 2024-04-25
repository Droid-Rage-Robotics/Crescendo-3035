package frc.robot.utility.template;

import com.revrobotics.AbsoluteEncoder;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.subsystems.misc.AbsoluteDutyEncoder;
import frc.robot.utility.motor.SafeCanSparkMax;

public class ArmAbsolute extends Arm {
    public enum Location{
        RIO,
        CONTROLLER
    }
    public ArmAbsolute(
        SafeCanSparkMax[] motors,
        PIDController controller,
        ArmFeedforward feedforward,
        double maxPosition,
        double minPosition,
        Control control,
        String name,
        int mainNum,
        AbsoluteDutyEncoder encoder
    ){
        super(motors, controller, feedforward, 
        maxPosition, minPosition, control, 
        name, mainNum);
        //TODO: Need to add encoder

    }
    
}
