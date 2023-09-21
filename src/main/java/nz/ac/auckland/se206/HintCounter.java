package nz.ac.auckland.se206;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HintCounter {
  private static HintCounter instance = null;
  private static String easyHintCount = "Unlimited";
  private static Integer mediumHintCount = 5;
  private static String hardHintCount = "None";

  public static HintCounter getInstance() {
    if (instance == null) {
      instance = new HintCounter();
    }
    return instance;
  }

  public static void resetHintCount() {
    mediumHintCount = 5;
  }

  private StringProperty hintDisplay = new SimpleStringProperty();

  public HintCounter() {
    setHintCount();
  }

  public void setHintCount() {
    // Set the hint counter display
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

  public Integer decrementHintCount(Integer count) {
    if (mediumHintCount - count > 0) {
      mediumHintCount = mediumHintCount - count;
      setHintCount();
      return 1;
    } else {
      return -1;
    }
  }

  public StringProperty hintCountProperty() {
    return hintDisplay;
  }
}
