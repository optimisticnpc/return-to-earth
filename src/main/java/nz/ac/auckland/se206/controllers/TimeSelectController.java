package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class TimeSelectController {
  @FXML private ImageView twoMinsButton;
  @FXML private ImageView fourMinsButton;
  @FXML private ImageView sixMinsButton;

  @FXML
  public void initialize() throws ApiProxyException {}

  @FXML
  public void clickTwoMinsButtonButton(MouseEvent event) {
    changeToRoom();
  }

  @FXML
  public void clickFourMinsButtonButton(MouseEvent event) {
    changeToRoom();
  }

  @FXML
  public void clickSixMinsButtonButton(MouseEvent event) {
    changeToRoom();
  }

  public void changeToRoom() {
    Parent roomRoot = SceneManager.getUiRoot(AppUi.ROOM);
    App.getScene().setRoot(roomRoot);
  }
}
