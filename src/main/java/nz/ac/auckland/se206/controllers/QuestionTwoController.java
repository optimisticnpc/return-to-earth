package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.MathQuestionSelector;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class QuestionTwoController {

  @FXML private Label timerLabel;
  @FXML private Label questionTwoLabel;

  public void initialize() {
    System.out.println("QuestionTwoController.initialize()");

    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    MathQuestionSelector selector = MathQuestionSelector.getInstance();
    questionTwoLabel.setText(selector.getSecondQuestion());
  }

  @FXML
  private void onGoBack() {
    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
  }
}
