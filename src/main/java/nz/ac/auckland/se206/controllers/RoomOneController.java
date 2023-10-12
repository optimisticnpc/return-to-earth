package nz.ac.auckland.se206.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.SpeechBubble;
import nz.ac.auckland.se206.ball.BouncingBallPane;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Controller class for the room view. */
public class RoomOneController {

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

  @FXML private AnchorPane chatPanel;

  private Boolean isPanelOnScreen = true;

  private CurrentScene currentScene = CurrentScene.getInstance();

  private SpeechBubble speech = SpeechBubble.getInstance();
  private Timer timer = new Timer();
  private GameTimer gameTimer = GameTimer.getInstance();
  private ChatCentralControl chatCentralControl = ChatCentralControl.getInstance();
  private String[] riddles = {
    "blackhole", "star", "moon", "sun", "venus", "comet", "satellite", "mars"
  };
  private String wordToGuess;

  private Sound sound = Sound.getInstance();

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws FileNotFoundException if the file is not found.
   */
  public void initialize() throws FileNotFoundException {

    System.out.println("RoomOneController.initialize()");
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
    // initially sets speech bubble to invisible.
    speechBubble.setVisible(false);
    speechLabel.setVisible(false);
    bouncingBall.setVisible(false);
    speechLabel.textProperty().bind(speech.speechDisplayProperty());
    // update hintlabel according to the difficulty
    HintCounter hintCounter = HintCounter.getInstance();
    hintCounter.setHintCount();
    hintLabel.textProperty().bind(hintCounter.hintCountProperty());

    soundIcon.imageProperty().bind(sound.soundImageProperty());
    InputStream soundOn = new FileInputStream("src/main/resources/images/soundicon.png");
    Image soundOnImage = new Image(soundOn);
    sound.soundImageProperty().set(soundOnImage);

    // I don't think we need this. Also it sometimes doesn't show up if the player is clicking
    // quickly through the background story screen
    // activateSpeech(
    //     "Hey you! Have you passed the\nauthorisation riddle to be\ntouching this stuff?");
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
    Parent roomThreeRoot = SceneManager.getUiRoot(AppUi.ROOM_THREE);
    currentScene.setCurrent(3);
    App.getScene().setRoot(roomThreeRoot);
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
    if (!GameState.isRiddleResolved) {
      activateSpeech(
          "Critical damage detected on the ship.\n"
              + "Please authorise yourself by clicking \nthe middle screen "
              + "to access the system and analyse the damage.");
      chatCentralControl.addMessage(
          new ChatMessage(
              "system",
              "Critical damage detected on the ship.\n"
                  + "Please authorise yourself by clicking \nthe middle screen "
                  + "to access the system\nand analyse the damage."));
    } else {
      activateSpeech(
          "Critical damage detected on the engine!\n"
              + "Please find the tools required and fix it!");
      chatCentralControl.addMessage(
          new ChatMessage(
              "system",
              "Critical damage detected on the engine!\n"
                  + "Please find the spare tools and\nfix it!"));
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
      activateSpeech("Authorisation needed to access\nthe system.");
      chatCentralControl.addMessage(
          new ChatMessage(
              "system",
              "Authorisation needed.\n"
                  + "Please authorise yourself by clicking the middle screen"
                  + "to access the system and analyse the damage."));
    } else {
      activateSpeech(
          "Critical failure on the main engine\n" + "Please find the spare parts and\nfix it!");
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
    activateSpeech("You have collected the wire!\nYou might need it to\nfix something...");
    chatCentralControl.addMessage(
        new ChatMessage(
            "system",
            "You have collected the wire!\n" + "You might need it to\n" + "fix something..."));
    room.getChildren().remove(wire);
    room.getChildren().remove(wireImage);
  }

  /**
   * Sets the text of the speech bubble and makes it visible for 5 seconds.
   *
   * @param text text that is to be displayed in the bubble.
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

  /**
   * Handles the click event on the authorisation button.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  private void clickAuthorisation(MouseEvent event) throws IOException {
    System.out.println("Authorisation clicked");
    GameState.isAuthorising = true;
    if (GameState.isRoomOneFirst) {
      selectRandomRiddle();
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
    }
  }

  private void selectRandomRiddle() {
    Random random = new Random();
    wordToGuess = riddles[random.nextInt(riddles.length)];
  }

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
}
