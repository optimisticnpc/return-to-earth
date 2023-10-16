package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.SpeechBubble;

public class JokeController {

  @FXML HBox yesNoButtons;
  @FXML ImageView challengeSpeechBubble;

  private Timer timer = new Timer();
  private SpeechBubble speech = SpeechBubble.getInstance();

  @FXML private Label speechLabel;
  @FXML private ImageView speechBubble;
  @FXML private ImageView spacesuitCollectedImage;
  @FXML private Label timerLabel;
  @FXML private Label challengeLabel;

  /** Initializes the word joke screen, setting up the timer, input field. */
  public void initialize() {
    System.out.println("JokeController.initialize()");

    speechBubble.setVisible(false);
    speechLabel.setVisible(false);
    speechLabel.textProperty().bind(speech.speechDisplayProperty());

    // Bind timer
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
  }

  /**
   * Handles the action when the player goes back from the word scramble puzzle screen to the
   * previous room.
   */
  @FXML
  private void onGoBack() {
    Parent root = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(root);
  }

  /**
   * Handles the click event on the spacesuit.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the spacesuit puzzle view
   */
  @FXML
  public void clickSpacesuit(MouseEvent event) throws IOException {
    System.out.println("Spacesuit Clicked");

    if (GameState.isJokeResolved.get() && !GameState.isSpacesuitCollected) {
      collectSpacesuit();
      activateSpeech("You have collected the spacesuit! Now you can go on spacewalks for longer!");
      GameState.isSpacesuitCollected = true;
      GameState.isSpacesuitJustCollected = true;
    }
  }

  /**
   * Helper function that fades in a specified object.
   *
   * @param node the specified image
   * @param duration the duration it takes for the image to load in
   */
  private void fadeInNode(ImageView node, double duration) {
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setNode(node);
    fadeTransition.setFromValue(0); // starting opacity value
    fadeTransition.setToValue(1); // ending opacity value (1 is fully opaque)
    fadeTransition.setDuration(Duration.seconds(duration)); // transition duration
    fadeTransition.play();
  }

  private void collectSpacesuit() {
    fadeInNode(spacesuitCollectedImage, 0.5);
  }

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

  @FXML
  public void onYesButton() {
    challengeSpeechBubble.setVisible(false);
    yesNoButtons.setVisible(false);
    challengeLabel.setVisible(false);
    GameState.isJokeChallengeAccepted.set(true);
  }
}
