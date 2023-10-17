package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * The TimerHintDisplayController class controls the behavior and interactions within a specific
 * room of the game. This room contains various elements and interactions, including speech bubbles,
 * timers, puzzle items, and navigation to other scenes. It manages the state of the room, such as
 * whether puzzle items have been collected or revealed, and handles player interactions.
 */
public class TimerHintDisplayController {

  @FXML private Label timerLabel;
  @FXML private Label hintLabel;
  private Sound sound = Sound.getInstance();
  private TextToSpeech textToSpeech = new TextToSpeech();

  /** Initializes the room view. This method is called when the room loads. */
  public void initialize() {
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    // Add a listener for when the timer goes below one minute
    gameTimer
        .belowOneMinuteProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!oldVal && newVal) {
                timerLabel.setStyle("-fx-text-fill: orange;"); // Change text to orange

                if (sound.isSoundOnProperty().get()
                    && !GameState
                        .isOneMinuteWarningTriggered) { // Make sure warning only plays once
                  GameState.isOneMinuteWarningTriggered = true;
                  // Use a separate thread for text-to-speech to avoid blocking the UI
                  new Thread(
                          () -> {
                            try {
                              if (sound.isSoundOnProperty().get()) {
                                textToSpeech.speak("One minute remaining");
                              }
                            } catch (Exception e) {
                              e.printStackTrace();
                            }
                          })
                      .start();
                }
              }
            });

    // Add a listener for when the timer goes below thirty seconds
    gameTimer
        .belowThirtySecondsProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!oldVal && newVal) {
                timerLabel.setStyle("-fx-text-fill: red;");
                if (sound.isSoundOnProperty().get()) {
                  // Use a separate thread for text-to-speech to avoid blocking the UI
                  new Thread(
                          () -> {
                            try {
                              if (sound.isSoundOnProperty().get()
                                  && !GameState
                                      .isThirtySecondWarningTriggered) { // Make sure warning only
                                // plays once
                                GameState.isThirtySecondWarningTriggered = true;
                                textToSpeech.speak("Thirty seconds remaining");
                              }
                            } catch (Exception e) {
                              e.printStackTrace();
                            }
                          })
                      .start();
                }
              }
            });

    HintCounter hintCounter = HintCounter.getInstance();
    hintCounter.setHintCount();
    hintLabel.textProperty().bind(hintCounter.hintCountProperty());
  }
}
