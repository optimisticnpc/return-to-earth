package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class StartController {
  @FXML private Label timeLabel;
  @FXML private Label difficultyLabel;
  @FXML private Label helpLabel;
  @FXML private Button nextDifficultyButton;
  @FXML private Button prevDifficultyButton;
  @FXML private Button nextTimeButton;
  @FXML private Button prevTimeButton;
  @FXML private Button startButton;
  
  private int time = 120;
  private String[] difficulty = {"Easy", "Medium", "Hard"};
  private String[] difficultyHelp = {"- Infinite hints! -", "- 5 hints! -", "- 0 hints! -"};
  private int currDifficulty = 0;

  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("StartController.initialize()");
  }
  
    public void clickNextDifficultyButton() {
    if (this.currDifficulty < 2) {
      this.currDifficulty += 1;
      difficultyLabel.setText(difficulty[currDifficulty]);
      helpLabel.setText(difficultyHelp[currDifficulty]);
    }
  }

  @FXML
  public void clickPrevDifficultyButton() {
    if (this.currDifficulty > 0) {
      this.currDifficulty -= 1;
      difficultyLabel.setText(difficulty[currDifficulty]);
      helpLabel.setText(difficultyHelp[currDifficulty]);
    }
  }

  @FXML
  public void clickNextTimeButton() {
    if (this.time < 360) {
      this.time += 120;
      timeLabel.setText(Integer.toString(this.time) + " sec");
    }
  }

  @FXML
  public void clickPrevTimeButton() {
    if (this.time > 120) {
      this.time -= 120;
      timeLabel.setText(Integer.toString(this.time) + " sec");
    }

    // MIGHT NEED
//   @FXML
//   public void clickEasyButton(MouseEvent event) {
//     GameState.medium = false;
//     GameState.hard = false;
//     GameState.easy = true;
//     changeToTimeSelect();
//   }

//   @FXML
//   public void clickMediumButton(MouseEvent event) {
//     GameState.easy = false;
//     GameState.hard = false;
//     GameState.medium = true;
//     changeToTimeSelect();
//   }

//   @FXML
//   public void clickHardButton(MouseEvent event) {
//     GameState.easy = false;
//     GameState.medium = false;
//     GameState.hard = true;
//     changeToTimeSelect();
//   }

  @FXML
  public void clickStartButton() {
    changeToRoom();
  }

  public void changeToRoom() {
    // Need to change the difficulty here as well later
    GameTimer.setInitialTime(this.time);
    // Reset the game so the player can lose
    GameState.isGameWon = false;
    Parent roomRoot = SceneManager.getUiRoot(AppUi.ROOM_ONE);
    App.getScene().setRoot(roomRoot);
    GameTimer gameTimer = GameTimer.getInstance();

    gameTimer.startTimer();
  }
}
