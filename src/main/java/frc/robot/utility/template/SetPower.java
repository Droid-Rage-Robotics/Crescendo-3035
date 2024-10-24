package frc.robot.utility.template;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.utility.motor.old.SafeCanSparkMax;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SetPower {
    private final SafeCanSparkMax[] motors;
    private final ShuffleboardValue<Double> powerWriter;
    private final int mainNum;

    public SetPower(
        SafeCanSparkMax[] motors,
        String name,
        int mainNum
    ){
        this.motors=motors;
        this.mainNum = mainNum;

        powerWriter = ShuffleboardValue
            .create(0.0, name+"/Power", name)
            .build();
    }

    public Command setTargetPositionCommand(double power){
        return new InstantCommand(()->setTargetPosition(power));
    }

    /*
     * Use this for initialization
     */
    public void setTargetPosition(double power) {
        powerWriter.set(power);
        for (SafeCanSparkMax motor: motors) {
            motor.setPower(power);
        }
    }
}
