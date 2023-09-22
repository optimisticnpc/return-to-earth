package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

public class GameTimer {

  private static GameTimer instance = null;
  private static int initialTime;

  public static void setInitialTime(int initialTime) {
    GameTimer.initialTime = initialTime;
  }

  public static GameTimer getInstance() {
    if (instance == null) {
      instance = new GameTimer(initialTime);
    }
    return instance;
  }

  private final BooleanProperty timeUp = new SimpleBooleanProperty(false);

  // A Timeline object that is responsible for decreasing timeSeconds every second
  // and updating the time display.
  private Timeline timeline;
  private int timeHundredths;

  // A StringProperty object that is used to display the time in the MM:SS format.
  private StringProperty timeDisplay = new SimpleStringProperty();

  public GameTimer(int initialSeconds) {
    timeHundredths = initialSeconds * 100; // Convert seconds to hundredths of a second.
    updateTimeDisplay();

    timeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(10),
                event -> {
                  timeHundredths--;
                  updateTimeDisplay();
                  if (timeHundredths <= 0) {
                    timeline.stop();
                    timeUp.set(true);
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  public void startTimer() {
    timeHundredths = initialTime * 100; // Convert seconds to hundredths of a second.
    updateTimeDisplay();
    timeUp.set(false); // Reset timeUp property
    timeline.playFromStart();
  }

  public void stopTimer() {
    timeline.stop();
  }

  public StringProperty timeDisplayProperty() {
    return timeDisplay;
  }

  private void updateTimeDisplay() {
    int minutes = timeHundredths / 6000;
    int seconds = (timeHundredths % 6000) / 100;
    // TODO: Decide whether to use hundredths
    timeDisplay.set(String.format("Time remaining: %02d:%02d", minutes, seconds));
  }

  public BooleanProperty timeUpProperty() {
    return timeUp;
  }

  public int getTimeHundredths() {
    return timeHundredths;
  }
}
