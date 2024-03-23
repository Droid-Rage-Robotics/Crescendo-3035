package frc.robot.utility.InfoTracker;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Shooter;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class CycleTracker {
    public enum ScorePos{
        SPEAKER,
        AMP,
        TRAP
    }
    protected StatCalculator stat = new StatCalculator();
    protected double lastTime = 0;
    protected Timer timer = new Timer();
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

    public CycleTracker (){
        timer.start();
    }
    
    public void trackCycle(ScorePos num){
        // double cycleTime = timer.get();
        double cycleTime = DriverStation.getMatchTime() - lastTime;
        lastTime= DriverStation.getMatchTime();
        stat.addNumber(cycleTime);
        cycle.set(stat.getSizeDouble());
        mean.set(stat.getMean());
        slowest.set(stat.getLowestValue());
        fastest.set(stat.getHighestValue());
        switch (num){
            case AMP:
                amp.set(amp.get()+1);
                break;
            case SPEAKER:
                speaker.set(speaker.get()+1);
                break;
            case TRAP:
                break;
        }
    }
}
