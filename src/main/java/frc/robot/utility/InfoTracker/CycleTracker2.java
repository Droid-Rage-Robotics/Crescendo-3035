package frc.robot.utility.InfoTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class CycleTracker2 {
    //TODO:Test
    protected StatCalculator stat = new StatCalculator();
    protected Timer timer = new Timer();
    private final File file = new File("/home/lvuser/Logs");
    //Add in data like time of match, what type it is from DRIVER STATION
    private final PrintStream stream;
/* SIngleton - {@link CommandScheduler} {@link Sendable}*/ 
    
    // private HashMap<String, Double> data;
    protected final ShuffleboardValue<Double> cycle = 
        ShuffleboardValue.create(0.0, "Cycles:", "Misc")
            .withSize(1, 2)
            .build();
    protected final ShuffleboardValue<Double> mean = 
        ShuffleboardValue.create(0.0, "Mean:", "Misc")
            .withSize(1, 2)
            .build();
    protected final ShuffleboardValue<Double> fastest = 
        ShuffleboardValue.create(0.0, "Fastest Time:", "Misc")
            .withSize(1, 2)
            .build();
    protected final ShuffleboardValue<Double> slowest = 
        ShuffleboardValue.create(0.0, "Slowest Time:", "Misc")
            .withSize(1, 2)
            .build();
    protected final ShuffleboardValue<Double> amp = 
        ShuffleboardValue.create(0.0, "Amp:", "Misc")
            .withSize(1, 2)
            .build();
    protected final  ShuffleboardValue<Double> speaker = 
        ShuffleboardValue.create(0.0, "Speaker:", "Misc")
            .withSize(1, 2)
            .build();

    public CycleTracker2 () throws FileNotFoundException{
        timer.start();
        stream = new PrintStream(file); 
    }
    
    public void trackCycle(Shooter.ShooterSpeeds num){
        double cycleTime = timer.get();
        stat.addNumber(cycleTime);
        cycle.set(stat.getSizeDouble());
        mean.set(stat.getMean());
        slowest.set(stat.getLowestValue());
        fastest.set(stat.getHighestValue());
        switch (num){
            case AMP:
                amp.set(amp.get()+1);
            case SPEAKER:
                speaker.set(speaker.get()+1);
            case POSITION_TOLERANCE:
            case STOP:
                break;
        }
    }

    public void printOut(String string, double num){
        // data.put(string, num);
        stream.print(string+num);
    }
    public void printAllData(){
        printOut(DriverStation.getMatchType().toString(), DriverStation.getMatchNumber());

        for (int i = 0; i < stat.getSizeInt(); i++) {
            printOut("Cycle "+ i +":", stat.getNum(i));
        }
        printOut("Amp: ", amp.get());
        printOut("Speaker: ", speaker.get());
        printOut("Total Cycles: ", cycle.get());
        printOut("Mean", mean.get());
        printOut("Fastest: ", stat.getHighestValue());
        printOut("SLowest", stat.getLowestValue());
    }
}
