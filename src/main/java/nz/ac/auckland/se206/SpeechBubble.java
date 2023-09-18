package nz.ac.auckland.se206;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SpeechBubble {
  private static SpeechBubble instance = null;
  private static String speechText = "";

  private StringProperty speechDisplay = new SimpleStringProperty();

  public SpeechBubble() {
    speechDisplay.set(speechText);
  }

  public static SpeechBubble getInstance() {
    if (instance == null) {
      instance = new SpeechBubble();
    }
    return instance;
  }

  public StringProperty speechDisplayProperty() {
    return speechDisplay;
  }

  public void setSpeechText(String text) {
    speechText = text;
    speechDisplay.set(speechText);
  }
}
