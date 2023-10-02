package nz.ac.auckland.se206;

public class ScoreScreenInfo {
  public static int fastestEasyTimeHundredths = Integer.MAX_VALUE;
  public static int fastestMediumTimeHundredths = Integer.MAX_VALUE;
  public static int fastestHardTimeHundredths = Integer.MAX_VALUE;

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
