package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.OxygenMeter;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.SpeechBubble;

/**
 * Controller class for Room Three in the game. This class handles user interactions, game logic,
 * and UI updates for Room Three.
 */
public class RoomThreeController {
  @FXML private Label timerLabel;
  @FXML private Label speechLabel;
  @FXML private ImageView speechBubble;
  @FXML private Label hintLabel;
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
  @FXML private ImageView robot;
  @FXML private ImageView soundIcon;

  private CurrentScene currentScene = CurrentScene.getInstance();
  private RotateTransition rotate = new RotateTransition();
  private boolean unscrewed = false;
  private SpeechBubble speech = SpeechBubble.getInstance();
  private Timer timer = new Timer();
  private Sound sound = Sound.getInstance();
  private Boolean isWarningShown = false;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    System.out.println("RoomThreeController.initialize()");

    // Bind timer
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
    OxygenMeter oxygenMeter = OxygenMeter.getInstance();
    oxygenMeter.setRoomThreeController(this);
    oxygenBar.progressProperty().bind(oxygenMeter.oxygenProgressProperty());
    meterPercent.textProperty().bind(oxygenMeter.percentProgressProperty());

    // Hides speech bubble at first
    speechBubble.setVisible(false);
    speechLabel.setVisible(false);
    speechLabel.textProperty().bind(speech.speechDisplayProperty());

    // Set hint counter
    HintCounter hintCounter = HintCounter.getInstance();
    hintCounter.setHintCount();
    hintLabel.textProperty().bind(hintCounter.hintCountProperty());

    soundIcon.imageProperty().bind(sound.soundImageProperty());
  }

  /**
   * Handles the click event on the sound icon.
   *
   * @throws FileNotFoundException if the file is not found.
   */
  @FXML
  private void clickSoundIcon() throws FileNotFoundException {
    sound.toggleImage();
  }

  /** Initializes the rotating meter, it is called when the screw is clicked. */
  public void initializeRotate() {
    // Initializes the parameters of the rotating pointer in the timing game
    Task<Void> rotateTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            rotate.setNode(meter); // Connect the RotateTransition to the image
            rotate.setDuration(Duration.millis(1500)); // Set the period to 1.5s
            rotate.setCycleCount(TranslateTransition.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.setByAngle(360);
            rotate.play();
            return null;
          }
        };
    Thread rotateThread = new Thread(rotateTask);
    rotateThread.start();
  }

  /** Shows a low oxygen warning when oxygen is under 30% and it's the first warning. */
  public void showLowOxygen() {
    if (!isWarningShown) {
      activateSpeech("OXYGEN RUNNING LOW!\n OXYGEN RUNNING LOW!\n OXYGEN RUNNING LOW!");
      isWarningShown = true;
    }
  }

  /** Changes oxygen meter colour and width once spacesuit is collected. */
  public void showSpacesuitOxygen() {
    oxygenBar.setStyle("-fx-accent: #ADD8E6;");
    oxygenBar.setPrefWidth(350);
  }

  /**
   * Handles the click event on the authorisation button.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickAuthorisation(MouseEvent event) throws IOException {
    // If the riddle is not solved tell the player to get authorisation
    if (!GameState.isRiddleResolved) {
      activateSpeech("Authorisation needed to access\nthe system.");
      return;
    }
    Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
    App.getScene().setRoot(chatRoot);
    // Let the game know that the previous scene was room 3
    currentScene.setCurrent(13);
  }

  /**
   * Handles the click event on the timing button within the timing minigame.
   *
   * @param event The mouse event triggered by clicking the timing button.
   * @throws FileNotFoundException if there is an error with the background image file.
   */
  @FXML
  public void pressTimingButton(MouseEvent event) throws FileNotFoundException {
    System.out.println(meter.getRotate());
    if (meter.getRotate() <= 177 && meter.getRotate() >= 164) {
      showSuccessMessage();
    }
  }

  /**
   * Shows a 'success!' message once the player completes the timing minigame.
   *
   * @throws FileNotFoundException if the background image file is not found.
   */
  @FXML
  public void showSuccessMessage() throws FileNotFoundException {
    Task<Void> successTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            rotate.stop();
            // Set the success message as visible and then slowly fade it out
            success.setVisible(true);
            FadeTransition fade = new FadeTransition();
            fade.setNode(success);
            fade.setDuration(Duration.millis(2000));
            fade.setInterpolator(Interpolator.LINEAR);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.play();
            // Change image to show open hatch
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

  /**
   * Handles the click event on the screw.
   *
   * @param event the mouse event
   */
  @FXML
  public void pressScrew(MouseEvent event) {

    // If toolbox not collected
    if (!GameState.isToolboxCollected) {
      activateSpeech("You need to find the right tools\nto open this hatch.");
      return;
    }

    System.out.println("screw pressed");
    // Show timing game
    puzzleScreen.setVisible(!puzzleScreen.isVisible());
    meter.setVisible(!meter.isVisible());
    initializeRotate();
  }

  /**
   * Handles the click event on the hatch.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat
   */
  @FXML
  public void clickHatch(MouseEvent event) throws IOException {
    System.out.println("hatch clicked");
    // If the hatch is open and the player has the wire
    // Fix the broken part
    if (unscrewed && GameState.isWireCollected) {
      InputStream stream =
          new FileInputStream(
              "src/main/resources/images/spaceship_exterior_open_hatch_wire_connected.png");
      Image img = new Image(stream);
      background.setImage(img);
      GameState.isPartFixed = true;
      GameState.phaseFour = true;
      SceneManager.addUi(AppUi.CHAT, App.loadFxml("chat"));
      activateSpeech(
          "You have fixed the engine!\n Now you have to reactivate it\n from somewhere...");
      ;
    }
  }

  /**
   * Sets the text of the speech bubble and makes it visible for 5 seconds.
   *
   * @param text the words for the ai to say
   */
  @FXML
  public void activateSpeech(String text) {
    // Make the speech bubble visible and set the text
    speechBubble.setVisible(true);
    speechLabel.setVisible(true);
    speech.setSpeechText(text);
    timer.schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            speechBubble.setVisible(false);
            speechLabel.setVisible(false);
          }
        },
        5000);
    // 5 second delay
  }

  /**
   * Handles the click event on the resume button.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickResumeButton(MouseEvent event) {
    System.out.println("resume clicked");
    // If timing game completed, remove all the unecessary components
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
      // Hide puzzle screen
      puzzleScreen.setVisible(!puzzleScreen.isVisible());
      meter.setVisible(!meter.isVisible());
    }
  }

  /**
   * Handles the click event on the room 1 triangle.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading room1/room1final
   */
  @FXML
  public void clickRoomOne(MouseEvent event) throws IOException {
    System.out.println("Room One Clicked");
    if (GameState.isPartFixed) {
      // If part is fixed, go to room with reactivate button
      Parent roomOneRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE_FINAL);
      App.getScene().setRoot(roomOneRoot);
    } else {
      Parent roomOneRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
      App.getScene().setRoot(roomOneRoot);
    }
    currentScene.setCurrent(1);
  }
}
