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
import nz.ac.auckland.ButtonOrder;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.SpeechBubble;

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
  @FXML private Rectangle redSwitch;
  @FXML private Rectangle greenSwitch;
  @FXML private Rectangle blueSwitch;
  @FXML private ImageView robot;
  @FXML private ImageView speechBubble;
  @FXML private Button reactivateButton;
  @FXML private ImageView wire;
  @FXML private ImageView wireImage;
  @FXML private Polygon reactivationHint;

  // private static String correctOrderString;

  private ButtonOrder buttonOrder = ButtonOrder.getInstance();
  private String[] switchOrder = buttonOrder.getCorrectOrderArray();
  private int switchIndex = 0;
  private int correctSwitch = 0;
  private CurrentScene currentScene = CurrentScene.getInstance();

  private SpeechBubble speech = SpeechBubble.getInstance();
  private Timer timer = new Timer();

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

    System.out.println("RoomOneController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    speechBubble.setVisible(false);
    speechLabel.setVisible(false);
    speechLabel.textProperty().bind(speech.speechDisplayProperty());
    // update hintlabel according to the difficulty
    HintCounter hintCounter = HintCounter.getInstance();
    hintCounter.setHintCount();
    hintLabel.textProperty().bind(hintCounter.hintCountProperty());

    String correctOrderString = buttonOrder.getCorrectOrderString();

    // Random random = new Random();

    // for (int i = 0; i < 3; i++) {
    //   int randomIndexToSwap = random.nextInt(switchOrder.length);
    //   String temp = switchOrder[randomIndexToSwap];
    //   switchOrder[randomIndexToSwap] = switchOrder[i];
    //   switchOrder[i] = temp;
    // }

    // correctOrderString = switchOrder[0] + " " + switchOrder[1] + " " + switchOrder[2];

    // TODO: Maybe remove later since we have cheat codes:
    System.out.println(correctOrderString);
  }

  /**
   * Handles the click event on the arrow to Room 2.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
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
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickRoomThree(MouseEvent event) throws IOException {
    System.out.println("Room Three clicked");
    Parent roomThreeRoot = SceneManager.getUiRoot(AppUi.ROOM_THREE);
    currentScene.setCurrent(3);
    App.getScene().setRoot(roomThreeRoot);
  }

  /**
   * Handles the click event on the main warning.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickMainWarning(MouseEvent event) throws IOException {
    System.out.println("Main Warning clicked");
    showSpeechBubble();
    speech.setSpeechText(
        "The system detected a critical damage.\n"
            + "Please authorise yourself by clicking \nthe middle screen"
            + " to access the system\nand analyse the damage.");
    setSpeechInvisible();
  }

  /**
   * Handles the click event on the engine warning.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickEngineWarning(MouseEvent event) throws IOException {
    System.out.println("Engine Warning clicked");
    if (!GameState.isRiddleResolved) {
      showSpeechBubble();
      speech.setSpeechText(
          "Authorisation Needed. \n You need to be authorised to access\n the system.");
    } else {
      showSpeechBubble();
      speech.setSpeechText(
          "Critical failure on the main engine"
              + "The main engine is damaged.\n"
              + " Please find the spare parts\n"
              + " and fix the engine!");
    }
  }

  @FXML
  public void clickWire(MouseEvent event) {
    GameState.isWireCollected = true;
    room.getChildren().remove(wire);
    room.getChildren().remove(wireImage);
  }

  @FXML
  public void clickClose(ActionEvent event) {
    backgroundScreen.getChildren().clear();
    room.getChildren().remove(backgroundScreen);
  }

  @FXML
  public void clickReactivationHint(MouseEvent event) {
    Parent reactivationRoot = SceneManager.getUiRoot(AppUi.REACTIVATION_ORDER);
    App.getScene().setRoot(reactivationRoot);
  }

  /**
   * Handles the click event on the red switch.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickRedSwitch(MouseEvent event) throws IOException {
    System.out.println("Red Switch clicked");
    if (GameState.isPartFixed) {
      redSwitch.setVisible(false);
      if (switchOrder[switchIndex].equals("red")) {
        switchIndex++;
        correctSwitch++;
      } else {
        switchIndex++;
      }
    }
  }

  /**
   * Handles the click event on the green switch.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickGreenSwitch(MouseEvent event) throws IOException {
    System.out.println("Green Switch clicked");
    if (GameState.isPartFixed) {
      greenSwitch.setVisible(false);
      if (switchOrder[switchIndex].equals("green")) {
        switchIndex++;
        correctSwitch++;
      } else {
        switchIndex++;
      }
    }
  }

  /**
   * Handles the click event on the blue switch.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickBlueSwitch(MouseEvent event) throws IOException {
    System.out.println("Blue Switch clicked");
    if (GameState.isPartFixed) {
      blueSwitch.setVisible(false);
      if (switchOrder[switchIndex].equals("blue")) {
        switchIndex++;
        correctSwitch++;
      } else {
        switchIndex++;
      }
    }
  }

  @FXML
  public void reactivate(ActionEvent event) throws IOException {
    if (GameState.isPartFixed) {
      if (correctSwitch == 3) {
        engineWarning.setVisible(false);
        reactivateButton.setVisible(false);
        App.setRoot("winscreen");
      } else {
        redSwitch.setVisible(true);
        greenSwitch.setVisible(true);
        blueSwitch.setVisible(true);
        switchIndex = 0;
        correctSwitch = 0;
      }
    }
  }

  /**
   * Makes the speech bubble and label invisible after 5 seconds. This is called when the speech
   * bubble is shown.
   */
  @FXML
  public void setSpeechInvisible() {
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

  /** Makes the speech bubble and label visible. This is called when the speech bubble is shown. */
  @FXML
  public void showSpeechBubble() {
    speechBubble.setVisible(true);
    speechLabel.setVisible(true);
  }

  /**
   * Handles the click event on the authorisation button.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickAuthorisation(MouseEvent event) throws IOException {
    System.out.println("Authorisation clicked");
    Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
    App.getScene().setRoot(chatRoot);
    GameState.isRoomOneFirst = false;
    currentScene.setCurrent(11);
  }
}
