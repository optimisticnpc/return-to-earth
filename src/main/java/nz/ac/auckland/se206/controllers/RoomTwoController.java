package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class RoomTwoController {
  @FXML private Label timerLabel;
  @FXML private Rectangle door;
  @FXML private Rectangle goBackRectangle;
  CurrentScene currentScene = CurrentScene.getInstance();

  public void initialize() {
    System.out.println("RoomTwoController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
    currentScene.setCurrent(2);
  }

  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    Parent roomThreeRoot = SceneManager.getUiRoot(AppUi.ROOM_THREE);
    App.getScene().setRoot(roomThreeRoot);
    currentScene.setCurrent(3);
  }

  @FXML
  public void clickGoBackRectangle(MouseEvent event) throws IOException {
    System.out.println("go back clicked");

    Parent roomOneRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomOneRoot);
    currentScene.setCurrent(1);
    
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
}
