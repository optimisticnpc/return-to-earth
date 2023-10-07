package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreScreenInfo {

  private static final String FILE_PATH = "times.txt";

  public static int fastestEasyTimeHundredths = Integer.MAX_VALUE;
  public static int fastestMediumTimeHundredths = Integer.MAX_VALUE;
  public static int fastestHardTimeHundredths = Integer.MAX_VALUE;

  public static void saveTimesToFile() {
    try (FileWriter writer = new FileWriter(FILE_PATH)) {
      writer.write(Integer.toString(fastestEasyTimeHundredths) + "\n");
      writer.write(Integer.toString(fastestMediumTimeHundredths) + "\n");
      writer.write(Integer.toString(fastestHardTimeHundredths) + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void loadTimesFromFile() {
    File file = new File(FILE_PATH);
    if (!file.exists() || file.length() == 0) {
      // File does not exist or is empty; use default values
      fastestEasyTimeHundredths = Integer.MAX_VALUE;
      fastestMediumTimeHundredths = Integer.MAX_VALUE;
      fastestHardTimeHundredths = Integer.MAX_VALUE;
      return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
      fastestEasyTimeHundredths = Integer.parseInt(reader.readLine());
      fastestMediumTimeHundredths = Integer.parseInt(reader.readLine());
      fastestHardTimeHundredths = Integer.parseInt(reader.readLine());
    } catch (IOException e) {
      // Could not read file; use default values
      e.printStackTrace();
      fastestEasyTimeHundredths = Integer.MAX_VALUE;
      fastestMediumTimeHundredths = Integer.MAX_VALUE;
      fastestHardTimeHundredths = Integer.MAX_VALUE;
    }
  }

  public static String formatTime(int hundredths) {
    if (hundredths == Integer.MAX_VALUE) {
      return "--:--";
    }
    int minutes = hundredths / 6000;
    int seconds = (hundredths % 6000) / 100;
    hundredths = hundredths % 100;
    return String.format("%d:%02d.%02d", minutes, seconds, hundredths);
  }
}
