package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.ControllerWithSpeechBubble;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.OxygenMeter;
import nz.ac.auckland.se206.RoomInitializer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.SpeechBubble;
import nz.ac.auckland.se206.ball.BouncingBallPane;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Controller class for the room view. */
public class RoomOneController implements ControllerWithSpeechBubble {

  private static String wordToGuess;

  public static String getWordToGuess() {
    return wordToGuess;
  }

  @FXML private Pane room;
  @FXML private ImageView background;
  @FXML private Rectangle mainWarning;
  @FXML private Rectangle authRectangle;
  @FXML private Rectangle engineWarning;
  @FXML private Polygon movetoRoomTwo;
  @FXML private Polygon movetoRoomThree;
  @FXML private Label timerLabel;
  @FXML private Label speechLabel;
  @FXML private Label hintLabel;
  @FXML private ImageView robot;
  @FXML private ImageView speechBubble;
  @FXML private ImageView wire;
  @FXML private ImageView wireImage;
  @FXML private BouncingBallPane bouncingBall;
  @FXML private Rectangle ballToggle;
  @FXML private ImageView soundIcon;
  @FXML private Polygon wireCompartmentPolygon;
  @FXML private HBox yesNoButtons;
  @FXML private Label oxygenWarningLabel;
  @FXML private Rectangle exitOxygenWarningRectangle;
  @FXML private ImageView wireCompartmentImage;
  @FXML private ImageView keypadWireCompartment;
  @FXML private AnchorPane chatPanel;

  private Boolean isPanelOnScreen = true;

  private CurrentScene currentScene = CurrentScene.getInstance();

  private SpeechBubble speech = SpeechBubble.getInstance();
  private Timer timer = new Timer();
  private ChatCentralControl chatCentralControl = ChatCentralControl.getInstance();
  private String[] riddles = {
    "blackhole", "star", "moon", "sun", "venus", "comet", "satellite", "mars", "saturn"
  };

  private Sound sound = Sound.getInstance();
  private TextToSpeech textToSpeech = new TextToSpeech();

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws FileNotFoundException if the file is not found.
   */
  public void initialize() throws FileNotFoundException {

    System.out.println("RoomOneController.initialize()");

    // initially sets speech bubble to invisible.
    speechBubble.setVisible(false);
    speechLabel.setVisible(false);

    exitOxygenWarningRectangle.setVisible(false);
    yesNoButtons.setVisible(false);
    oxygenWarningLabel.setVisible(false);

    // bouncingBall.setVisible(false); TODO: Undo this
    speechLabel.textProperty().bind(speech.speechDisplayProperty());

    soundIcon.imageProperty().bind(sound.soundImageProperty());
    soundIcon.opacityProperty().bind(sound.getIconOpacityProperty());
    InputStream soundOn = new FileInputStream("src/main/resources/images/soundicon.png");
    Image soundOnImage = new Image(soundOn);
    sound.soundImageProperty().set(soundOnImage);

    // Initializes the room for the animations to play.
    RoomInitializer roomInitializer = new RoomInitializer();
    roomInitializer.setupAiHoverImageListeners(robot);
    roomInitializer.setupPhaseChange(this);
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
   * Handles the click event on the arrow to Room 2.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the Room 2
   */
  @FXML
  private void clickRoomTwo(MouseEvent event) throws IOException {
    System.out.println("Room Two clicked");
    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
    currentScene.setCurrent(2);
  }

  /**
   * Handles the click event on the arrow to Room 3.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the Room 3
   */
  @FXML
  private void clickRoomThree(MouseEvent event) throws IOException {
    System.out.println("Room Three clicked");

    // Show warning about going outside if oxygen is low, spacesuit not collected and speech bubble
    // isn't already visible
    if (OxygenMeter.getInstance().getProgress().doubleValue() < 0.3
        && !GameState.isSpacesuitCollected
        && !speechBubble.isVisible()) {
      yesNoButtons.setVisible(true);
      oxygenWarningLabel.setVisible(true);
      speechBubble.setVisible(true);
      exitOxygenWarningRectangle.setVisible(true);
    } else {
      Parent roomThreeRoot = SceneManager.getUiRoot(AppUi.ROOM_THREE);
      currentScene.setCurrent(3);
      App.getScene().setRoot(roomThreeRoot);
    }
  }

  /** Handles the click event on the main warning. */
  @FXML
  private void onYesButton() {
    hideAllOxygenWarningElements();
    Parent roomThreeRoot = SceneManager.getUiRoot(AppUi.ROOM_THREE);
    currentScene.setCurrent(3);
    App.getScene().setRoot(roomThreeRoot);
    exitOxygenWarningRectangle.setVisible(false);
  }

  /** Handles the click event on the main warning. */
  @FXML
  private void onNoButton() {
    hideAllOxygenWarningElements();
  }

  /** Helper function that hides all the oxygen warning elements. */
  private void hideAllOxygenWarningElements() {
    yesNoButtons.setVisible(false);
    oxygenWarningLabel.setVisible(false);
    speechBubble.setVisible(false);
    exitOxygenWarningRectangle.setVisible(false);
  }

  /** Handles the click event on the main warning. */
  @FXML
  private void onClickExitOxygenWarningRectangle() {
    yesNoButtons.setVisible(false);
    oxygenWarningLabel.setVisible(false);
    speechBubble.setVisible(false);
    exitOxygenWarningRectangle.setVisible(false);
  }

  /**
   * Handles the click event on the bouncing ball pane.
   *
   * @param event the mouse event
   */
  @FXML
  private void clickBallToggle(MouseEvent event) {
    bouncingBall.setVisible(!bouncingBall.isVisible());
  }

  /**
   * Handles the click event on the main warning.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  private void clickMainWarning(MouseEvent event) throws IOException {
    System.out.println("Main Warning clicked");
    // If riddle not solved tell the player to get authorization, otherwise tell them to fix the
    // ship
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Critical damage detected on the ship. Please authorize yourself by clicking the middle"
              + " screen to access the system and analyse the damage.");
    } else {
      activateSpeech(
          "Critical damage detected on the engine! Please find the tools required and fix it!");
    }
  }

  /**
   * Handles the click event on the engine warning.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  private void clickEngineWarning(MouseEvent event) throws IOException {
    System.out.println("Engine Warning clicked");
    // If riddle not solved tell the player to get authorization
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Authorization needed to access the system. Please click on the middle screen.");
      playAuthorizationNeededSound();
    } else {
      activateSpeech(
          "Critical failure on the main engine! Please find the spare parts and fix it!");
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
   * Handles the click event on the wire.
   *
   * @param event the mouse event
   */
  @FXML
  private void clickWire(MouseEvent event) {
    GameState.isWireCollected = true;
    activateSpeech("You have collected the wire!\nYou might need it to fix something...");
    room.getChildren().remove(wire);
    room.getChildren().remove(wireImage);
  }

  /**
   * Sets the text of the speech bubble and makes it visible for 5 seconds.
   *
   * @param text text that is to be displayed in the bubble.
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
   * Handles the click event on the authorization button.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  private void clickAuthorization(MouseEvent event) throws IOException {
    System.out.println("Authorization clicked");

    // If riddle not solved tell the player to get authorized and start the riddle
    if (GameState.isRoomOneFirst
        && !GameState.isAuthorizing
        && !GameState.isPersonalitySetup) { // Make sure personality setup is done
      activateSpeech("Authorization Commencing...");
      GameState.isAuthorizing = true;
      selectRandomRiddle();
      // Add hint prompts only if difficulty is not hard and if they haven't been added already
      if (GameState.easy) {
        chatCentralControl.runGpt(
            new ChatMessage(
                "system",
                GptPromptEngineering.getRiddle(wordToGuess)
                    + GptPromptEngineering.getEasyHintSetup()));
      } else if (GameState.medium) {
        chatCentralControl.runGpt(
            new ChatMessage(
                "system",
                GptPromptEngineering.getRiddle(wordToGuess)
                    + GptPromptEngineering.getMediumHintSetup()));
      } else if (GameState.hard) {
        chatCentralControl.runGpt(
            new ChatMessage(
                "system",
                GptPromptEngineering.getRiddle(wordToGuess)
                    + GptPromptEngineering.getHardHintSetup()));
      }
      return;
    }

    if (!GameState.hard) {
      activateSpeech("Good luck fixing the ship! Let me know if you need any help.");
    } else {
      activateSpeech("Fixing the ship is very hard but I know you can do it. Keep trying!");
    }
  }

  /** Selects a random riddle from the list of riddles. */
  private void selectRandomRiddle() {
    Random random = new Random();
    wordToGuess = riddles[random.nextInt(riddles.length)];
  }

  /**
   * Handles the click event on the wire compartment.
   *
   * @param event the mouse event
   */
  @FXML
  private void onSlideChatButtonClicked(ActionEvent event) {
    System.out.println("onSlideChatButtonClicked()");

    // Target X is the value relative to the initial value
    double targetX;

    if (isPanelOnScreen) {
      targetX = -294;
      isPanelOnScreen = false;
    } else {
      targetX = 0;
      isPanelOnScreen = true;
    }

    TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), chatPanel);
    System.out.println("target X = " + targetX);
    tt.setToX(targetX);
    tt.play();
  }

  /**
   * Handles the click event on the wire compartment.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  private void clickWireCompartment(MouseEvent event) throws IOException {
    System.out.println("Wire Compartment Clicked");

    // If riddle is not solved, do no allow entry
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Authorization needed to access ship compartments. Please click on the middle screen.");
      playAuthorizationNeededSound();
      return;
    }

    // If the scramble word puzzle hasn't been solved
    // Go to enter access key screen
    if (!GameState.isWireCompartmentUnlocked) {
      // Add hint prompts only if difficulty is not hard
      if (!GameState.hard) {
        addWordScramblePromptsIfNotAdded();
      }

      Parent spacesuitPuzzlesRoom = SceneManager.getUiRoot(AppUi.WORD_SCRAMBLE);
      App.getScene().setRoot(spacesuitPuzzlesRoom);
      // If spacesuit hasn't been revealed
    } else {
      // Disable and hide images
      wireCompartmentPolygon.setVisible(false);
      fadeOutNode(wireCompartmentImage, 0.5);
      wireCompartmentImage.setDisable(true);

      keypadWireCompartment.setVisible(false);
      fadeOutNode(keypadWireCompartment, 0.5);
      keypadWireCompartment.setDisable(true);
    }
  }

  /**
   * Helper function that fades out a specified object.
   *
   * @param node the specified image
   * @param duration the duration it takes for the image to load in
   */
  private void fadeOutNode(ImageView node, double duration) {
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setNode(node);
    fadeTransition.setFromValue(1); // starting opacity value
    fadeTransition.setToValue(0); // ending opacity value (1 is fully opaque)
    fadeTransition.setDuration(Duration.seconds(duration)); // transition duration
    fadeTransition.play();
  }

  /** Handles the click event on the keypad. */
  private void addWordScramblePromptsIfNotAdded() {
    // Add hint prompts only if difficulty is not hard
    if (!GameState.isWordScramblePromptAdded) {
      String prompt = GptPromptEngineering.hintWordScrambleSetup();
      if (GameState.medium) {
        prompt = prompt + GptPromptEngineering.getMediumHintReminder();
      }

      // Add the prompt to the chat
      ChatCentralControl.getInstance()
          .getChatCompletionRequest()
          .addMessage(new ChatMessage("system", prompt));
      GameState.isWordScramblePromptAdded = true;
    }
  }
}
