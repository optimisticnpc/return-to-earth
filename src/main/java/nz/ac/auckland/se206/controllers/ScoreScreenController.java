package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ScoreScreenController {

  @FXML
  private void onGoBack() {
    Parent startParent = SceneManager.getUiRoot(AppUi.START);
    App.getScene().setRoot(startParent);
  }
}
