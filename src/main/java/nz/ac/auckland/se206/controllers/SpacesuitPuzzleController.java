package nz.ac.auckland.se206.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class SpacesuitPuzzleController {

  private static String correctWordString;

  public static String getCorrectWordString() {
    return correctWordString;
  }

  @FXML private Label timerLabel;
  @FXML private TextField inputField;
  @FXML private Label resultLabel;
  @FXML private Label scrambledWordLabel;
  @FXML private Button submitCodeButton;

  // TODO: Get more words and make sure they are different from the riddle
  private String[] words = {"blackhole", "venus", "comet", "satellite", "mars"};

  public void initialize() {
    System.out.println("SpacesuitPuzzleController.initialize()");

    // Bind timer
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    // Allow pressing enter to submit the code
    inputField.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            submitCode();
          }
        });

    pickAndScrambleWord();
  }

  private void pickAndScrambleWord() {
    // Randomly select a word from the array
    correctWordString = words[(int) (Math.random() * words.length)];

    // Convert the word to a list of characters, shuffle it to get the scrambled version, and then
    // convert it back to a string.
    List<String> letters = Arrays.asList(correctWordString.split(""));
    Collections.shuffle(letters);
    String scrambledWord = String.join("", letters);

    // Set the scrambled word to the label
    scrambledWordLabel.setText(scrambledWord.toUpperCase());
  }

  @FXML
  private void submitCode() {
    String enteredPasscode = inputField.getText();

    if (correctWordString.equals(enteredPasscode)) {
      resultLabel.setText("Correct! Compartment unlocked.");
      GameState.isSpacesuitUnlocked = true;

      // Disable passcode field and button after correct passcode entered.
      submitCodeButton.setDisable(true);
      inputField.setDisable(true);
    } else {
      resultLabel.setText("Incorrect. Try again.");
      // Hide the message after 0.7 seconds
      PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
      pause.setOnFinished(event -> resultLabel.setText(""));
      pause.play();
    }
  }

  @FXML
  private void goBack() {
    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
  }
}
