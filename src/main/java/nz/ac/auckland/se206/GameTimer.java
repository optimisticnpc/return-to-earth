package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

/**
 * The GameTimer class generates a timer that allows starting, stopping, and displaying time. The
 * class is implemented as a singleton to ensure a single instance throughout the application.
 */
public class GameTimer {

  /** Singleton instance of the `GameTimer` class. */
  private static GameTimer instance = null;

  /** The initial time in seconds when the timer is created. */
  private static int initialTime;

  /**
   * Sets the initial time for the timer in seconds.
   *
   * @param initialTime The initial time in seconds.
   */
  public static void setInitialTime(int initialTime) {
    GameTimer.initialTime = initialTime;
  }

  /**
   * Retrieves the singleton instance of the `GameTimer` class.
   *
   * @return The singleton instance of the `GameTimer` class.
   */
  public static GameTimer getInstance() {
    if (instance == null) {
      instance = new GameTimer(initialTime);
    }
    return instance;
  }

  /** A BooleanProperty representing whether the timer has reached its end. */
  private final BooleanProperty timeUp = new SimpleBooleanProperty(false);

  // A Timeline object that is responsible for decreasing timeSeconds every second
  // and updating the time display.
  private Timeline timeline;
  private int timeHundredths;

  // A StringProperty object that is used to display the time in the MM:SS format.
  private StringProperty timeDisplay = new SimpleStringProperty();

  /**
   * Constructs a new `GameTimer` instance with an initial time in seconds.
   *
   * @param initialSeconds The initial time in seconds.
   */
  public GameTimer(int initialSeconds) {
    timeHundredths = initialSeconds * 100; // Convert seconds to hundredths of a second.
    updateTimeDisplay();

    // Generates the timeline that acts as the timer. Sets timeUp to true when timeline reaches 0.
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

  /** Starts the timer, resetting it to the initial time. */
  public void startTimer() {
    timeHundredths = initialTime * 100; // Convert seconds to hundredths of a second.
    updateTimeDisplay();
    timeUp.set(false); // Reset timeUp property
    timeline.playFromStart();
  }

  /** This method stops the timer to prevent any later bugs occuring. */
  public void stopTimer() {
    timeline.stop();
  }

  /**
   * Retrieves the StringProperty for the time display.
   *
   * @return The StringProperty for the time display.
   */
  public StringProperty timeDisplayProperty() {
    return timeDisplay;
  }

  /** Updates the time display to show the remaining time in the format "Time remaining: MM:SS". */
  private void updateTimeDisplay() {
    int minutes = timeHundredths / 6000;
    int seconds = (timeHundredths % 6000) / 100;
    // TODO: Decide whether to use hundredths
    timeDisplay.set(String.format("Time remaining: %02d:%02d", minutes, seconds));
  }

  /**
   * Retrieves the BooleanProperty indicating whether the timer has reached its end.
   *
   * @return The BooleanProperty for the time-up state.
   */
  public BooleanProperty timeUpProperty() {
    return timeUp;
  }

  /**
   * Retrieves the current time in hundredths of a second.
   *
   * @return The current time in hundredths of a second.
   */
  public int getTimeHundredths() {
    return timeHundredths;
  }
}
