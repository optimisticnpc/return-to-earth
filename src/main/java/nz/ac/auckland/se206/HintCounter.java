package nz.ac.auckland.se206;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HintCounter {
  private static HintCounter instance = null;
  private static String hintCount;
  private static String easyHintCount = "Unlimited";
  private static Integer middleHintCount = 5;
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
      hintCount = easyHintCount;
    } else if (GameState.medium) {
      hintCount = middleHintCount.toString();
    } else if (GameState.hard) {
      hintCount = hardHintCount;
    } else {
      hintCount = "Null";
    }
  }

  public String getHintCount() {
    return hintCount;
  }

  public Integer getMiddleHintCount() {
    return middleHintCount;
  }

  public void decrementHintCount() {
    middleHintCount--;
    setHintCount();
  }

  public StringProperty hintCountProperty() {
    return hintDisplay;
  }
}
