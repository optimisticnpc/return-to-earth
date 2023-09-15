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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
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
  @FXML private Pane room;
  @FXML private Text meterPercent;

  private Timeline timeline;
  private CurrentScene currentScene = CurrentScene.getInstance();
  private RotateTransition rotate = new RotateTransition();
  private boolean unscrewed = false;
  private boolean warning = false;

  public void initialize() {
    System.out.println("RoomThreeController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
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
                    if (oxygenBar.getProgress() > 0) {
                      if (GameState.isSpacesuitCollected) {
                        oxygenBar.setProgress(oxygenBar.getProgress() - 0.02);
                      } else {
                        oxygenBar.setProgress(oxygenBar.getProgress() - 0.05);
                      }
                      Integer percentage = (int) Math.round(oxygenBar.getProgress() * 100);
                      meterPercent.setText(Integer.toString(percentage) + "%");
                      if (oxygenBar.getProgress() <= 0.3 && oxygenBar.getProgress() >= 0.2) {
                        if (!warning) {
                          Alert alert = new Alert(Alert.AlertType.INFORMATION);
                          alert.setTitle("PLACEHOLDER");
                          alert.setHeaderText("OXYGEN RUNNING LOW");
                          alert.setContentText("DELETE LATER");
                          alert.show();
                          warning = true;
                        }
                      }
                    } else {
                      try {
                        App.setRoot("losescreen");
                        currentScene.setCurrent(4);
                        oxygenBar.setProgress(1);
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  public void initializeRotate() {
    rotate.setNode(meter);
    rotate.setDuration(Duration.millis(1500));
    rotate.setCycleCount(TranslateTransition.INDEFINITE);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setByAngle(360);
    rotate.play();
  }

  @FXML
  public void pressTimingButton(MouseEvent event) throws FileNotFoundException {
    System.out.println(meter.getRotate());
    if (meter.getRotate() <= 174 && meter.getRotate() >= 168) {
      showSuccessMessage();
    }
  }

  @FXML
  public void showSuccessMessage() throws FileNotFoundException {
    Task<Void> successTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            rotate.stop();
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
            unscrewed = true;
            return null;
          }
        };
    Thread successThread = new Thread(successTask);
    successThread.start();
  }

  @FXML
  public void pressScrew(MouseEvent event) {

    if (!GameState.isToolboxCollected) {
      // TODO: replace with speech bubble?
      // Placeholder
      ChatController.showDialog("Placeholder", "TOOLS NEEDED", "delete this later");
      return;
    }

    System.out.println("screw pressed");
    puzzleScreen.setVisible(!puzzleScreen.isVisible());
    meter.setVisible(!meter.isVisible());
    initializeRotate();
  }

  @FXML
  public void clickHatch(MouseEvent event) throws FileNotFoundException {
    System.out.println("hatch clicked");
    if (unscrewed && true) {
      InputStream stream =
          new FileInputStream(
              "src/main/resources/images/spaceship_exterior_open_hatch_wire_connected.png");
      Image img = new Image(stream);
      background.setImage(img);
    }
  }

  @FXML
  public void clickResumeButton(MouseEvent event) {
    System.out.println("resume clicked");
    if (unscrewed) {
      puzzleScreen.getChildren().clear();
      room.getChildren().remove(puzzleScreen);
      room.getChildren().remove(meter);
      room.getChildren().remove(success);
      room.getChildren().remove(screwOne);
      room.getChildren().remove(screwTwo);
      room.getChildren().remove(screwThree);
      room.getChildren().remove(screwFour);
    } else {
      puzzleScreen.setVisible(!puzzleScreen.isVisible());
      meter.setVisible(!meter.isVisible());
    }
  }

  @FXML
  public void clickRoomOne(MouseEvent event) throws IOException {
    System.out.println("Room One Clicked");

    Parent roomOneRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomOneRoot);
    currentScene.setCurrent(1);
  }
}
