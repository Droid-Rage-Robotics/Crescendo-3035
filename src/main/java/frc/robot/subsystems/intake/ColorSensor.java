package frc.robot.subsystems.intake;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@Deprecated
public class ColorSensor extends SubsystemBase {
    //TODO: Check this
    protected final ColorSensorV3 colorSensor;
    protected final ColorMatch colorMatcher;
    protected final Color orange = new Color(0.143, 0.427, 0.429);//TODO: Fix Color
    protected Color detectedColor;
    protected ColorMatchResult match;

    public ColorSensor() {
        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);//Not Sure What This Is
        colorMatcher = new ColorMatch();
        colorMatcher.addColorMatch(orange);
        detectedColor = colorSensor.getColor(); 
        match = colorMatcher.matchClosestColor(detectedColor);
    }

    @Override
    public void periodic() {
        updateColorSensor();
        
    }

    //Color Sensor Functions
    private void updateColorSensor(){
        colorMatcher.addColorMatch(orange);
        detectedColor = colorSensor.getColor(); 
        match = colorMatcher.matchClosestColor(detectedColor);
    }
    
    public boolean isElementIn(){
        return match.color == orange;
        // return encoderVelocityErrorWriter.get()<-2000;
    }
}

