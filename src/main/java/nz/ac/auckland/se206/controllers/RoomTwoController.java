package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class RoomTwoController {
  @FXML private Pane room;
  @FXML private Label timerLabel;
  @FXML private ImageView toolBoxOpenImage;
  @FXML private ImageView toolBoxCollectedImage;
  @FXML private ImageView spacesuitRevealedImage;
  @FXML private ImageView spacesuitCollectedImage;
  @FXML private Rectangle questionOne;
  @FXML private Rectangle questionTwo;
  @FXML private Polygon crate;
  @FXML private ImageView crateImage;
  @FXML private ImageView robot;

  private CurrentScene currentScene = CurrentScene.getInstance();

  public void initialize() {
    System.out.println("RoomTwoController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    // Make the overlay images not visible
    toolBoxOpenImage.setOpacity(0);
    toolBoxCollectedImage.setOpacity(0);
    spacesuitRevealedImage.setOpacity(0);
    spacesuitCollectedImage.setOpacity(0);
  }

  /**
   * Handles the click event on the authorisation button.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickAuthorisation(MouseEvent event) throws IOException {
    if (!GameState.isRiddleResolved) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Access Denied");
      alert.setHeaderText("Authorisation needed");
      alert.setContentText(
          "You need to be authorised to access the system.\nPlease click the middle screen to"
              + " authorise yourself.");
      alert.showAndWait();
    }
    Parent chatRoot = SceneManager.getUiRoot(AppUi.CHAT);
    App.getScene().setRoot(chatRoot);
    currentScene.setCurrent(12);
  }

  @FXML
  public void clickRoomOne(MouseEvent event) throws IOException {
    System.out.println("Room One Clicked");

    Parent roomOneRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomOneRoot);
    currentScene.setCurrent(1);
  }

  @FXML
  public void clickQuestionOne(MouseEvent event) throws IOException {
    Parent questionOneRoot = SceneManager.getUiRoot(AppUi.QUESTION_ONE);
    App.getScene().setRoot(questionOneRoot);
  }

  @FXML
  public void clickQuestionTwo(MouseEvent event) throws IOException {
    Parent questionTwoRoot = SceneManager.getUiRoot(AppUi.QUESTION_TWO);
    App.getScene().setRoot(questionTwoRoot);
  }

  @FXML
  public void clickCrate(MouseEvent event) throws InterruptedException {
    System.out.println("Crate clicked");

    if (GameState.isToolboxCollected) {
      FadeTransition fadeTransition = new FadeTransition();
      fadeTransition.setNode(crateImage);
      fadeTransition.setFromValue(1); // starting opacity value
      fadeTransition.setToValue(0); // ending opacity value (1 is fully opaque)
      fadeTransition.setDuration(Duration.millis(600)); // transition duration
      fadeTransition.setOnFinished(
          e -> {
            room.getChildren().remove(crate);
            room.getChildren().remove(crateImage);
          });
      fadeTransition.play();
    }
  }

  @FXML
  public void clickSpacesuit(MouseEvent event) throws IOException {
    System.out.println("Spacesuit Clicked");

    // If riddle is not solved, do no allow entry
    if (!GameState.isRiddleResolved) {
      // TODO: replace with speech bubble?
      // Placeholder
      ChatController.showDialog("Placeholder", "AUTHORIZATION NEEDED", "delete this later");
      return;
    }

    // If the scramble word puzzle hasn't been solved
    // Go to enter access key screen
    if (!GameState.isSpacesuitUnlocked) {
      Parent spacesuitPuzzlesRoom = SceneManager.getUiRoot(AppUi.SPACESUIT_PUZZLE);
      App.getScene().setRoot(spacesuitPuzzlesRoom);

      // If spacesuit hasn't been revealed
    } else if (!GameState.isSpacesuitRevealed) {
      revealSpacesuit();
      GameState.isSpacesuitRevealed = true;
    } else if (!GameState.isSpacesuitCollected) {
      collectSpacesuit();
      GameState.isSpacesuitCollected = true;
    }
  }

  @FXML
  public void clickToolCompartment(MouseEvent event) throws IOException {
    System.out.println("Tool Compartment Clicked");

    // TODO: Get rid of duplicate code here
    // If riddle is not solved, do no allow entry
    if (!GameState.isRiddleResolved) {
      // TODO: replace with speech bubble?
      // Placeholder
      ChatController.showDialog("Placeholder", "AUTHORIZATION NEEDED", "delete this later");
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
      System.out.println("Toolbox collected");
    }
  }

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
