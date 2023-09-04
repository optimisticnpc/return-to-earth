package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class StartController {
  @FXML private Label timerLabel;
  @FXML private Label displayLabel;
  @FXML private ImageView easyButton;
  @FXML private ImageView mediumButton;
  @FXML private ImageView hardButton;

  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("StartController.initialize()");
  }

  @FXML
  public void clickEasyButton(MouseEvent event) {
    GameState.medium = false;
    GameState.hard = false;
    GameState.easy = true;
    changeToTimeSelect();
  }

  @FXML
  public void clickMediumButton(MouseEvent event) {
    GameState.easy = false;
    GameState.hard = false;
    GameState.medium = true;
    changeToTimeSelect();
  }

  @FXML
  public void clickHardButton(MouseEvent event) {
    GameState.easy = false;
    GameState.medium = false;
    GameState.hard = true;
    changeToTimeSelect();
  }

  private void changeToTimeSelect() {
    Parent timeSelectRoot = SceneManager.getUiRoot(AppUi.TIME_SELECT);
    App.getScene().setRoot(timeSelectRoot);
  }
}
