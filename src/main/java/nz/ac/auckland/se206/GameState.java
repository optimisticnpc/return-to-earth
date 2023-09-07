package nz.ac.auckland.se206;

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
}
