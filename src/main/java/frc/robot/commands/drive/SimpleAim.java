package frc.robot.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.vision.Vision;

public class SimpleAim extends Command{
    private SwerveDrive drive;
    private Vision vision;
    private PIDController controller;
    //  private final ShuffleboardValue<Double> writer = 
        // ShuffleboardValue.create(0.0, "SImple AIm Test number", 
        // SwerveDrive.class.getSimpleName()).build();
    
    public SimpleAim(SwerveDrive drive, Vision vision){
        this.drive = drive;
        this.vision = vision;
        controller = new PIDController(.06, 0, 0);
        controller.setTolerance(1);
    }

    @Override
    public void initialize() {
        controller.reset();
    }

    @Override
    public void execute() {
        //x/y can be controller
        // writer.set(getTurn());
        drive.drive(0,0, getTurn());

        // if(controller.atSetpoint()){
        //     drive.stop();
        //     return;
        // }
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }

    private double getTurn(){
        double num = controller.calculate(vision.gettX(), 0);
        if (num > 1){
            return 1;
        } else if(num < .05){
            return 0;
        }
        else return controller.calculate(vision.gettX(), 0);
    }
}
