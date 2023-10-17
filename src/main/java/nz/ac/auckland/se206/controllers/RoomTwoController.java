package nz.ac.auckland.se206.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RoomInitializer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.SpeechBubble;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * The RoomTwoController class controls the behavior and interactions within Room Two of the game.
 * This room contains various elements and interactions, including speech bubbles, timers, puzzle
 * items, and navigation to other scenes. It manages the state of the room, such as whether puzzle
 * items have been collected or revealed, and handles player interactions.
 */
public class RoomTwoController {
  @FXML private Pane room;
  @FXML private Label timerLabel;
  @FXML private Label speechLabel;
  @FXML private ImageView speechBubble;
  @FXML private Label hintLabel;
  @FXML private ImageView toolBoxOpenImage;
  @FXML private ImageView toolBoxCollectedImage;
  @FXML private ImageView spacesuitRevealedImage;
  @FXML private ImageView spacesuitCollectedImage;
  @FXML private Rectangle questionOne;
  @FXML private Rectangle questionTwo;
  @FXML private Polygon crate;
  @FXML private ImageView crateImage;
  @FXML private ImageView robot;
  @FXML private ImageView soundIcon;

  private TextToSpeech textToSpeech = new TextToSpeech();
  private SpeechBubble speech = SpeechBubble.getInstance();
  private Timer timer = new Timer();
  private CurrentScene currentScene = CurrentScene.getInstance();
  private Sound sound = Sound.getInstance();
  private ChatCentralControl chat = ChatCentralControl.getInstance();

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    System.out.println("RoomTwoController.initialize()");
    speechBubble.setVisible(false);
    speechLabel.setVisible(false);
    speechLabel.textProperty().bind(speech.speechDisplayProperty());

    soundIcon.imageProperty().bind(sound.soundImageProperty());
    soundIcon.opacityProperty().bind(sound.getIconOpacityProperty());

    // Make the overlay images not visible
    toolBoxOpenImage.setOpacity(0);
    toolBoxCollectedImage.setOpacity(0);
    spacesuitRevealedImage.setOpacity(0);
    spacesuitCollectedImage.setOpacity(0);

    // Add a listener to isJokeResolved property
    GameState.isJokeResolved.addListener(
        (observable, oldValue, newValue) -> {
          if (!oldValue && newValue) { // If it changes from false to true
            collectSpacesuit();
          }
        });
    // Initializes the room for the animations to play.
    RoomInitializer roomInitializer = new RoomInitializer();
    roomInitializer.setupAiHoverImageListeners(robot);
  }

  /**
   * Handles the click event on the sound icon.
   *
   * @throws FileNotFoundException if file is not found.
   */
  @FXML
  private void clickSoundIcon() throws FileNotFoundException {
    sound.toggleImage();
  }

  /**
   * Handles the click event on the authorization button.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the authorization view
   */
  @FXML
  private void clickAuthorization(MouseEvent event) throws IOException {
    // If riddle not solved tell the player to get authorized
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Authorization needed to access the system. Please click on the middle screen in control"
              + " room.");

      playAuthorizationNeededSound();
      return;
    }
    if (!GameState.hard) {
      activateSpeech("Good luck fixing the ship! Let me know if you need any help.");
    } else {
      activateSpeech("Fixing the ship is very hard but I know you can do it. Keep trying!");
    }
  }

  private void playAuthorizationNeededSound() {
    if (sound.isSoundOnProperty().get()) {
      // Text to speech tells the player they are low on oxygen
      new Thread(
              () -> {
                try {
                  if (sound.isSoundOnProperty().get()) {
                    textToSpeech.speak("Authorization Needed");
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              })
          .start();
    }
  }

  /**
   * Handles the click event on the room 1 triangle.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the room1/room1final view
   */
  @FXML
  private void clickRoomOne(MouseEvent event) throws IOException {
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

  /**
   * Handles the click event on question 1.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the question 1 view
   */
  @FXML
  private void clickQuestionOne(MouseEvent event) throws IOException {
    // If riddle is not solved, do no allow entry
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Authorization needed to access ship materials. Please click on the middle screen in the"
              + " control room.");
      playAuthorizationNeededSound();
      return;
    }
    if (!GameState.hard) {
      addMathPromptsIfNotAdded();
    }

    Parent questionOneRoot = SceneManager.getUiRoot(AppUi.QUESTION_ONE);
    App.getScene().setRoot(questionOneRoot);
  }

  /**
   * Adds the math prompts if they have not been added yet. This is called when the player clicks on
   * a question.
   */
  private void addMathPromptsIfNotAdded() {
    // If the math question prompt has not been added yet, add it
    if (!GameState.isMathQuestionPromptAdded) {
      String prompt = GptPromptEngineering.hintMathQuestionPrompt();
      if (GameState.medium) {
        prompt = prompt + GptPromptEngineering.getMediumHintReminder();
      }
      // Add the prompt to the chat
      ChatCentralControl.getInstance()
          .getChatCompletionRequest()
          .addMessage(new ChatMessage("system", prompt));
      GameState.isMathQuestionPromptAdded = true;
    }
  }

  /**
   * Handles the click event on question 2.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the question 2 view
   */
  @FXML
  private void clickQuestionTwo(MouseEvent event) throws IOException {
    // If riddle is not solved, do no allow entry
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Authorization needed to access ship materials. Please click on the middle screen in the"
              + " control room.");
      playAuthorizationNeededSound();
      return;
    }

    if (!GameState.hard) {
      addMathPromptsIfNotAdded();
    }

    Parent questionTwoRoot = SceneManager.getUiRoot(AppUi.QUESTION_TWO);
    App.getScene().setRoot(questionTwoRoot);
  }

  /**
   * Sets the text of the speech bubble and makes it visible for 5 seconds.
   *
   * @param text tex that is to be displayed in the bubble.
   */
  @FXML
  private void activateSpeech(String text) {
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

  /** Makes the speech bubble and label visible. This is called when the speech bubble is shown. */
  @FXML
  private void showSpeechBubble() {
    speechBubble.setVisible(true);
    speechLabel.setVisible(true);
  }

  /**
   * Handles the click event on the crate element. If the toolbox has been collected, this method
   * initiates a fade-out animation for the crate image, followed by removing both the crate and
   * crate image from the room's children.
   *
   * @param event The mouse event triggered by clicking on the crate.
   * @throws InterruptedException if there is an issue with thread execution during the animation.
   */
  @FXML
  private void clickCrate(MouseEvent event) throws InterruptedException {
    System.out.println("Crate clicked");

    if (GameState.isToolboxCollected) {
      FadeTransition fadeTransition = new FadeTransition();
      fadeTransition.setNode(crateImage);
      fadeTransition.setFromValue(1); // starting opacity value
      fadeTransition.setToValue(0); // ending opacity value (1 is fully opaque)
      fadeTransition.setDuration(Duration.millis(300)); // transition duration
      fadeTransition.setOnFinished(
          e -> {
            room.getChildren().remove(crate);
            room.getChildren().remove(crateImage);
          });
      fadeTransition.play();
    }
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

    // If riddle is not solved, do no allow entry
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Authorization needed to access the system. Please click on the middle screen in the"
              + " control room.");
      return;
    }
    // If the joke is not resolved, go to joke puzzle
    if (!GameState.isSpacesuitRevealed) {
      revealSpacesuit();
      GameState.isSpacesuitRevealed = true;
    } else if (!GameState.isJokeResolved.get()) {
      Parent spacesuitPuzzlesRoom = SceneManager.getUiRoot(AppUi.JOKE_PUZZLE);
      App.getScene().setRoot(spacesuitPuzzlesRoom);
    } else if (!GameState.isSpacesuitCollected) {
      collectSpacesuit();
      activateSpeech(
          "Congratulations, you have collected the spacesuit! Now you can go on spacewalks for"
              + " longer!");
      GameState.isSpacesuitCollected = true;
      GameState.isSpacesuitJustCollected = true;
    }
  }

  /**
   * Handles the click event on the tool compartment.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the passcode view
   */
  @FXML
  private void clickToolCompartment(MouseEvent event) throws IOException {
    System.out.println("Tool Compartment Clicked");

    // If riddle is not solved, do no allow entry
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Authorization needed to access ship compartments. Please click on the middle screen in"
              + " the control room.");
      playAuthorizationNeededSound();
      return;
    }

    // If the passcode hasn't been solved
    // Go to enter access key screen
    if (!GameState.isPasscodeSolved) {
      Parent passcodeScreen = SceneManager.getUiRoot(AppUi.PASSCODE);
      App.getScene().setRoot(passcodeScreen);
    } else if (!GameState.isToolboxRevealed) {
      revealToolbox();
      GameState.isToolboxRevealed = true;
    } else if (!GameState.isToolboxCollected) {
      collectToolbox();
      GameState.isToolboxCollected = true;
      GameState.isPhaseChange = true;
      // Add the prompt to the chat
      if (GameState.hard) {
        chat.runGpt(new ChatMessage("system", GptPromptEngineering.getHardPhaseThreeProgress()));
      } else {
        chat.runGpt(new ChatMessage("system", GptPromptEngineering.getPhaseThreeProgress()));
      }
      System.out.println("Toolbox collected");
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

  public void revealToolbox() {
    fadeInNode(toolBoxOpenImage, 0.7);
  }

  private void collectToolbox() {
    fadeInNode(toolBoxCollectedImage, 0.5);
  }

  public void revealSpacesuit() {
    fadeInNode(spacesuitRevealedImage, 0.7);
  }

  private void collectSpacesuit() {
    fadeInNode(spacesuitCollectedImage, 0.5);
  }
}
