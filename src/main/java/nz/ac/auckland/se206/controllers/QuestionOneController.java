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
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/**
 * Controller class for the Question One view. This class handles user interactions and UI updates
 * for the Question One view.
 */
public class QuestionOneController {

  @FXML private Label timerLabel;
  @FXML private Label questionOneLabel;

  /**
   * Initializes the Question One view when it loads. This method sets up the timer label and
   * displays the first math question.
   */
  public void initialize() {
    System.out.println("QuestionOneController.initialize()");

    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    MathQuestionSelector selector = MathQuestionSelector.getInstance();
    questionOneLabel.setText(selector.getFirstQuestion());
  }

  /**
   * Handles the click event on the "Go Back" button. Navigates back to Room Two when the button is
   * clicked.
   */
  @FXML
  private void onClickQuestionOneHintButton() {

    ChatCentralControl.getInstance()
        .runGpt((new ChatMessage("system", GptPromptEngineering.hintMathQuestionPrompt())));
    System.out.println();
  }

  @FXML
  private void onGoBack() {
    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
  }
}
