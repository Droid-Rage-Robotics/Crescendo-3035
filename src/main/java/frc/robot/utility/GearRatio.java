package frc.robot.utility;

public abstract class GearRatio {
    // (count.countPerPulse * gearRatio) - This allows 
    // for you to know how many rotations the output 
    // shaft does compared to the input
    public enum Type{
        DISTANCE,   //Linear - inches
        SPEED,      //Shooter - RPM
        ROTATION    //Arm - Degrees
    }
    public enum PulseCount{
        NEO(42), //42 counts per rev.
        KRAKEN(1), //2048, but the position is already given in rotations
        FALCON(2048),   //(2048 steps per rotation)
        THROUGH_BORE_RELATIVE(4096), //2048 Cycles per Revolution (8192 Counts per Revolution)
        THROUGH_BORE_ABSOLUTE(1),
        CANCODER(4096)  //On the CTR Documentation
        ;

        double countPerPulse;
        private PulseCount(double countPerPulse){
            this.countPerPulse=countPerPulse;
        }
    }

    /**
    *Create a GearedMechanism with a gear ratio.
    *
    * <p>For example, if the pinion is 10 teeth and the  gear is 50 teeth, 
    *    the gear ratio is 1:5 and therefore the value is (1/5) = 0.2.
    * @param count = How many pulses does the encoder get to recognize as 1 rotation
    * <p>gearRatio is 1 if used with encoder on output shaft
    * @param gearRatio Gear ratio (input / output, driving:driven) - This needs to thought about
    * @param diameter in inches
    */
    public static double getConversion(Type type, PulseCount count, 
        double gearRatio, double diameter){
        if(type==Type.DISTANCE){
            return (count.countPerPulse * gearRatio) / (diameter * Math.PI);
            //(count.countPerPulse * gearRatio) / (diameter * Math.PI);
            //(count.countPerPulse*gearRatio) * (diameter * Math.PI)
            //((count.countPerPulse * gearRatio)*(2*Math.PI))*(diameter /2);
            
            //s=radius*theta(in radians)
            //(count.countPerPulse*gearRatio) - This should give the rotation
        } else{
            return getConversion(type, count, gearRatio);
        }

    }
    /**
    *Create a GearedMechanism with a gear ratio.
    *
    * <p>For example, if the pinion is 10 teeth and the  gear is 50 teeth, 
    *    the gear ratio is 1:5 and therefore the value is (1/5) = 0.2.
    * @param count = How many pulses does the encoder get to recognize as 1 rotation
    * <p>gearRatio is 1 if used with encoder on output shaft
    * @param gearRatio Gear ratio (input / output, driving:driven) - This needs to thought about
    */
    public static double getConversion(Type type, PulseCount count, double gearRatio){
        switch (type) {
            case DISTANCE:
                return 1; // For Distance, you need to give gearRatio/diameter
            case ROTATION:
                return (count.countPerPulse*gearRatio)*(2*Math.PI);//Radians
                //Worked for abs (2 * Math.PI) where count=1
            case SPEED:
                return count.countPerPulse*gearRatio;
                //If set to the position conversion, it should do rpm
            default:
                return 1;//count.countPerPulse*gearRatio
        }

    }

    // public GearRatio(){};
}
