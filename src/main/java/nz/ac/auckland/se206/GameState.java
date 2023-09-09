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

  /** Indicates the difficulties of the game */
  public static boolean easy = false;

  public static boolean medium = false;
  public static boolean hard = false;
  public static boolean isRoomOneFirst = true;
  public static boolean isJokeResolved = false;

  //** Indicates whether the player has solved the math questions and entered the correct passcode. */
  public static boolean isPasscodeSolved = false;

  //** Indicates whether that the player has clicked on the compartment and the toolbox is revealed. */
  public static boolean isToolboxRevealed = false;

  //** Indicates that the player has clicked on the tool box to collect it, the visual in the room will show the tool box as taken */
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
