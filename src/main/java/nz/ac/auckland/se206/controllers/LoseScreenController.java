package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * Controller class for the Lose Screen view. This class handles user interactions and UI updates
 * for the lose screen, including providing audio feedback.
 */
public class LoseScreenController {

  private TextToSpeech textToSpeech = new TextToSpeech();

  /**
   * Initializes the Lose Screen view when it loads. This method triggers text-to-speech to announce
   * that the player has lost.
   *
   * @throws ApiProxyException If there's an error with the text-to-speech API.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Tell the player they have lost
    new Thread(
            () -> {
              try {
                textToSpeech.speak("You lost!");
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  /**
   * Handles the click event on the "Exit" button. Closes the application when the button is
   * clicked.
   *
   * @param event The action event triggered by clicking the "Exit" button.
   */
  @FXML
  private void onExitButtonClicked(ActionEvent event) {
    Platform.exit();
    System.exit(0);
  }

  /**
   * Handles the click event on the "Restart" button. Restarts the game when the button is clicked.
   *
   * @param event The action event triggered by clicking the "Restart" button.
   * @throws IOException If there's an error restarting the game.
   */
  @FXML
  private void onClickRestartButton(ActionEvent event) throws IOException {
    GameState.resetGameVariables();
    GameState.goToStart();
  }
}
