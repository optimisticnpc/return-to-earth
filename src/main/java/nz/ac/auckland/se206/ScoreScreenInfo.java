package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The ScoreScreenInfo class is responsible for managing and storing game completion times for
 * different difficulty levels in the application. It provides methods to save and load these times
 * to/from a file and format time durations.
 */
public class ScoreScreenInfo {

  /** The file path to store game completion times. */
  private static final String FILE_PATH = "times.txt";

  /** The fastest completion time for the Easy difficulty level in hundredths of a second. */
  public static int fastestEasyTimeHundredths = Integer.MAX_VALUE;

  /** The fastest completion time for the Medium difficulty level in hundredths of a second. */
  public static int fastestMediumTimeHundredths = Integer.MAX_VALUE;

  /** The fastest completion time for the Hard difficulty level in hundredths of a second. */
  public static int fastestHardTimeHundredths = Integer.MAX_VALUE;

  /** Saves the fastest completion times for different difficulty levels to a file. */
  public static void saveTimesToFile() {
    try (FileWriter writer = new FileWriter(FILE_PATH)) {
      writer.write(Integer.toString(fastestEasyTimeHundredths) + "\n");
      writer.write(Integer.toString(fastestMediumTimeHundredths) + "\n");
      writer.write(Integer.toString(fastestHardTimeHundredths) + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the fastest completion times for different difficulty levels from a file. If the file
   * does not exist or is empty, default values are used.
   */
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

  /**
   * Formats a time duration in hundredths of a second into a string representation.
   *
   * @param hundredths The time duration in hundredths of a second.
   * @return A formatted string representing the time duration.
   */
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
