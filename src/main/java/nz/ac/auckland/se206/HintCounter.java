package nz.ac.auckland.se206;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The HintCounter class manages hint counts for different difficultys. It provides methods for
 * retrieving and updating hint counts based on the current game difficulty. The class is
 * implemented as a singleton to ensure a single instance throughout the application.
 */
public class HintCounter {
  /** Singleton instance of the `HintCounter` class. */
  private static HintCounter instance = null;

  /** The hint count for easy difficulty (unlimited hints). */
  private static String easyHintCount = "Unlimited";

  /** The hint count for medium difficulty. */
  private static Integer mediumHintCount = 5;

  /** The hint count for hard difficulty (no hints available). */
  private static String hardHintCount = "None";

  /**
   * Retrieves the singleton instance of the `HintCounter` class.
   *
   * @return The singleton instance of the `HintCounter` class.
   */
  public static HintCounter getInstance() {
    if (instance == null) {
      instance = new HintCounter();
    }
    return instance;
  }

  /** Resets the hint count to its initial value for the medium difficulty level. */
  public static void resetHintCount() {
    mediumHintCount = 5;
  }

  /** A StringProperty object used to display the current hint count. */
  private StringProperty hintDisplay = new SimpleStringProperty();

  /**
   * Constructs a new `HintCounter` instance and sets the initial hint count based on the current
   * game difficulty level.
   */
  public HintCounter() {
    setHintCount();
  }

  /**
   * Sets the hint count based on the current game difficulty level and updates the hint counter
   * display.
   */
  public void setHintCount() {
    // Set the hint counter display
    if (GameState.easy) {
      hintDisplay.set("Hints Remaining: " + easyHintCount);
    } else if (GameState.medium) {
      hintDisplay.set("Hints Remaining: " + mediumHintCount.toString());
    } else if (GameState.hard) {
      hintDisplay.set("Hints Remaining: " + hardHintCount);
    } else {
      hintDisplay.set("Null");
    }
  }

  /**
   * Retrieves the hint count for the medium difficulty level.
   *
   * @return The hint count for medium difficulty.
   */
  public Integer getMediumHintCount() {
    return mediumHintCount;
  }

  /**
   * Decrements the hint count for the medium difficulty level by a specified count and updates the
   * hint counter display.
   *
   * @param count The number of hints to decrement.
   */
  public void decrementHintCount(Integer count) {
    mediumHintCount = mediumHintCount - count;
    setHintCount();
  }

  /**
   * Retrieves the StringProperty for the hint count display.
   *
   * @return The StringProperty for the hint count display.
   */
  public StringProperty hintCountProperty() {
    return hintDisplay;
  }
}
