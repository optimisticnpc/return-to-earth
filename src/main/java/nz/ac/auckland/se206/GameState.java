package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.scene.Parent;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  public static boolean isGameWon = false;

  public static boolean isJokeResolved = false;

  public static boolean isPasscodeSolved = false;

  public static boolean isToolboxRevealed = false;

  public static boolean isToolboxCollected = false;

  public static void resetGame() throws IOException {
    // TODO: Update and double check this
    // Reset all game state variables
    isRiddleResolved = false;
    isPasscodeSolved = false;
    isToolboxRevealed = false;
    isToolboxCollected = false;

    // Get new math puzzles and update room visuals
    App.resetMathQuestions();
    App.resetRoomTwo();

    Parent startRoot = SceneManager.getUiRoot(AppUi.START);
    App.getScene().setRoot(startRoot);
  }
}
