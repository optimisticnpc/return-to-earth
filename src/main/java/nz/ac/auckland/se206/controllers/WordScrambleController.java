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
import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MyControllers;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/**
 * The WordScrambleController class is responsible for controlling the Word Scramble puzzle screen
 * in the game. It manages the timer, player input for solving the puzzle, and provides feedback on
 * the player's progress in solving the puzzle.
 */
public class WordScrambleController implements MyControllers {

  private static String correctWordString;

  /**
   * Gets the correct word string for the puzzle.
   *
   * @return The correct word string.
   */
  public static String getCorrectWordString() {
    return correctWordString;
  }

  @FXML private TextField inputField;
  @FXML private Label resultLabel;
  @FXML private Label scrambledWordLabel;
  @FXML private Button submitCodeButton;
  @FXML private Button wordScrambleHintButton;

  // TODO: Get more words and make sure they are different from the riddle
  private String[] unscrambleWords = {
    "blackhole",
    "venus",
    "comet",
    "satellite",
    "saturn",
    "saturn",
    "jupiter",
    "uranus",
    "neptune",
    "lightyear",
    "universe",
    "asteroid",
    "meteorite",
    "galaxy",
    "mercury",
    "constellation",
    "orbit",
    "nebula",
    "rocket",
    "supernova",
    "stardust",
    "astronaut",
    "matter",
    "cosmos",
    "pluto"
  };

  /**
   * Initializes the word scramble puzzle screen, setting up the timer, input field, and scrambled
   * word.
   */
  public void initialize() {
    System.out.println("WordScrambleController.initialize()");

    // Allow pressing enter to submit the code
    inputField.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            onSubmitCode();
          }
        });

    // Picks and scrambles words. Hides hint if hard mode
    pickAndScrambleWord();

    if (GameState.hard) {
      wordScrambleHintButton.setVisible(false);
    }
  }

  /** Picks a random word, scrambles it, and sets it as the puzzle word for the player to solve. */
  private void pickAndScrambleWord() {
    // Randomly select a word from the array
    correctWordString = unscrambleWords[(int) (Math.random() * unscrambleWords.length)];

    // Convert the word to a list of characters, shuffle it to get the scrambled version, and then
    // convert it back to a string.
    List<String> letters = Arrays.asList(correctWordString.split(""));
    Collections.shuffle(letters);
    String scrambledWord = String.join("", letters);

    // Set the scrambled word to the label
    scrambledWordLabel.setText(scrambledWord.toUpperCase());
  }

  /**
   * Handles the action when the player submits a word to unlock the compartment. Checks if the
   * entered word matches the correct word and provides feedback.
   */
  @FXML
  private void onSubmitCode() {
    String enteredPasscode = inputField.getText();

    // Check if they guessed correct word
    // Ignore capitalisation and whitespace
    if (correctWordString.equals(enteredPasscode.toLowerCase().replaceAll("\\s+", ""))) {
      resultLabel.setText("Correct! Compartment unlocked.");
      GameState.isWireCompartmentUnlocked = true;

      // Disable passcode field and button after correct passcode entered.
      submitCodeButton.setDisable(true);
      wordScrambleHintButton.setDisable(true);
      inputField.setDisable(true);
    } else {
      resultLabel.setText("Incorrect. Try again.");
      // Hide the message after 0.7 seconds
      PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
      pause.setOnFinished(event -> resultLabel.setText(""));
      pause.play();
    }
  }

  /**
   * Handles the action when the player goes back from the word scramble puzzle screen to the
   * previous room.
   */
  @FXML
  private void onGoBack() {
    Parent root = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(root);
  }

  /**
   * Handles the click event on the "Hint" button. Sends a message to the chat asking for a hint for
   * the word scramble question.
   */
  @FXML
  private void onClickWordScrambleHintButton() {
    if (GptPromptEngineering.checkIfAuthorisedAndPrintMessage()) {
      return;
    }
    // Add asking for hint message to chat
    ChatMessage msg =
        new ChatMessage("user", "Please give me a hint for the word scramble question");

    ChatCentralControl.getInstance().addMessage(msg);
    ChatCentralControl.getInstance().runGpt(msg);
  }

  /** Method that disables the hint button so that the player cannot click it. */
  @Override
  public void disableHintButton() {
    wordScrambleHintButton.setDisable(true);
  }
}
