package frc.robot.utility.motor;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.MusicTone;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.RobotController;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeTalonFX extends SafeMotor{
    private final TalonFX motor;
    private final Orchestra orchestra;
    private final MusicTone music = new MusicTone(20);
    private final String[] songs = new String[] {
        "song1.chrp",
        "song2.chrp",
        "song3.chrp",
        "song4.chrp",
        "song5.chrp",
        "song6.chrp",
        "song7.chrp",
        "song8.chrp",
        "song9.chrp", /* the remaining songs play better with three or more FXs */
        "song10.chrp",
        "song11.chrp",
      };
      /* schedule a play request, after a delay.  
            This gives the Orchestra service time to parse chirp file.
            If play() is called immedietely after, you may get an invalid action error code. */
        private int timeToPlayLoops = 10;


    public SafeTalonFX(int deviceNumber, boolean isInverted, 
        IdleMode mode, ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter) {
        super(isEnabled, outputWriter);
        motor = new TalonFX(deviceNumber);
        
        TalonFXConfiguration configuration = new TalonFXConfiguration();
        if(isInverted){
            configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        } else {
            configuration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        }
        motor.setNeutralMode(switch (mode) {
            case Brake -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
        configuration.Audio.AllowMusicDurDisable = false; //true
        // configuration.Feedback.SensorToMechanismRatio = 1;

        motor.getConfigurator().apply(configuration);

        orchestra = new Orchestra();
        orchestra.addInstrument(motor);
    }


    public void setPower(double power) {
        outputWriter.write(power);
        if (!isEnabled.get()) motor.set(0);
            else motor.set(power);
    }

    public void setVoltage(double outputVolts) {
        outputWriter.write(outputVolts);
        if (!isEnabled.get()) motor.set(0);
            else motor.set(outputVolts / RobotController.getBatteryVoltage());
    }

    @Override
    public void setInverted(boolean isInverted) {
        motor.setInverted(isInverted);;
    }

    @Override
    public void setIdleMode(IdleMode mode) {
        // motor.setNeutralMode(switch (mode) {
        //     case Brake -> NeutralModeValue.Brake;
        //     case Coast -> NeutralModeValue.Coast;
        // });
    }

    public double getVelocity() {
        return motor.getVelocity().getValueAsDouble();
    }

    public double getPosition() {
        return motor.getPosition().getValueAsDouble();
    }

    public void setPosition(double position) {
        motor.setPosition(position);
    }

    public TalonFXConfigurator getConfigurator(){
        return motor.getConfigurator();
    }

    public void playMusic(int songNum){
        loadMusic(songNum);

        if (timeToPlayLoops > 0) {
            --timeToPlayLoops;
            if (timeToPlayLoops == 0) {
                /* scheduled play request */
                System.out.println("Auto-playing song.");
                orchestra.play();
            }
        }
        orchestra.play();
    }

    public void pauseMusic(){
        orchestra.pause();
    }

    public void stopMusic(){
        orchestra.stop();
    }

    public void playFrequency(){
        motor.setControl(music);
    }
    public void stopFrequency(){
        motor.stopMotor();
    }

    private void loadMusic(int songNum)
    {
        int _songSelection = 0;

        /* increment song selection */
        _songSelection += songNum;
        /* wrap song index in case it exceeds boundary */
        if (_songSelection >= songs.length) {
            _songSelection = 0;
        }
        if (_songSelection < 0) {
            _songSelection = songs.length - 1;
        }
        /* load the chirp file */
        orchestra.loadMusic(songs[_songSelection]); 

        /* print to console */
        // System.out.println("Song selected is: " + songs[_songSelection] + ".  Press left/right on d-pad to change.");
    }
}