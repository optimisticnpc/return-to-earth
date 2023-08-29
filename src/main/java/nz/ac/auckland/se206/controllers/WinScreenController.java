package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class WinScreenController {

  private TextToSpeech textToSpeech = new TextToSpeech();

  @FXML
  public void initialize() throws ApiProxyException {
    new Thread(
            () -> {
              try {
                textToSpeech.speak("Congratulations you win!");
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
}
