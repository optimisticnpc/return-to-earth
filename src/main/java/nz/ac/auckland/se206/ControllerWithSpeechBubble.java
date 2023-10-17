/**
 * The {@code ControllerWithSpeechBubble} interface represents a controller that can display speech
 * bubbles with text messages.
 */
package nz.ac.auckland.se206;

public interface ControllerWithSpeechBubble {

  /**
   * Activates a speech bubble with the specified text message.
   *
   * @param message The text message to be displayed in the speech bubble.
   */
  void activateSpeech(String message);
}
