package nz.ac.auckland.se206;

/**
 * The CurrentScene class keeps track of the current scene and provides methods for setting and
 * retrieving the current scene identifier. This class is implemented as a singleton to ensure a
 * single instance throughout the application.
 */
public class CurrentScene {

  /** Singleton instance of the CurrentScene class. */
  private static CurrentScene instance = null;

  /**
   * Retrieves the singleton instance of the CurrentScene class.
   *
   * @return The singleton instance of the CurrentScene class.
   */
  public static CurrentScene getInstance() {
    if (instance == null) {
      instance = new CurrentScene();
    }
    return instance;
  }

  /** The identifier of the current scene. */
  private int current;

  /**
   * Sets the identifier of the current scene.
   *
   * @param current The identifier of the current scene.
   */
  public void setCurrent(int current) {
    this.current = current;
  }

  /**
   * Retrieves the identifier of the current scene.
   *
   * @return The identifier of the current scene.
   */
  public int getCurrent() {
    return this.current;
  }
}
