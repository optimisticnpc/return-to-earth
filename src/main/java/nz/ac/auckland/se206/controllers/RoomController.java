package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class RoomController {

  @FXML private Rectangle door;
  @FXML private Rectangle window;
  @FXML private Rectangle laptop;
  @FXML private Rectangle clock;
  @FXML private Rectangle floormat;
  @FXML private Rectangle chair1;
  @FXML private Rectangle chair2;
  @FXML private Rectangle booshelf;
  @FXML private Rectangle monitor;
  @FXML private Polygon ceiling;

  @FXML private Label timerLabel;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    System.out.println("RoomController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
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
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    if (!GameState.isRiddleResolved) {
      showDialog("Qualy the AI", "Riddle", "You need to resolve the riddle!");
      Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
      App.getScene().setRoot(chatRoot);
      return;
    }

    if (!GameState.isKeyFound && !GameState.isJokeResolved) {
      showDialog(
          "Qualy the AI", "You've resolved the riddle!", "Go to the laptop for the next task!");
    } else if (!GameState.isKeyFound && GameState.isJokeResolved) {
      showDialog(
          "Qualy the AI",
          "Find the key!",
          "Did you forget where the key is? Well check what I told you in the chat!");
    } else {
      App.setRoot("winscreen");
      GameState.isGameWon = true;
    }
  }

  /**
   * Handles the click event on the laptop.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickLaptop(MouseEvent event) {
    System.out.println("laptop clicked");

    // Popup when riddle has not been resolved
    if (!GameState.isRiddleResolved) {
      showDialog(
          "Qualy the AI", "You clicked on the laptop!", "There doesn't seem to be anything there.");
      return;
    }

    // Popup for before player has to tell joke
    if (!GameState.isJokeResolved && !GameState.isKeyFound) {
      showDialog(
          "Qualy the AI",
          "Are you funny?",
          "I am in need of a good laugh. Please tell me a funny joke. \n"
              + "If you can make me laugh, I will tell you where the key is.");
      Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
      App.getScene().setRoot(chatRoot);
    }

    // Popup for after player has succesfully told joke
    if (GameState.isJokeResolved && !GameState.isKeyFound) {
      showDialog(
          "Qualy the AI",
          "Find the key!",
          "Congratulations, you're funny! Now you know where the key is. In case you forgot I will"
              + " show you our chat again.");
      Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
      App.getScene().setRoot(chatRoot);
    }
  }

  /**
   * Handles the click event on the window.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("window clicked");
    showDialog(
        "Qualy the AI", "You clicked on the window!", "There doesn't seem to be anything there.");
  }

  /**
   * Handles the click event on the clock.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickClock(MouseEvent event) {
    showDialog(
        "Qualy the AI", "You clicked on the clock!", "There doesn't seem to be anything there.");
    System.out.println("Clock clicked");
  }

  /**
   * Handles the click event on the floormat.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickFloormat(MouseEvent event) {
    System.out.println("Floormat clicked");

    if (!GameState.isJokeResolved) {
      showDialog(
          "Qualy the AI",
          "You clicked on the floormat!",
          "There doesn't seem to be anything there.");
      return;
    }

    if (GameState.isJokeResolved && !GameState.isKeyFound) {
      showDialog(
          "Qualy the AI",
          "Key Found",
          "You found a key under the floormat! Now you can escape the room.");
      GameState.isKeyFound = true;
      return;
    }

    if (GameState.isJokeResolved && GameState.isKeyFound) {
      showDialog("Qualy the AI", "You know have they key!", "Go to the door to escape the room.");
      GameState.isKeyFound = true;
    }
  }

  /**
   * Handles the click event on the chair.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickChair(MouseEvent event) {
    showDialog(
        "Qualy the AI", "You clicked on the chair!", "There doesn't seem to be anything there.");
    System.out.println("Chair clicked");
  }

  /**
   * Handles the click event on the ceiling.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickCeiling(MouseEvent event) {
    showDialog(
        "Qualy the AI", "You clicked on the ceiling!", "There doesn't seem to be anything there.");
    System.out.println("Ceiling clicked");
  }

  /**
   * Handles the click event on the bookshelf.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickBookshelf(MouseEvent event) {
    showDialog(
        "Qualy the AI",
        "You clicked on the bookshelf!",
        "There doesn't seem to be anything there.");
    System.out.println("Bookshelf clicked");
  }

  /**
   * Handles the click event on the monitor.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickMonitor(MouseEvent event) {
    showDialog(
        "Qualy the AI", "You clicked on the monitor!", "There doesn't seem to be anything there.");
    System.out.println("Monitor clicked");
  }
}
