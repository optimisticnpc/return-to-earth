package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class WinScreenController {

  @FXML private Label winTimeLabel;

  private TextToSpeech textToSpeech = new TextToSpeech();

  @FXML
  public void initialize() throws ApiProxyException {
    // Text to speech tells the player they have won
    new Thread(
            () -> {
              try {
                textToSpeech.speak("Congratulations you win!");
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();

    // Capture the time it took for the player to win
    int hundredthsRemaining = GameTimer.getInstance().getTimeHundredths();
    displayWinTime(hundredthsRemaining);
  }

  public void displayWinTime(int hundredthsRemaining) {
    // Subtract the time started with the time remaining to get the time used
    int timeHundredths = StartController.getTimeSettingSeconds() * 100 - hundredthsRemaining;

    int minutes = timeHundredths / 6000;
    int seconds = (timeHundredths % 6000) / 100;
    int hundredths = timeHundredths % 100;
    winTimeLabel.setText(String.format("Time: %d:%02d.%02d", minutes, seconds, hundredths));
  }

  @FXML
  private void onExitButtonClicked(ActionEvent event) {
    Platform.exit();
    System.exit(0);
  }

  @FXML
  private void onClickRestartButton(ActionEvent event) throws IOException {
    GameState.resetGame();
  }
}
