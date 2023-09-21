package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
    START,
    CHAT,
    ROOM_ONE,
    ROOM_TWO,
    ROOM_THREE,
    PASSCODE,
    QUESTION_ONE,
    QUESTION_TWO,
    SPACESUIT_PUZZLE,
    REACTIVATION_ORDER,
    ROOM_ONE_FINAL
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
