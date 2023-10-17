package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class BackgroundController {

  @FXML private Button continueButton;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

    System.out.println("Background.initialize()");
  }

  /** Handles the action event when the continue button is clicked. */
  @FXML
  private void onContinue() {
    Parent roomRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomRoot);
  }
}
