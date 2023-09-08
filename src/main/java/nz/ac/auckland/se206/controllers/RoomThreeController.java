package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class RoomThreeController {
  @FXML private Label timerLabel;
  @FXML private Rectangle goBackRectangle;
  @FXML private Rectangle door;
  @FXML private Polygon hatch;
  @FXML private VBox puzzleScreen;
  @FXML private Button resumeButton;
  @FXML private Circle screwOne;
  @FXML private Circle screwTwo;
  @FXML private Circle screwThree;
  @FXML private Circle screwFour;
  @FXML private Button timingButton;
  @FXML private ImageView meter;
  @FXML private Text success;
  @FXML private ImageView background;
  @FXML private ProgressBar oxygenBar;
  private Timeline timeline;
  CurrentScene currentScene = CurrentScene.getInstance();

  public void initialize() {
    System.out.println("RoomThreeController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
    currentScene.setCurrent(3);
    initializeOxygen();
    timeline.play();
  }

  public void initializeOxygen() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (currentScene.getCurrent() == 3) {
                    if (oxygenBar.getProgress() < 1) {
                      oxygenBar.setProgress(oxygenBar.getProgress() + 0.02);
                    } else {
                      try {
                        App.setRoot("losescreen");
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  public void initializeRotate() {
    RotateTransition rotate = new RotateTransition();
    rotate.setNode(meter);
    rotate.setDuration(Duration.millis(1500));
    rotate.setCycleCount(TranslateTransition.INDEFINITE);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setByAngle(360);
    rotate.play();
  }

  @FXML
  public void clickTimingButton(MouseEvent event) throws FileNotFoundException {
    System.out.println(meter.getRotate());
    if (meter.getRotate() <= 184 && meter.getRotate() >= 180) {
      showSuccessMessage();
    }
  }

  @FXML
  public void showSuccessMessage() throws FileNotFoundException {
    success.setVisible(!success.isVisible());
    FadeTransition fade = new FadeTransition();
    fade.setNode(success);
    fade.setDuration(Duration.millis(2000));
    fade.setInterpolator(Interpolator.LINEAR);
    fade.setFromValue(1);
    fade.setToValue(0);
    fade.play();
    InputStream stream =
        new FileInputStream("src/main/resources/images/spaceship_exterior_open_hatch.png");
    Image img = new Image(stream);
    background.setImage(img);
    screwOne.setVisible(false);
    screwTwo.setVisible(false);
    screwThree.setVisible(false);
    screwFour.setVisible(false);
  }

  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    // TODO: Change or move this
    GameState.isGameWon = true;
    App.setRoot("winscreen");
  }

  @FXML
  public void clickScrew(MouseEvent event) {
    System.out.println("hatch clicked");
    puzzleScreen.setVisible(!puzzleScreen.isVisible());
    meter.setVisible(!meter.isVisible());
    initializeRotate();
  }

  @FXML
  public void clickHatch(MouseEvent event) {
    System.out.println("hatch clicked");
    // NEED TO ADD --> ONLY IF PLAYER HAS THE TOOL/SELECTED CORRECT TOOL
    // puzzleScreen.setVisible(!puzzleScreen.isVisible());
  }

  @FXML
  public void clickResumeButton(MouseEvent event) {
    System.out.println("resume clicked");

    puzzleScreen.setVisible(!puzzleScreen.isVisible());
    meter.setVisible(!meter.isVisible());
  }

  @FXML
  public void clickGoBackRectangle(MouseEvent event) throws IOException {
    System.out.println("go back clicked");

    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
    currentScene.setCurrent(2);
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }
}
