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
  private int timeSeconds;

  // A StringProperty object that is used to display the time in the MM:SS format.
  private StringProperty timeDisplay = new SimpleStringProperty();

  public GameTimer(int initialSeconds) {
    timeSeconds = initialSeconds;
    updateTimeDisplay();

    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  timeSeconds--;
                  updateTimeDisplay();
                  if (timeSeconds <= 0) {
                    timeline.stop();
                    timeUp.set(true);
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  public void startTimer() {
    timeSeconds = initialTime;
    updateTimeDisplay();
    timeline.playFromStart();
  }

  public void stopTimer() {
    timeline.stop();
  }

  public StringProperty timeDisplayProperty() {
    return timeDisplay;
  }

  private void updateTimeDisplay() {
    int minutes = timeSeconds / 60;
    int seconds = timeSeconds % 60;
    timeDisplay.set(String.format("%02d:%02d", minutes, seconds));
  }

  public BooleanProperty timeUpProperty() {
    return timeUp;
  }
}
