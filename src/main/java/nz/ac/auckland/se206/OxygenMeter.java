package nz.ac.auckland.se206;

import java.io.IOException;
import java.math.BigDecimal;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.RoomThreeController;

public class OxygenMeter {
  private static OxygenMeter instance = null;

  public static OxygenMeter getInstance() {
    if (instance == null) {
      instance = new OxygenMeter();
    }
    return instance;
  }

  private Timeline timeline;
  private CurrentScene currentScene = CurrentScene.getInstance();
  private DoubleProperty oxygenProgressProperty = new SimpleDoubleProperty();
  private StringProperty percentProgressProperty = new SimpleStringProperty();
  private BigDecimal progress = new BigDecimal(String.format("%.2f", 1.0));
  private RoomThreeController roomThree;

  public void setRoomThreeController(RoomThreeController controller) {
    this.roomThree = controller;
  }

  public OxygenMeter() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (currentScene.getCurrent() == 3) {
                    if (progress.doubleValue() > 0) {
                      if (GameState.isSpacesuitCollected) {
                        // Decrement oxygen level slower if spacesuit is collected
                        progress =
                            new BigDecimal(String.format("%.2f", progress.doubleValue() - 0.02));
                      } else {
                        progress =
                            new BigDecimal(String.format("%.2f", progress.doubleValue() - 0.05));
                      }
                      if (progress.doubleValue() < 0.3) {
                        roomThree.showLowOxygen();
                      }
                      // Display oxygen level as percentage
                      oxygenProgressProperty.set(progress.doubleValue());
                      Integer percentage = (int) Math.round(progress.doubleValue() * 100);
                      percentProgressProperty.set(Integer.toString(percentage) + "%");
                    } else {
                      try {
                        System.out.println(
                            "oxygen progress when death occured: " + progress.doubleValue());
                        timeline.stop();
                        currentScene.setCurrent(4);
                        App.setRoot("losescreen");
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }
                  } else {
                    if (GameState.isSpacesuitJustCollected) {
                      progress = new BigDecimal(String.format("%.2f", 1.0));
                      oxygenProgressProperty.set(progress.doubleValue());
                      percentProgressProperty.set("100%");
                      GameState.isSpacesuitJustCollected = false;
                      roomThree.showSpacesuitOxygen();
                    }
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  public void startTimer() {
    progress = new BigDecimal(String.format("%.2f", 1.0));
    oxygenProgressProperty.set(progress.doubleValue());
    percentProgressProperty.set("100%");
    timeline.play();
  }

  public DoubleProperty oxygenProgressProperty() {
    return oxygenProgressProperty;
  }

  public StringProperty percentProgressProperty() {
    return percentProgressProperty;
  }
}
