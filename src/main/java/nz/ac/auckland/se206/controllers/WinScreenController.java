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

/**
 * The WinScreenController class controls the behavior and interactions of the win screen. It
 * displays the player's winning time, congratulates the player, and handles button actions to exit
 * the application or restart the game.
 */
public class WinScreenController {

  @FXML private Label winTimeLabel;

  private TextToSpeech textToSpeech = new TextToSpeech();

  /**
   * Initializes the win screen, including playing a congratulatory message and displaying the
   * player's winning time.
   *
   * @throws ApiProxyException If there is an issue with the TextToSpeech API.
   */
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

  /**
   * Calculates the time used by subtracting the remaining time from the initial time.
   *
   * @param hundredthsRemaining The remaining time in hundredths of a second.
   * @return The time used in hundredths of a second.
   */
  private int calculateTimeUsed(int hundredthsRemaining) {
    // Subtract the time started with the time remaining to get the time used
    return StartController.getTimeSettingSeconds() * 100 - hundredthsRemaining;
  }

  /**
   * Checks and updates the player's fastest winning time based on the game difficulty.
   *
   * @param hundredthsRemaining The remaining time in hundredths of a second.
   */
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

  /**
   * Displays the player's winning time in minutes, seconds, and hundredths of a second.
   *
   * @param timeHundredths The winning time in hundredths of a second.
   */
  public void displayWinTime(int timeHundredths) {
    int minutes = timeHundredths / 6000;
    int seconds = (timeHundredths % 6000) / 100;
    int hundredths = timeHundredths % 100;
    winTimeLabel.setText(String.format("Time: %d:%02d.%02d", minutes, seconds, hundredths));
  }

  /**
   * Handles the action when the exit button is clicked, exiting the application.
   *
   * @param event The action event.
   */
  @FXML
  private void onExitButtonClicked(ActionEvent event) {
    Platform.exit();
    System.exit(0);
  }

  /**
   * Handles the action when the restart button is clicked, restarting the game.
   *
   * @param event The action event.
   * @throws IOException If there is an issue with restarting the game.
   */
  @FXML
  private void onClickRestartButton(ActionEvent event) throws IOException {
    GameState.resetGame();
  }
}
