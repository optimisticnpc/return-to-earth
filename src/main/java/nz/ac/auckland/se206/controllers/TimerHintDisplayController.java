package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * The RoomTwoController class controls the behavior and interactions within Room Two of the game.
 * This room contains various elements and interactions, including speech bubbles, timers, puzzle
 * items, and navigation to other scenes. It manages the state of the room, such as whether puzzle
 * items have been collected or revealed, and handles player interactions.
 */
public class TimerHintDisplayController {

  @FXML private Label timerLabel;
  @FXML private Label hintLabel;
  private Sound sound = Sound.getInstance();
  private TextToSpeech textToSpeech = new TextToSpeech();

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    // Add listeners
    gameTimer
        .belowOneMinuteProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!oldVal && newVal) {
                timerLabel.setStyle("-fx-text-fill: orange;");

                if (sound.isSoundOnProperty().get() && !GameState.isOneMinuteWarningTriggered) {
                  GameState.isOneMinuteWarningTriggered = true;
                  // Text to speech tells the player they are low on oxygen
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

    gameTimer
        .belowThirtySecondsProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!oldVal && newVal) {
                timerLabel.setStyle("-fx-text-fill: red;");
                if (sound.isSoundOnProperty().get()) {
                  // Text to speech tells the player they are low on oxygen
                  new Thread(
                          () -> {
                            try {
                              if (sound.isSoundOnProperty().get()
                                  && !GameState.isThirtySecondWarningTriggered) {
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
