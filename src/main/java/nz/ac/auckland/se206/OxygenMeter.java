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

/**
 * The`OxygenMeter class manages the oxygen level and its display in the game. It follows the
 * singleton design pattern to ensure a single instance throughout the application.
 */
public class OxygenMeter {

  /** The singleton instance of the `OxygenMeter` class. */
  private static OxygenMeter instance = null;

  /**
   * Gets the singleton instance of the `OxygenMeter` class.
   *
   * @return The singleton instance of the `OxygenMeter` class.
   */
  public static OxygenMeter getInstance() {
    if (instance == null) {
      instance = new OxygenMeter();
    }
    return instance;
  }

  /** A timeline for managing the oxygen level and updating its display. */
  private Timeline timeline;

  /** The current scene in the game. */
  private CurrentScene currentScene = CurrentScene.getInstance();

  /** A property representing the oxygen progress as a double value. */
  private DoubleProperty oxygenProgressProperty = new SimpleDoubleProperty();

  /** A property representing the oxygen progress as a percentage string. */
  private StringProperty percentProgressProperty = new SimpleStringProperty();

  /** A BigDecimal object representing the oxygen progress. */
  private BigDecimal progress = new BigDecimal(String.format("%.2f", 1.0));

  private RoomThreeController roomThree;

  /**
   * Constructs a new OxygenMeter object and initializes the timeline for managing oxygen levels.
   */
  public OxygenMeter() {
    // Generates a timeline that the oxygenmeter uses to decrement per second.
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  // Only decrements when user is in the third room.
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
                        // Sends player to lose screen when oxygen runs out.
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
                    // Resets and changes oxygen meter when user collects spacesuit.
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

  /**
   * Sets the controller for Room Three for the oxygen meter to use.
   *
   * @param controller The RoomThreeController instance to set as the controller for Room Three.
   */
  public void setRoomThreeController(RoomThreeController controller) {
    this.roomThree = controller;
  }

  /** Starts the oxygen meter timer. */
  public void startTimer() {
    progress = new BigDecimal(String.format("%.2f", 1.0));
    oxygenProgressProperty.set(progress.doubleValue());
    percentProgressProperty.set("100%");
    timeline.play();
  }

  /**
   * Gets the property representing the oxygen progress as a double value.
   *
   * @return The oxygen progress property.
   */
  public DoubleProperty oxygenProgressProperty() {
    return oxygenProgressProperty;
  }

  /**
   * Gets the property representing the oxygen progress as a percentage string.
   *
   * @return The percentage progress property.
   */
  public StringProperty percentProgressProperty() {
    return percentProgressProperty;
  }
}
