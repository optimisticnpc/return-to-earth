package nz.ac.auckland.se206.controllers;

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

/**
 * Controller class for the Passcode view. This class handles user interactions and UI updates for
 * entering a passcode.
 */
public class PasscodeController {
  private static String correctPassCodeString;

  /**
   * Sets the correct passcode string to be compared with user input.
   *
   * @param correctPassCodeString The correct passcode to unlock the compartment.
   */
  public static void setCorrectPassCodeString(String correctPassCodeString) {
    PasscodeController.correctPassCodeString = correctPassCodeString;
  }

  /**
   * Gets the correct passcode string for validation.
   *
   * @return The correct passcode string.
   */
  public static String getCorrectPassCodeString() {
    return correctPassCodeString;
  }

  @FXML private Label timerLabel;
  @FXML private TextField passcodeField;
  @FXML private Label resultLabel;
  @FXML private Button checkButton;

  /**
   * Initializes the Passcode view when it loads. This method sets up the timer label, restricts
   * passcode input length, and enables Enter key validation.
   */
  @FXML
  public void initialize() {
    System.out.println("PasscodeController.initialize()");
    // Bind timer
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    // Make sure player cannot type more than four characters
    passcodeField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.length() > 4) {
                passcodeField.setText(oldValue);
              }
            });

    // Allow enter to be used to check passcode
    passcodeField.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            onCheckPasscode();
          }
        });
  }

  /**
   * Handles the click event on the "Check" button. Compares the entered passcode with the correct
   * passcode and provides feedback.
   */
  @FXML
  private void onCheckPasscode() {
    String enteredPasscode = passcodeField.getText();

    if (correctPassCodeString.equals(enteredPasscode)) {
      resultLabel.setText("Correct! Compartment unlocked.");
      GameState.isPasscodeSolved = true;

      // Disable passcode field and button after correct passcode entered.
      checkButton.setDisable(true);
      passcodeField.setDisable(true);
    } else {
      resultLabel.setText("Incorrect. Try again.");
      // Hide the message after 0.7 seconds
      PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
      pause.setOnFinished(event -> resultLabel.setText(""));
      pause.play();
    }
  }

  /**
   * Handles the click event on the "Go Back" button. Navigates back to Room Two when the button is
   * clicked.
   */
  @FXML
  private void onGoBack() {
    System.out.println("go back clicked");

    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
  }
}
