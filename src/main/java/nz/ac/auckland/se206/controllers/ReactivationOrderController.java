package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.ButtonOrder;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ReactivationOrderController {
  @FXML private Label timerLabel;
  @FXML private Label orderLabel;

  public void initialize() {
    System.out.println("ReactivationOrderController.initialize()");

    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    ButtonOrder buttonOrder = ButtonOrder.getInstance();
    orderLabel.setText(buttonOrder.getCorrectOrderString());
  }

  @FXML
  public void goBack() {
    Parent roomOneRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomOneRoot);
  }
}
