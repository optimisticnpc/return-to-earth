package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
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
    changeToTimeSelect();
  }

  @FXML
  public void clickMediumButton(MouseEvent event) {
    changeToTimeSelect();
  }

  @FXML
  public void clickHardButton(MouseEvent event) {
    changeToTimeSelect();
  }

  private void changeToTimeSelect() {
    Parent timeSelectRoot = SceneManager.getUiRoot(AppUi.TIME_SELECT);
    App.getScene().setRoot(timeSelectRoot);
  }
}
