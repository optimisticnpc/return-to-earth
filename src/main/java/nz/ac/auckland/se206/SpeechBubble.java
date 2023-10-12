package nz.ac.auckland.se206;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The SpeechBubble class manages the speech bubbles and speech labels for the application. The
 * class is implemented as a singleton to ensure a single instance throughout the application.
 */
public class SpeechBubble {

  /** Singleton instance of the Sound class. */
  private static SpeechBubble instance = null;

  /** The current speech text to be displayed. */
  private static String speechText = "";

  /**
   * Retrieves the singleton instance of the SpeechBubble class.
   *
   * @return The singleton instance of the SpeechBubble class.
   */
  public static SpeechBubble getInstance() {
    if (instance == null) {
      instance = new SpeechBubble();
    }
    return instance;
  }

  /** String Property representing the speech text to be displayed. */
  private StringProperty speechDisplay = new SimpleStringProperty();

  /** Constructs a new SpeechBubble instance and initializes it with the current speech text. */
  public SpeechBubble() {
    speechDisplay.set(speechText);
  }

  /**
   * Gets the String Property for the SpeechBubble.
   *
   * @return The String Property for the SpeechBubble.
   */
  public StringProperty speechDisplayProperty() {
    return speechDisplay;
  }

  /** Sets the speech text and display for the speech bubble. */
  public void setSpeechText(String text) {
    speechText = text;
    speechDisplay.set(speechText);
  }
}
