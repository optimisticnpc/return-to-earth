package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class LoseScreenController {

  private TextToSpeech textToSpeech = new TextToSpeech();

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
