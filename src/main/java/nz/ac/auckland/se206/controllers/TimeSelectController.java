package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class TimeSelectController {
  @FXML private ImageView twoMinsButton;
  @FXML private ImageView fourMinsButton;
  @FXML private ImageView sixMinsButton;

  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("TimeSelectController.initialize()");
  }

  @FXML
  public void clickTwoMinsButtonButton(MouseEvent event) {
    GameTimer.setInitialTime(120);
    changeToRoom();
  }

  @FXML
  public void clickFourMinsButtonButton(MouseEvent event) {
    GameTimer.setInitialTime(240);
    changeToRoom();
  }

  @FXML
  public void clickSixMinsButtonButton(MouseEvent event) {
    GameTimer.setInitialTime(360);
    changeToRoom();
  }

  public void changeToRoom() {
    Parent roomRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomRoot);
    GameTimer gameTimer = GameTimer.getInstance();
    // Start timer
    gameTimer.startTimer();
  }
}
