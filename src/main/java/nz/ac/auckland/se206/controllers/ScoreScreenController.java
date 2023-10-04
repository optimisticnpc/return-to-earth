package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ScoreScreenInfo;

public class ScoreScreenController {
  @FXML private Label fastestEasyTimeLabel;
  @FXML private Label fastestMediumTimeLabel;
  @FXML private Label fastestHardTimeLabel;

  // Add to ScoreScreenController
  private static ScoreScreenController instance;

  public static void updateFastestTimes() {
    if (instance != null) {
      instance.refreshTimes();
    }
  }

  public ScoreScreenController() {
    instance = this;
  }

  @FXML
  public void initialize() {
    refreshTimes();
  }

  public void refreshTimes() {
    ScoreScreenInfo.loadTimesFromFile();
    fastestEasyTimeLabel.setText(
        ScoreScreenInfo.formatTime(ScoreScreenInfo.fastestEasyTimeHundredths));
    fastestMediumTimeLabel.setText(
        ScoreScreenInfo.formatTime(ScoreScreenInfo.fastestMediumTimeHundredths));
    fastestHardTimeLabel.setText(
        ScoreScreenInfo.formatTime(ScoreScreenInfo.fastestHardTimeHundredths));
  }

  @FXML
  private void onGoBack() {
    Parent startParent = SceneManager.getUiRoot(AppUi.START);
    App.getScene().setRoot(startParent);
  }
}
