package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class StartController {
  @FXML private Label timerLabel;
  @FXML private Label displayLabel;
  @FXML private Button nextAndStartButton;

  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("StartController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
    gameTimer.startTimer();
  }

  @FXML
  private void onNextAndStartButtonClicked(ActionEvent event)
      throws ApiProxyException, IOException {

    // Change the scene to the room view
    Parent roomRoot = SceneManager.getUiRoot(AppUi.ROOM);
    App.getScene().setRoot(roomRoot);
  }
}
