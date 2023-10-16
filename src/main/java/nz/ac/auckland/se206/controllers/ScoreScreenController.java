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

/**
 * The ScoreScreenController class controls the behavior and interactions of the score screen. It
 * displays the fastest completion times for different difficulty levels and provides star ratings
 * based on completion times. The player can access help on star ratings and return to the start
 * screen.
 */
public class ScoreScreenController {
  // Add to ScoreScreenController
  private static ScoreScreenController instance;

  /** Refreshes times if instance is null. */
  public static void updateFastestTimes() {
    if (instance != null) {
      instance.refreshTimes();
    }
  }

  @FXML private Label fastestEasyTimeLabel;
  @FXML private Label fastestMediumTimeLabel;
  @FXML private Label fastestHardTimeLabel;
  @FXML private ImageView easyStars;
  @FXML private ImageView mediumStars;
  @FXML private ImageView hardStars;
  @FXML private Pane helpScreen;
  @FXML private Button helpStarsButton;
  @FXML private Button goBackButton;

  /**
   * Initializes a new instance of the ScoreScreenController class. This constructor sets the
   * instance variable to reference this instance of the controller.
   */
  public ScoreScreenController() {
    instance = this;
  }

  /**
   * Initializes the score screen, updating fastest completion times, star ratings, and hiding the
   * help screen.
   */
  @FXML
  public void initialize() {
    System.out.println("ScoreScreenController.initialize()");
    refreshTimes();
    helpScreen.setVisible(false);
  }

  /**
   * Refreshes the fastest completion times and star ratings displayed on the score screen. This
   * method is called when there is a change in completion times.
   */
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

  /**
   * Updates the star rating displayed for a given difficulty level based on the completion time.
   *
   * @param imageView The ImageView element representing the star rating.
   * @param hundredths The completion time in hundredths of a second.
   */
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

  /** Handles the click event when the player wants to go back to the start screen. */
  @FXML
  private void onGoBack() {
    Parent startParent = SceneManager.getUiRoot(AppUi.START);
    App.getScene().setRoot(startParent);
  }

  /**
   * Handles the click event when the player clicks the help button for star ratings. It displays
   * the help screen and hides the buttons temporarily.
   */
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

  @FXML
  private void onClickResetButton() {
    System.out.println("Times Reset");
    ScoreScreenInfo.fastestEasyTimeHundredths = Integer.MAX_VALUE;
    ScoreScreenInfo.fastestMediumTimeHundredths = Integer.MAX_VALUE;
    ScoreScreenInfo.fastestHardTimeHundredths = Integer.MAX_VALUE;
    System.out.println(ScoreScreenInfo.fastestEasyTimeHundredths);
    ScoreScreenInfo.saveTimesToFile();
    refreshTimes();
  }
}
