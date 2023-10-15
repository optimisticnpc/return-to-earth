package nz.ac.auckland.se206;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Parent;

/**
 * The SceneManager class manages the mapping of different scenes or UI elements in a JavaFX
 * application. It allows you to add and retrieve scenes by their associated enum values.
 */
public class SceneManager {

  /** An enumeration of the different UI elements or scenes in the application. */
  public enum AppUi {
    START,
    SCORE_SCREEN,
    CHAT,
    ROOM_ONE,
    ROOM_TWO,
    ROOM_THREE,
    PASSCODE,
    QUESTION_ONE,
    QUESTION_TWO,
    SPACESUIT_PUZZLE,
    REACTIVATION_ORDER,
    ROOM_ONE_FINAL,
    BACKGROUND
  }

  /** A HashMap that stores the mapping of AppUi enums to their corresponding scenes. */
  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  /** A HashMap that stores the mapping of AppUi enums to their corresponding scene controllers. */
  private static Map<Parent, MyControllers> controllerMap = new HashMap<>();

  /**
   * Adds a scene to the scene map with the specified enum value and scene.
   *
   * @param appUi The enum value representing the UI element or scene.
   * @param uiRoot The root node of the UI element or scene.
   */
  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  /**
   * Retrieves the root node of a scene associated with the specified enum value.
   *
   * @param appUi The enum value representing the scene to retrieve.
   * @return The root node of the specified scene.
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  public static void addController(Parent uiRoot, MyControllers controller) {
    controllerMap.put(uiRoot, controller);
  }

  public static MyControllers getController(Parent uiRoot) {
    return controllerMap.get(uiRoot);
  }
}
