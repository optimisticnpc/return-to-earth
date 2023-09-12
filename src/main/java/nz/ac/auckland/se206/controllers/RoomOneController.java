package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class RoomOneController {

  @FXML private Rectangle mainWarning;
  @FXML private Rectangle authRectangle;
  @FXML private Rectangle engineWarning;
  @FXML private Polygon movetoRoomTwo;
  @FXML private Polygon movetoRoomThree;
  @FXML private Label timerLabel;
  @FXML private Rectangle redSwitch;
  @FXML private Rectangle greenSwitch;
  @FXML private Rectangle blueSwitch;
  private String[] switchOrder = {"red", "green", "blue"};
  private int switchIndex = 0;
  private int correctSwitch = 0;

  CurrentScene currentScene = CurrentScene.getInstance();
  @FXML private Button systemButton;
  @FXML private Button reactivateButton;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

    System.out.println("RoomOneController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    Random random = new Random();

    for (int i = 0; i < 3; i++) {
      int randomIndexToSwap = random.nextInt(switchOrder.length);
      String temp = switchOrder[randomIndexToSwap];
      switchOrder[randomIndexToSwap] = switchOrder[i];
      switchOrder[i] = temp;
    }
    System.out.println(switchOrder[0] + switchOrder[1] + switchOrder[2]);
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
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
    showDialog(
        "Warning!!!",
        "Critical failure",
        "The system detected a critical damage to the ship.\n"
            + "Please authorise yourself by clicking the middle screen\n"
            + "to access the system and analyse the damage.");
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
      showDialog(
          "Access Denied",
          "Authorisation needed",
          "You need to be authorised to access the system. Please click the middle screen to"
              + " authorise yourself.");
    } else {
      showDialog(
          "Warning!!!",
          "Critical failure on the main engine",
          "The main engine is damaged.\n"
              + " Please proceed to the storage room to find the spare parts.\n"
              + " And fix the engine!");
    }
  }

  @FXML
  public void accessSystem(ActionEvent event) throws IOException {
    if (!GameState.isRiddleResolved) {
      showDialog(
          "Access Denied",
          "Authorisation needed",
          "You need to be authorised to access the system.\nPlease click the middle screen to"
              + " authorise yourself.");
      ;
    } else {
      Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
      App.getScene().setRoot(chatRoot);
    }
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
    redSwitch.setVisible(false);
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
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickGreenSwitch(MouseEvent event) throws IOException {
    System.out.println("Green Switch clicked");
    greenSwitch.setVisible(false);
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
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickBlueSwitch(MouseEvent event) throws IOException {
    System.out.println("Blue Switch clicked");
    blueSwitch.setVisible(false);
    if (switchOrder[switchIndex].equals("blue")) {
      switchIndex++;
      correctSwitch++;
    } else {
      switchIndex++;
    }
  }

  @FXML
  public void reactivate(ActionEvent event) throws IOException {
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

  /**
   * Handles the click event on the authorisation button.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickAuthorisation(MouseEvent event) throws IOException {

    if (!GameState.isRiddleResolved && GameState.isRoomOneFirst) {
      showDialog(
          "AI",
          "Authorisation needed",
          "You need to be authorised to access the system. Please solve the provided riddle.");
      SceneManager.addUi(AppUi.CHAT, App.loadFxml("chat"));
    }
    Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
    App.getScene().setRoot(chatRoot);
    GameState.isRoomOneFirst = false;
  }
}
