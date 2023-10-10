package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.ScoreScreenInfo;
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
    int timeHundredths = calculateTimeUsed(hundredthsRemaining);
    checkWinTime(timeHundredths);
    displayWinTime(timeHundredths);
  }

  private int calculateTimeUsed(int hundredthsRemaining) {
    // Subtract the time started with the time remaining to get the time used
    return StartController.getTimeSettingSeconds() * 100 - hundredthsRemaining;
  }

  private void checkWinTime(int hundredthsRemaining) {
    if (GameState.easy) {
      if (hundredthsRemaining < ScoreScreenInfo.fastestEasyTimeHundredths) {
        ScoreScreenInfo.fastestEasyTimeHundredths = hundredthsRemaining;
      }
    } else if (GameState.medium) {
      if (hundredthsRemaining < ScoreScreenInfo.fastestMediumTimeHundredths) {
        ScoreScreenInfo.fastestMediumTimeHundredths = hundredthsRemaining;
      }
    } else {
      if (hundredthsRemaining < ScoreScreenInfo.fastestHardTimeHundredths) {
        ScoreScreenInfo.fastestHardTimeHundredths = hundredthsRemaining;
      }
    }
    ScoreScreenInfo.saveTimesToFile();
    ScoreScreenController.updateFastestTimes();
  }

  public void displayWinTime(int timeHundredths) {
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
    GameState.resetGameVariables();
    GameState.goToStart();
  }
}
