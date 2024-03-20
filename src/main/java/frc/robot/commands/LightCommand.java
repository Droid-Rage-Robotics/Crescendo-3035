package frc.robot.commands;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Light;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeWheel;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class LightCommand extends Command {
    public enum IntakeState {
        INTAKE,
        ElEMENT_IN,
        ALIGN_TO_SHOOT,
        READY_TO_SHOOT,
        

        END_GAME
    }
    IntakeState intakeState = IntakeState.INTAKE;
    Light light;
    CommandXboxController driver, operator;
    Intake intake;
    protected final ShuffleboardValue<String> intakeStateWriter = ShuffleboardValue.create
        ("Test", "Intake State", IntakeWheel.class.getSimpleName()).build();



    public LightCommand(Intake intake,
        Light light, 
        CommandXboxController driver, 
        CommandXboxController operator) {
            this.intake =intake;
            this.light=light;
            this.driver=driver;
            this.operator=operator;
            addRequirements(light);
    }

    @Override
    public void initialize() {
    }

    @SuppressWarnings("unused")
    @Override
    public void execute() {
        //List is with Order of Priority
        if(getMatchTime()<20&&getMatchTime()>18){   
            intakeState=IntakeState.END_GAME;
        } else if ((intake.isElementInClaw())){
            intakeState=IntakeState.ElEMENT_IN;
        } else if(driver.getRightTriggerAxis()>0.5){//If Intaking
            intakeState=IntakeState.INTAKE;
        } else if(true){//How to recognize that the robot is aimed to the shooter
            intakeState=IntakeState.READY_TO_SHOOT;
        } else if(operator.getRightTriggerAxis()>0.5){//Whatever Button that will be used for shooting
            intakeState=IntakeState .ALIGN_TO_SHOOT;
        } else{
            //stuff
        }

        switch(intakeState){
            case ElEMENT_IN:
                driver.getHID().setRumble(RumbleType.kBothRumble, 1);
                break;
            case INTAKE:
           
            case ALIGN_TO_SHOOT:
                break;
            case READY_TO_SHOOT:
                break;
            case END_GAME://TODO:Test
                light.setAlternatingColor(light.green, light.red);
                break;
        }
        
        setLightState(intakeState);
    }

    @Override
    public void end(boolean interrupted) {
        intakeState=IntakeState.INTAKE;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    public double getMatchTime(){//TODO:test
          return DriverStation.getMatchTime();
    }

    public void setLightState(IntakeState state){
        intakeStateWriter.set(state.name());
    }
}
