package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class PasscodeController {
  private static final String CORRECT_PASSCODE = "1234";

  @FXML private TextField passcodeField;
  @FXML private Label resultLabel;

  @FXML
  public void initialize() {
    passcodeField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.length() > 4) {
                passcodeField.setText(oldValue);
              }
            });
  }

  @FXML
  public void checkPasscode() {
    String enteredPasscode = passcodeField.getText();

    if (CORRECT_PASSCODE.equals(enteredPasscode)) {
      resultLabel.setText("Correct!");
    } else {
      resultLabel.setText("Incorrect. Try again.");
    }
  }

  @FXML
  public void goBack() {
    System.out.println("go back clicked");

    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
  }
}
