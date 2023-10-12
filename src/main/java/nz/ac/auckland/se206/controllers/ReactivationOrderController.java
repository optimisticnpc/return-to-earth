package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ButtonOrder;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * Controller class for the Reactivation Order view. This class handles user interactions and UI
 * updates for the Reactivation Order view.
 */
public class ReactivationOrderController {
  @FXML private Label timerLabel;
  @FXML private Label orderLabel;

  /**
   * Initializes the Reactivation Order view when it loads. This method sets up the timer label and
   * displays the correct order label.
   */
  public void initialize() {
    System.out.println("ReactivationOrderController.initialize()");

    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    ButtonOrder buttonOrder = ButtonOrder.getInstance();
    orderLabel.setText(buttonOrder.getCorrectOrderString());
  }

  /**
   * Handles the click event on the "Go Back" button. If the spaceship part is fixed, navigates to
   * the final room; otherwise, goes back to Room One.
   */
  @FXML
  private void onGoBack() {
    if (GameState.isPartFixed) {
      // If part is fixed, go to room with reactivate button
      Parent roomOneRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE_FINAL);
      App.getScene().setRoot(roomOneRoot);
    } else {
      Parent roomOneRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
      App.getScene().setRoot(roomOneRoot);
    }
  }
}
