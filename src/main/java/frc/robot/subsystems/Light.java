package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Light extends SubsystemBase {
  //You can only make one addressable led
    private final AddressableLED ledOne;
    private final AddressableLEDBuffer bufferOne;

    private final ShuffleboardValue<String> lightWriter = ShuffleboardValue.create
        ("Color", "Light Color", Light.class.getSimpleName())
        .build();
    
    private int LED_COUNT_ONE = 13;   //Number of LEDS on Strip (Can have multiple strips)
    // private int LED_COUNT_TWO = 39;   //Number of LEDS on Strip (Can have multiple strips)
    public final Color red = Color.kRed, 
                      batteryBlue = Color.kMidnightBlue,
                      orange = Color.kOrange, //Has Ring 
                      purple = Color.kPurple,
                      yellow = Color.kOrangeRed,
                      blue = Color.kBlue, //Decoration
                      green = Color.kGreen, //Ready to Shoot?
                      white = Color.kWhite; //Aligned?
    
    // class Rainbow{
    //   private int rainbowFirstPixelHue = 0;
    // }
    class SwitchLED{
      private boolean on = true;
      private double lastChange;
    }
    class FlashingColor{
      private long waitTime = 200, 
                    startTime = System.currentTimeMillis();
      private int stage = 0;
    }
    private SwitchLED switchLED;
    private FlashingColor flashingColor;
    // private Rainbow rainbow;
    
    public Light() {
        ledOne = new AddressableLED(2);
        bufferOne = new AddressableLEDBuffer(LED_COUNT_ONE);

        ledOne.setLength(bufferOne.getLength());
        ledOne.setData(bufferOne);
        ledOne.start();
        flashingColor = new FlashingColor();
    }

    @Override
    public void periodic() {
      // lightWriter.set(bufferOne.getLED(2).toString());
      ledOne.setData(bufferOne);
    }
  
    @Override
    public void simulationPeriodic() {
        periodic();
    }
  
    // public void rainbow() {
    //     // For every pixel
    //     for (int i = 0; i < bufferOne.getLength(); i++) {
    //       // Calculate the hue - hue is easier for rainbows because the color
    //       // shape is a circle so only one value needs to precess
    //       final var hue = (rainbow.rainbowFirstPixelHue + (i * 180 / bufferOne.getLength())) % 180;
    //       // Set the value
    //       bufferOne.setHSV(i, hue, 255, 128);
    //     }
    //     // Increase by to make the rainbow "move"
    //     rainbow.rainbowFirstPixelHue += 3;
    //     // Check bounds
    //     rainbow.rainbowFirstPixelHue %= 180;

    //     // for (int i = 0; i < bufferTwo.getLength(); i++) {
    //     //   // Calculate the hue - hue is easier for rainbows because the color
    //     //   // shape is a circle so only one value needs to precess
    //     //   final var hue = (rainbow.rainbowFirstPixelHue + (i * 180 / bufferTwo.getLength())) % 180;
    //     //   // Set the value
    //     //   bufferTwo.setHSV(i, hue, 255, 128);
    //     // }
    //     // // Increase by to make the rainbow "move"
    //     // rainbow.rainbowFirstPixelHue += 3;
    //     // // Check bounds
    //     // rainbow.rainbowFirstPixelHue %= 180;
    // }

    // private void rainbow2() {
    //   List<Color> colors = List.of(
    //                               Color.kBlack,
    //                               Color.kRed,
    //                               Color.kOrangeRed,
    //                               Color.kYellow,
    //                               Color.kGreen,
    //                               Color.kBlue,
    //                               Color.kPurple,
    //                               Color.kBlack,
    //                               new Color(0.15, 0.3, 1.0),
    //                               Color.kDeepPink,
    //                               Color.kWhite,
    //                               Color.kDeepPink,
    //                               new Color(0.15, 0.3, 1.0));
    //   int length = 3;
    //   double duration = 5.0;
      
    //   int offset = (int) (Timer.getFPGATimestamp() % duration / duration * length * colors.size());
    //   for (int i = 0; i < LED_COUNT; i++) {
    //     int colorIndex =
    //         (int) (Math.floor((double) (i - offset) / length) + colors.size()) % colors.size();
    //     colorIndex = colors.size() - 1 - colorIndex;
    //     buffer.setLED(i, colors.get(colorIndex));
    //   }
    // }


  public void setAlternatingColor(Color colorOne, Color colorTwo) {
    for (int i = 0; i < bufferOne.getLength(); i++) {
      if(i%2==0) {bufferOne.setLED(i, colorOne);} 
    }
  }

    public void setAllColor(Color color) {
      lightWriter.set(color.toString());

      for (int i = 0; i < bufferOne.getLength(); i++) {
        bufferOne.setLED(i, color);
      }
    }
    public void setAllColor(int r, int g, int b) {
      for (int i = 0; i < bufferOne.getLength(); i++) {
        setColor(i, r, g, b);
      }
    }

    public void setColor(int i,int r, int g, int b) {
        bufferOne.setRGB(i, r, g, b);
    }
    public void setColor(int i, Color color) {
      bufferOne.setLED(i, color);
    }

    public void switchLeds() {
      double timestamp = Timer.getFPGATimestamp();
      if (timestamp- switchLED.lastChange > 1){
        switchLED.on = !switchLED.on;
        switchLED.lastChange = timestamp;
      }
      if (switchLED.on){
        setAlternatingColor(yellow, blue);
      } else {
        setAlternatingColor(blue, yellow);
      }
    }

    public void flashingColors(Color colorOne, Color colorTwo){
      if (System.currentTimeMillis() - flashingColor.startTime >= flashingColor.waitTime) {
        for (int i = 0; i < bufferOne.getLength(); i++) {
            if (i % 3 == flashingColor.stage) {
                setColor(i, colorOne);
                continue;
            }
            setColor(i, colorTwo);
        }
        flashingColor.stage = flashingColor.stage + 1 > 3 ? 0 : flashingColor.stage + 1;
        flashingColor.startTime = System.currentTimeMillis();
      }
    }
}

