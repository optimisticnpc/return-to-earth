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
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.SpeechBubble;

/**
 * The `JokeController` class is responsible for handling interactions in the word joke screen,
 * including collecting the spacesuit and managing speech bubbles.
 */
public class JokeController {

  private Timer timer = new Timer();
  private SpeechBubble speech = SpeechBubble.getInstance();

  @FXML private HBox yesNoButtons;
  @FXML private Label normalSpeechLabel;
  @FXML private ImageView speechBubble;
  @FXML private ImageView spacesuitCollectedImage;
  @FXML private Label timerLabel;
  @FXML private Label challengeLabel;

  /** Initializes the word joke screen, setting up the timer, input field. */
  public void initialize() {
    System.out.println("JokeController.initialize()");

    speechBubble.setVisible(true);
    normalSpeechLabel.setVisible(false);
    normalSpeechLabel.textProperty().bind(speech.speechDisplayProperty());
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
  private void clickSpacesuit(MouseEvent event) throws IOException {
    System.out.println("Spacesuit Clicked");
    // If the spacesuit has been collected, go to the spacesuit puzzle
    if (GameState.isJokeResolved.get() && !GameState.isSpacesuitCollected) {
      collectSpacesuit();
      activateSpeech(
          "Congratulations, you have collected the spacesuit!\nNow you can go on spacewalks for"
              + " longer!");
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

  /** Helper function that fades out the collected spacesuit. */
  private void collectSpacesuit() {
    fadeInNode(spacesuitCollectedImage, 0.5);
  }

  /**
   * Helper function that activates the speech bubble.
   *
   * @param text the text to be displayed in the speech bubble
   */
  @FXML
  private void activateSpeech(String text) {
    // Make the speech bubble visible and set the text
    speechBubble.setVisible(true);
    normalSpeechLabel.setVisible(true);
    speech.setSpeechText(text);
    timer.schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            speechBubble.setVisible(false);
            normalSpeechLabel.setVisible(false);
          }
        },
        5000);
    // 5 second delay
  }

  /** Handles the action when the player clicks on the yes button. */
  @FXML
  private void onYesButton() {
    // Sets the scene to change to the joke game puzzle
    speechBubble.setVisible(false);
    yesNoButtons.setVisible(false);
    challengeLabel.setVisible(false);
    GameState.isJokeChallengeAccepted.set(true);
  }
}
