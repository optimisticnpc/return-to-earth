package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.MathQuestionSelector;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;

/**
 * Controller class for the Question Two view. This class handles user interactions and UI updates
 * for the Question Two view.
 */
public class QuestionTwoController {

  @FXML private Label timerLabel;
  @FXML private Label questionTwoLabel;

  /**
   * Initializes the Question Two view when it loads. This method sets up the timer label and
   * displays the second math question.
   */
  public void initialize() {
    System.out.println("QuestionTwoController.initialize()");

    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    MathQuestionSelector selector = MathQuestionSelector.getInstance();
    questionTwoLabel.setText(selector.getSecondQuestion());
  }

  @FXML
  private void onClickQuestionTwoHintButton() {
    ChatMessage msg = new ChatMessage("user", "Please give me a hint for the second question");

    // Add asking for hint message to chat
    ChatCentralControl.getInstance().addMessage(msg);
    ChatCentralControl.getInstance().runGpt(msg);
  }

  /**
   * Handles the click event on the "Go Back" button. Navigates back to Room Two when the button is
   * clicked.
   */
  @FXML
  private void onGoBack() {
    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
  }
}
