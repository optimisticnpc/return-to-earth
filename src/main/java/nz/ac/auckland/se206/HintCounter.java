package nz.ac.auckland.se206;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HintCounter {
  private static HintCounter instance = null;
  private static String easyHintCount = "Unlimited";
  private static Integer mediumHintCount = 5;
  private static String hardHintCount = "None";

  private StringProperty hintDisplay = new SimpleStringProperty();

  public HintCounter() {
    setHintCount();
  }

  public static HintCounter getInstance() {
    if (instance == null) {
      instance = new HintCounter();
    }
    return instance;
  }

  public void setHintCount() {
    if (GameState.easy) {
      hintDisplay.set(easyHintCount);
    } else if (GameState.medium) {
      hintDisplay.set(mediumHintCount.toString());
    } else if (GameState.hard) {
      hintDisplay.set(hardHintCount);
    } else {
      hintDisplay.set("Null");
    }
  }

  public Integer getMediumHintCount() {
    return mediumHintCount;
  }

  public void decrementHintCount() {
    mediumHintCount--;
    setHintCount();
  }

  public StringProperty hintCountProperty() {
    return hintDisplay;
  }
}
