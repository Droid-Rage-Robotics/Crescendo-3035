package frc.robot.utility.motor.old;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.MusicTone;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.RobotController;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class SafeTalonFX extends SafeMotor{
    //Can use in relative and Absolute Encoder
    private double velocityConversionFactor, positionConversionFactor;
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
    private TalonFXConfiguration configuration = new TalonFXConfiguration();

    public SafeTalonFX(int deviceNumber, boolean isInverted, 
        IdleMode mode, double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter) {
            this(deviceNumber, isInverted, 
            mode, positionConversionFactor,
            positionConversionFactor/60,
            isEnabled, 
            outputWriter, 
            "", 0,0);
    }

    public SafeTalonFX(int deviceNumber, boolean isInverted, 
        IdleMode mode, double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter, 
        double supplyCurrentLimit) {
            this(deviceNumber, isInverted, 
            mode, positionConversionFactor,
            positionConversionFactor/60,
            isEnabled, 
            outputWriter,
            "", 
            supplyCurrentLimit,0);
    }

    public SafeTalonFX(int deviceNumber, boolean isInverted, 
        IdleMode mode, double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter, String canName) {
            this(deviceNumber, isInverted, 
            mode, positionConversionFactor,
            positionConversionFactor/60,
            isEnabled, 
            outputWriter, canName, 0,0);
    }

    public SafeTalonFX(int deviceNumber, boolean isInverted, 
        IdleMode mode, double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter, 
        String canName, double supplyCurrentLimit) {
            this(deviceNumber, isInverted, 
            mode, positionConversionFactor,
            positionConversionFactor/60,
            isEnabled, 
            outputWriter, canName,
            supplyCurrentLimit,0);
    }

    public SafeTalonFX(int deviceNumber, boolean isInverted, 
        IdleMode mode, double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter,
        double supplyCurrentLimit, double statorCurrentLimit) {
            this(deviceNumber, isInverted, mode, 
            positionConversionFactor, positionConversionFactor/60, 
            isEnabled, outputWriter, "", supplyCurrentLimit);
    }

    // motor.getVoltageCompensationNominalVoltage()
    public SafeTalonFX(int deviceNumber, boolean isInverted, 
        IdleMode mode, double positionConversionFactor,
        double velocityConversionFactor,
        ShuffleboardValue<Boolean> isEnabled, 
        ShuffleboardValue<Double> outputWriter, 
        String canName,
        double supplyCurrentLimit, double statorCurrentLimit) {
            super(isEnabled, outputWriter);
            motor = new TalonFX(deviceNumber);
            
            motor.setInverted(isInverted);
            setIdleMode(mode);
            configuration.Audio.AllowMusicDurDisable = false; //true
            // configuration.Feedback.SensorToMechanismRatio = positionConversionFactor;    //Does not work as intended
            if(supplyCurrentLimit!=0){
                configuration.CurrentLimits.SupplyCurrentLimitEnable =true;
                configuration.CurrentLimits.SupplyCurrentLimit = supplyCurrentLimit;
            }
            if(statorCurrentLimit!=0){
                configuration.CurrentLimits.StatorCurrentLimitEnable =true;
                configuration.CurrentLimits.StatorCurrentLimit = supplyCurrentLimit;
            }
            
            this.positionConversionFactor = positionConversionFactor;
            this.velocityConversionFactor = velocityConversionFactor;

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

    // @Override
    // public void setInverted(boolean isInverted) {
    //     motor.setInverted(isInverted);
    // }

    @Override
    public void setIdleMode(IdleMode mode) {
        motor.setNeutralMode(switch (mode) {
            case Brake -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
    }

    @Override
    public double getVelocity() {
        return motor.getVelocity().getValueAsDouble()*velocityConversionFactor;
        // return motor.getVelocity().getValueAsDouble();

    }
    @Override
    public double getSpeed() {
        return motor.get();
    }

    public double getPosition() {
        return motor.getPosition().getValueAsDouble()*positionConversionFactor;
        // return motor.getPosition().getValueAsDouble();

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


    // @Override
    // public void setPositionConversionFactor(double num) {
    //     configuration.Feedback.SensorToMechanismRatio = num;
    //     //TODO:Test
    // }
    // @Override
    // public void setVelocityConversionFactor(double num) {
    //     velocityConversionFactor = num;
    //     //TODO:Test
    // }

    // @Override
    // public void set(double num){
    //     setPower(num);
    // }

    @Override
    public int getDeviceID(){
        return motor.getDeviceID();
    }

    // public void setSmartCurrentLimit(int num){
    //     motor.Smart
    // }

    public double getVoltage(){
        return motor.getMotorVoltage().getValueAsDouble();//The applied (output) motor voltage//1.58 Most Consistent
        // return motor.getSupplyCurrent().getValueAsDouble();//Measured supply side current
        // return motor.getSupplyVoltage().getValueAsDouble();//Measured supply voltage to the TalonFX//no
        // return motor.getTorqueCurrent().getValueAsDouble();//Stator current where positive current means 
            //torque is applied in the forward direction as determined by the Inverted setting; 
            //Doesn't seem like this ^^
    }
}