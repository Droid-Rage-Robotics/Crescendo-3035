package frc.robot.utility.InfoTracker;

import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.DriverStation;

public class CycleTracker {
    private final String filePath;

    public CycleTracker() {
        this.filePath = generateFileName();
    }

    public static String generateFileName() {
        String eventName = DriverStation.getEventName(); // Example: "Week1Regional"
        String matchType = DriverStation.getMatchType().toString(); // Example: "Qualification"
        int matchNumber = DriverStation.getMatchNumber(); // Example: 5
        String alliance = DriverStation.getAlliance().toString(); // Example: "Red"

        // Create a file-safe string
        return String.format("/home/lvuser/logs/%s_%s_Match%d_%s.txt",
                eventName.replaceAll("[^a-zA-Z0-9]", "_"), // Replace unsafe characters
                matchType,
                matchNumber,
                alliance);
    }

    public void printOut(String string){
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(string + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
