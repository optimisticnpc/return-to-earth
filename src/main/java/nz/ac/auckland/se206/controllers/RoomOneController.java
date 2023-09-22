package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.SpeechBubble;
import nz.ac.auckland.se206.ball.BouncingBallPane;

/** Controller class for the room view. */
public class RoomOneController {

  @FXML private Pane room;
  @FXML private Button closeButton;
  @FXML private Pane backgroundScreen;
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

  private CurrentScene currentScene = CurrentScene.getInstance();

  private SpeechBubble speech = SpeechBubble.getInstance();
  private Timer timer = new Timer();
  private GameTimer gameTimer = GameTimer.getInstance();

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

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
  }

  /**
   * Handles the click event on the arrow to Room 2.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
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
   * @throws IOException if there is an error loading the chat view
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
    activateSpeech(
        "The system detected a critical damage.\n"
            + "Please authorise yourself by clicking \nthe middle screen "
            + "to access the system\nand analyse the damage.");
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
      activateSpeech("Authorisation Needed. \nYou need to be authorised to access\nthe system.");
    } else {
      activateSpeech(
          "Critical failure on the main engine\n"
              + "The main engine is damaged.\n"
              + "Please find the spare parts\n"
              + "and fix the engine!");
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
    room.getChildren().remove(wire);
    room.getChildren().remove(wireImage);
  }

  /**
   * Handles the click event on the close button.
   *
   * @param event the mouse event
   */
  @FXML
  private void onClickClose(ActionEvent event) {
    backgroundScreen.getChildren().clear();
    room.getChildren().remove(backgroundScreen);
    activateSpeech(
        "Hey you! Have you passed the\nauthorisation riddle to be\ntouching this stuff?");
  }

  /**
   * Sets the text of the speech bubble and makes it visible for 5 seconds.
   *
   * @param text
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
    Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
    App.getScene().setRoot(chatRoot);
    GameState.isRoomOneFirst = false;
    currentScene.setCurrent(11);
  }
}
