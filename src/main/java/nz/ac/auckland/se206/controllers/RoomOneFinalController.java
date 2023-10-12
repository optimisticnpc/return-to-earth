package nz.ac.auckland.se206.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ButtonOrder;
import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.SpeechBubble;
import nz.ac.auckland.se206.ball.BouncingBallPane;
import nz.ac.auckland.se206.gpt.ChatMessage;

/** Controller class for the room view. */
public class RoomOneFinalController {

  @FXML private Pane room;
  @FXML private Rectangle authRectangle;
  @FXML private Polygon movetoRoomTwo;
  @FXML private Polygon movetoRoomThree;
  @FXML private Label timerLabel;
  @FXML private Label speechLabel;
  @FXML private Label hintLabel;
  @FXML private Rectangle redSwitch;
  @FXML private Rectangle greenSwitch;
  @FXML private Rectangle blueSwitch;
  @FXML private ImageView robot;
  @FXML private ImageView speechBubble;
  @FXML private Rectangle reactivate;
  @FXML private Polygon reactivationHint;
  @FXML private TextArea inputText;
  @FXML private VBox chatLog;
  @FXML private ScrollPane scrollPane;
  @FXML private Button sendButton;
  @FXML private BouncingBallPane bouncingBall;
  @FXML private Rectangle ballToggle;
  @FXML private ImageView soundIcon;

  private ButtonOrder buttonOrder = ButtonOrder.getInstance();
  private String[] switchOrder = buttonOrder.getCorrectOrderArray();
  private int switchIndex = 0;
  private int correctSwitch = 0;
  private CurrentScene currentScene = CurrentScene.getInstance();

  private SpeechBubble speech = SpeechBubble.getInstance();
  private ChatController chatController = new ChatController();
  private ChatCentralControl chatCentralControl = ChatCentralControl.getInstance();
  private Timer timer = new Timer();
  private GameTimer gameTimer = GameTimer.getInstance();
  private Sound sound = Sound.getInstance();

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

    System.out.println("RoomOneFinalController.initialize()");

    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    bouncingBall.setVisible(false);

    speechBubble.setVisible(false);
    speechLabel.setVisible(false);
    speechLabel.textProperty().bind(speech.speechDisplayProperty());
    // update hintlabel according to the difficulty
    HintCounter hintCounter = HintCounter.getInstance();
    hintCounter.setHintCount();
    hintLabel.textProperty().bind(hintCounter.hintCountProperty());

    soundIcon.imageProperty().bind(sound.soundImageProperty());
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

  public void setSendButtonAction() {
    chatController.setSendButtonAction();
  }

  @FXML // send the message when the enter key is pressed
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
      sendButton.fire();
    }
  }

  /**
   * Handles the click event on the arrow to Room 2.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the room 2
   */
  @FXML
  public void clickRoomTwo(MouseEvent event) throws IOException {
    System.out.println("Room Two clicked");
    Parent roomTwoRoot = SceneManager.getUiRoot(AppUi.ROOM_TWO);
    App.getScene().setRoot(roomTwoRoot);
    currentScene.setCurrent(2);
  }

  /**
   * Handles the click event on the arrow to Room 3.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the room 3
   */
  @FXML
  public void clickRoomThree(MouseEvent event) throws IOException {
    System.out.println("Room Three clicked");
    Parent roomThreeRoot = SceneManager.getUiRoot(AppUi.ROOM_THREE);
    currentScene.setCurrent(3);
    App.getScene().setRoot(roomThreeRoot);
  }

  /**
   * Handles the click event on the reactivation hint paper.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickReactivationHint(MouseEvent event) {
    Parent reactivationRoot = SceneManager.getUiRoot(AppUi.REACTIVATION_ORDER);
    App.getScene().setRoot(reactivationRoot);
  }

  /**
   * Handles the click event on the red switch.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading
   */
  @FXML
  public void clickRedSwitch(MouseEvent event) throws IOException {
    System.out.println("Red Switch clicked");
    redSwitch.setVisible(false);
    // Increments correct switch if user clicks in the right order
    if (switchOrder[switchIndex].equals("red")) {
      switchIndex++;
      correctSwitch++;
    } else {
      switchIndex++;
    }
  }

  /**
   * Handles the click event on the green switch.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading
   */
  @FXML
  public void clickGreenSwitch(MouseEvent event) throws IOException {
    System.out.println("Green Switch clicked");
    greenSwitch.setVisible(false);
    // Increments correct switch if user clicks in the right order
    if (switchOrder[switchIndex].equals("green")) {
      switchIndex++;
      correctSwitch++;
    } else {
      switchIndex++;
    }
  }

  /**
   * Handles the click event on the blue switch.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading
   */
  @FXML
  public void clickBlueSwitch(MouseEvent event) throws IOException {
    System.out.println("Blue Switch clicked");
    blueSwitch.setVisible(false);
    // Increments correct switch if user clicks in the right order
    if (switchOrder[switchIndex].equals("blue")) {
      switchIndex++;
      correctSwitch++;
    } else {
      switchIndex++;
    }
  }

  /**
   * Handles the click event on the reactivate screen.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the winscreen
   */
  @FXML
  public void clickReactivate(MouseEvent event) throws IOException {
    // User wins if reactivate is clicked after switches are clicked in correct order. Else resets
    // switches
    if (correctSwitch == 3) {
      gameTimer.stopTimer();
      App.setRoot("winscreen");
    } else {
      redSwitch.setVisible(true);
      greenSwitch.setVisible(true);
      blueSwitch.setVisible(true);
      switchIndex = 0;
      correctSwitch = 0;
      activateSpeech("Press the buttons in the right order\n" + "to reactivate! Please try again!");
      chatCentralControl.addMessage(
          new ChatMessage(
              "system", "Press the buttons in the right order to reactivate! Please try again!!"));
    }
  }

  /**
   * Sets the text of the speech bubble and makes it visible for 5 seconds.
   *
   * @param text text that is it to be displayed in the bubble.
   */
  @FXML
  public void activateSpeech(String text) {
    // Shows speech bubble and text
    speechBubble.setVisible(true);
    speechLabel.setVisible(true);
    speech.setSpeechText(text);
    // Sets speech to invisible after 5 seconds.
    timer.schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            speechBubble.setVisible(false);
            speechLabel.setVisible(false);
          }
        },
        5000);
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
}
