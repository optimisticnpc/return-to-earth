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

public class PasscodeController {
  private static String correctPassCodeString;

  @FXML private Label timerLabel;
  @FXML private TextField passcodeField;
  @FXML private Label resultLabel;
  @FXML private Button checkButton;

  @FXML
  public void initialize() {
    System.out.println("PasscodeController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    passcodeField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.length() > 4) {
                passcodeField.setText(oldValue);
              }
            });

    passcodeField.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            checkPasscode();
          }
        });
  }

  @FXML
  public void checkPasscode() {
    String enteredPasscode = passcodeField.getText();

    if (correctPassCodeString.equals(enteredPasscode)) {
      resultLabel.setText("Correct! Compartment unlocked.");
      GameState.isPasscodeSolved = true;

      // Disable passcode field and button after correct passcode entered.
      checkButton.setDisable(true);
      passcodeField.setDisable(true);
    } else {
      resultLabel.setText("Incorrect. Try again.");
      // Hide the message after 0.5 seconds
      PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
      pause.setOnFinished(event -> resultLabel.setText(""));
      pause.play();
    }
  }

  @FXML
  public void goBack() {
    System.out.println("go back clicked");

    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
  }

  public static void setCorrectPassCodeString(String correctPassCodeString) {
    PasscodeController.correctPassCodeString = correctPassCodeString;
  }
}
