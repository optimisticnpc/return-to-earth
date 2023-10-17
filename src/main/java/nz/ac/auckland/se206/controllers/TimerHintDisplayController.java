package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;

/**
 * The RoomTwoController class controls the behavior and interactions within Room Two of the game.
 * This room contains various elements and interactions, including speech bubbles, timers, puzzle
 * items, and navigation to other scenes. It manages the state of the room, such as whether puzzle
 * items have been collected or revealed, and handles player interactions.
 */
public class TimerHintDisplayController {

  @FXML private Label timerLabel;
  @FXML private Label hintLabel;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // System.out.println("TIMER HINT DISPLAY INITIALIZE");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    HintCounter hintCounter = HintCounter.getInstance();
    hintCounter.setHintCount();
    hintLabel.textProperty().bind(hintCounter.hintCountProperty());
  }
}
