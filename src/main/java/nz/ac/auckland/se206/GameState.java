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
  public static boolean easy = true;

  public static boolean medium = false;
  public static boolean hard = false;

  public static boolean isRoomOneFirst = true;

  public static boolean isWireCollected = false;

  // ** Indicates whether the player has solved the math questions and entered the correct passcode.
  // */
  public static boolean isPasscodeSolved = false;

  // ** Indicates whether that the player has clicked on the compartment and the toolbox is
  // revealed. */
  public static boolean isToolboxRevealed = false;

  // ** Indicates that the player has clicked on the tool box to collect it, the visual in the room
  // will show the tool box as taken */
  public static boolean isToolboxCollected = false;

  public static boolean isSpacesuitUnlocked = false;

  public static boolean isSpacesuitRevealed = false;

  public static boolean isSpacesuitCollected = false;

  public static boolean isSpacesuitJustCollected = false;

  public static boolean isPartFixed = false;

  // ** Indicates if the game is launched first time for setting up of the AI.*/
  public static boolean isSetup = true;

  // ** indicates each phase of the game the player is in */
  public static boolean phaseTwo = false;
  public static boolean phaseThree = false;
  public static boolean phaseFour = false;

  public static void resetGame() throws IOException {
    // TODO: Update and double check this
    // Reset all game state variables
    isSetup = true;
    isRiddleResolved = false;
    isWireCollected = false;
    isPasscodeSolved = false;
    isToolboxRevealed = false;
    isToolboxCollected = false;
    isSpacesuitUnlocked = false;
    isSpacesuitRevealed = false;
    isSpacesuitCollected = false;
    isSpacesuitJustCollected = false;
    isPartFixed = false;

    // Get new math puzzles and update room visuals
    App.resetMathQuestions();
    App.resetRooms();
    App.resetChat();
    // ChatCentralControl.resetChatCentralControl();
    HintCounter.resetHintCount();

    Parent startRoot = SceneManager.getUiRoot(AppUi.START);
    App.getScene().setRoot(startRoot);
  }
}
