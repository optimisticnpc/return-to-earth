package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ScoreScreenInfo;

public class ScoreScreenController {
  @FXML private Label fastestEasyTimeLabel;
  @FXML private Label fastestMediumTimeLabel;
  @FXML private Label fastestHardTimeLabel;
  @FXML private ImageView easyStars;
  @FXML private ImageView mediumStars;
  @FXML private ImageView hardStars;
  @FXML private Pane helpScreen;
  @FXML private Button helpStarsButton;
  @FXML private Button goBackButton;

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
    helpScreen.setVisible(false);
  }

  public void refreshTimes() {
    ScoreScreenInfo.loadTimesFromFile();

    // Update times
    fastestEasyTimeLabel.setText(
        ScoreScreenInfo.formatTime(ScoreScreenInfo.fastestEasyTimeHundredths));
    fastestMediumTimeLabel.setText(
        ScoreScreenInfo.formatTime(ScoreScreenInfo.fastestMediumTimeHundredths));
    fastestHardTimeLabel.setText(
        ScoreScreenInfo.formatTime(ScoreScreenInfo.fastestHardTimeHundredths));

    // Update star ratings
    updateStarRating(easyStars, ScoreScreenInfo.fastestEasyTimeHundredths);
    updateStarRating(mediumStars, ScoreScreenInfo.fastestMediumTimeHundredths);
    updateStarRating(hardStars, ScoreScreenInfo.fastestHardTimeHundredths);
  }

  private void updateStarRating(ImageView imageView, int hundredths) {
    String imagePath;

    if (hundredths <= 12000) { // Under 2 minutes
      imagePath = "src/main/resources/images/stars_yellow_3.png";
    } else if (hundredths <= 24000) { // Under 4 minutes
      imagePath = "src/main/resources/images/stars_yellow_2.png";
    } else if (hundredths <= 36000) { // Under 6 minutes
      imagePath = "src/main/resources/images/stars_yellow_1.png";
    } else {
      imagePath = "src/main/resources/images/stars_grey.png"; // Default grey stars
    }

    try (InputStream stream = new FileInputStream(imagePath)) {
      imageView.setImage(new Image(stream));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onGoBack() {
    Parent startParent = SceneManager.getUiRoot(AppUi.START);
    App.getScene().setRoot(startParent);
  }

  @FXML
  private void onClickHelpStarsButton() {
    helpScreen.setVisible(true);
    goBackButton.setVisible(false);
    helpStarsButton.setVisible(false);
  }

  /**
   * Handles the click event on the close button in the help screen.
   *
   * @param event the mouse event
   */
  @FXML
  private void onClickClose() {
    helpScreen.setVisible(false);
    goBackButton.setVisible(true);
    helpStarsButton.setVisible(true);
  }
}
