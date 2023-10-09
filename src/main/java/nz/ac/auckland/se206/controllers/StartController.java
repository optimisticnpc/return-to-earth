package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.OxygenMeter;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/**
 * The StartController class controls the behavior and interactions of the start screen. It allows
 * the player to select game difficulty and time settings, view the score screen, and start the
 * game.
 */
public class StartController {

  private static int timeSettingSeconds;

  /**
   * Gets the selected time setting in seconds.
   *
   * @return The time setting in seconds.
   */
  public static int getTimeSettingSeconds() {
    return timeSettingSeconds;
  }

  @FXML private Label timeLabel;
  @FXML private Label difficultyLabel;
  @FXML private Label helpLabel;
  @FXML private Button nextDifficultyButton;
  @FXML private Button prevDifficultyButton;
  @FXML private Button nextTimeButton;
  @FXML private Button prevTimeButton;
  @FXML private Button startButton;

  private CurrentScene currentScene = CurrentScene.getInstance();
  private int currDifficulty = 0;
  private String[] difficulty = {"Easy", "Medium", "Hard"};
  private String[] difficultyHelp = {"- Infinite hints! -", "- 5 hints! -", "- 0 hints! -"};

  // Default time setting currently set at 4 mins
  private int timeSetting = 1;

  private String[] timeStrings = {"2 min", "4 min", "6 min"};

  /**
   * Initializes the start screen, setting the default time setting and difficulty.
   *
   * @throws ApiProxyException If there is an issue with the TextToSpeech API.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("StartController.initialize()");
  }

  /**
   * Handles the action when the "Next Difficulty" button is clicked, cycling through difficulty
   * levels.
   */
  @FXML
  private void onClickNextDifficultyButton() {
    if (this.currDifficulty < 2) {
      this.currDifficulty += 1;
      difficultyLabel.setText(difficulty[currDifficulty]);
      helpLabel.setText(difficultyHelp[currDifficulty]);
    }
  }

  /**
   * Handles the action when the "Previous Difficulty" button is clicked, cycling through difficulty
   * levels.
   */
  @FXML
  private void onClickPrevDifficultyButton() {
    if (this.currDifficulty > 0) {
      this.currDifficulty -= 1;
      difficultyLabel.setText(difficulty[currDifficulty]);
      helpLabel.setText(difficultyHelp[currDifficulty]);
    }
  }

  /** Handles the action when the "Next Time" button is clicked, cycling through time settings. */
  @FXML
  private void onClickNextTimeButton() {
    if (this.timeSetting < 2) {
      this.timeSetting += 1;
      timeLabel.setText(timeStrings[timeSetting]);
    }
  }

  /**
   * Handles the action when the "Previous Time" button is clicked, cycling through time settings.
   */
  @FXML
  private void onClickPrevTimeButton() {
    if (this.timeSetting > 0) {
      this.timeSetting -= 1;
      timeLabel.setText(timeStrings[timeSetting]);
    }
  }

  /**
   * Handles the action when the "Score Screen" button is clicked, allowing the player to view the
   * score screen.
   */
  @FXML
  private void onClickScoreScreenButton() {
    Parent scoreParent = SceneManager.getUiRoot(AppUi.SCORE_SCREEN);
    App.getScene().setRoot(scoreParent);
  }

  /**
   * Handles the action when the "Start" button is clicked, starting the game with the selected
   * settings.
   *
   * @throws IOException If there is an issue with starting the game.
   */
  @FXML
  private void onClickStartButton() throws IOException {
    // Need to change the difficulty here as well later
    timeSettingSeconds = (timeSetting + 1) * 120;
    GameTimer.setInitialTime(timeSettingSeconds);
    // Reset the game so the player can lose
    GameState.isGameWon = false;
    // Initialise the chat depending on the difficulty
    if (this.currDifficulty == 0) {
      GameState.easy = true;
      GameState.medium = false;
      GameState.hard = false;
    } else if (this.currDifficulty == 1) {
      GameState.easy = false;
      GameState.medium = true;
      GameState.hard = false;
    } else if (this.currDifficulty == 2) {
      GameState.easy = false;
      GameState.medium = false;
      GameState.hard = true;
    }
    SceneManager.addUi(AppUi.CHAT, App.loadFxml("chat"));
    SceneManager.addUi(AppUi.BACKGROUND, App.loadFxml("background"));
    SceneManager.addUi(AppUi.ROOM_ONE, App.loadFxml("roomone"));
    SceneManager.addUi(AppUi.ROOM_TWO, App.loadFxml("roomtwo"));
    SceneManager.addUi(AppUi.ROOM_THREE, App.loadFxml("roomthree"));
    SceneManager.addUi(AppUi.ROOM_ONE_FINAL, App.loadFxml("roomonefinal"));

    Parent roomRoot = SceneManager.getUiRoot(AppUi.BACKGROUND);
    currentScene.setCurrent(1);
    App.getScene().setRoot(roomRoot);
    GameTimer gameTimer = GameTimer.getInstance();
    OxygenMeter oxygenMeter = OxygenMeter.getInstance();
    Sound sound = Sound.getInstance();

    gameTimer.startTimer();
    oxygenMeter.startTimer();
    sound.resetSoundProperty();
  }
}
