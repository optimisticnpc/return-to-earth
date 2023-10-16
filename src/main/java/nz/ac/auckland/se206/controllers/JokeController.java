package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class JokeController {

  @FXML private Label timerLabel;

  /** Initializes the word joke screen, setting up the timer, input field. */
  public void initialize() {
    System.out.println("JokeController.initialize()");

    // Bind timer
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
  }

  /**
   * Handles the action when the player goes back from the word scramble puzzle screen to the
   * previous room.
   */
  @FXML
  private void onGoBack() {
    Parent root = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(root);
  }
}
