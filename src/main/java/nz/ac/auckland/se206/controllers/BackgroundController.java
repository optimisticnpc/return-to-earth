package nz.ac.auckland.se206.controllers;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class BackgroundController {

  @FXML private Button continueButton;
  @FXML private Label timerLabel;
  @FXML private Label hintLabel;
  @FXML private ImageView spaceship;

  private GameTimer gameTimer = GameTimer.getInstance();

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

    System.out.println("RoomOneController.initialize()");
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());
    // initially sets speech bubble to invisible.
    // update hintlabel according to the difficulty
    HintCounter hintCounter = HintCounter.getInstance();
    hintCounter.setHintCount();
    hintLabel.textProperty().bind(hintCounter.hintCountProperty());
    initializeTranslate();
  }

  @FXML
  private void initializeTranslate() {
    Platform.runLater(
        () -> {
          TranslateTransition translate = new TranslateTransition();
          translate.setNode(spaceship);
          translate.setByX(750);
          translate.setByY(750);
          translate.setInterpolator(Interpolator.EASE_BOTH);
          translate.setDuration(Duration.millis(10000));
          translate.play();
        });
  }

  /** Handles the action event when the continue button is clicked. */
  @FXML
  private void onContinue() {
    Parent roomRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomRoot);
  }
}
