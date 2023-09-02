package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class StartController {
  @FXML private Label timeLabel;
  @FXML private Label difficultyLabel;
  @FXML private Button nextDifficultyButton;
  @FXML private Button prevDifficultyButton;
  @FXML private Button nextTimeButton;
  @FXML private Button prevTimeButton;
  @FXML private Button startButton;

  private int time = 120;
  private String[] difficulty = {"Easy", "Medium", "Hard"};
  private int currDifficulty = 0;

  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("StartController.initialize()");
  }

  @FXML
  public void clickNextDifficultyButton(MouseEvent event) {
    if (this.currDifficulty < 2) {
      this.currDifficulty += 1;
      difficultyLabel.setText(difficulty[currDifficulty]);
    }
  }

  @FXML
  public void clickPrevDifficultyButton(MouseEvent event) {
    if (this.currDifficulty > 0) {
      this.currDifficulty -= 1;
      difficultyLabel.setText(difficulty[currDifficulty]);
    }
  }

  @FXML
  public void clickNextTimeButton(MouseEvent event) {
    if (this.time < 360) {
      this.time += 120;
      timeLabel.setText(Integer.toString(this.time));
    }
  }

  @FXML
  public void clickPrevTimeButton(MouseEvent event) {
    if (this.time > 120) {
      this.time -= 120;
      timeLabel.setText(Integer.toString(this.time));
    }
  }

  @FXML
  public void clickStartButton(MouseEvent event) {
    changeToRoom();
  }

  public void changeToRoom() {
    // Reset the game so the player can lose
    GameTimer.setInitialTime(this.time);
    GameState.isGameWon = false;
    Parent roomRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomRoot);
    GameTimer gameTimer = GameTimer.getInstance();

    gameTimer.startTimer();
  }
}
