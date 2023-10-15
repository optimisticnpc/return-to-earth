package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GlobalController;
import nz.ac.auckland.se206.controllers.PasscodeController;
import nz.ac.auckland.se206.controllers.RoomOneController;
import nz.ac.auckland.se206.controllers.ScoreScreenController;
import nz.ac.auckland.se206.controllers.SpacesuitPuzzleController;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  public static Parent loadFxml(final String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    Parent root = loader.load();
    Object controller = loader.getController();

    if (controller instanceof MyControllers) {
      MyControllers myController = (MyControllers) controller;
      SceneManager.addController(root, myController);
    }
    return root;
  }

  public static Scene getScene() {
    return scene;
  }

  /**
   * Resets all the rooms in the app by reloading the associated FXML files.
   *
   * @throws IOException If the file is not found.
   */
  public static void resetRooms() throws IOException {
    // Re initialize all the rooms the need to be reset every round
    try {
      SceneManager.addUi(AppUi.PASSCODE, loadFxml("passcode"));
      SceneManager.addUi(AppUi.BACKGROUND, loadFxml("background"));
      SceneManager.addUi(AppUi.ROOM_ONE, loadFxml("roomone"));
      SceneManager.addUi(AppUi.ROOM_TWO, loadFxml("roomtwo"));
      SceneManager.addUi(AppUi.ROOM_THREE, loadFxml("roomthree"));
      SceneManager.addUi(AppUi.ROOM_ONE_FINAL, App.loadFxml("roomonefinal"));
      SceneManager.addUi(AppUi.SPACESUIT_PUZZLE, loadFxml("spacesuitpuzzle"));
      SceneManager.addUi(AppUi.REACTIVATION_ORDER, loadFxml("reactivationorder"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Resets the math questions by using the MathQuestionSelector's setNewMathQuestions method and
   * reloads the question pages.
   *
   * @throws IOException If the file is not found.
   */
  public static void resetMathQuestions() throws IOException {

    try {

      SceneManager.addUi(AppUi.PASSCODE, loadFxml("passcode"));
      MathQuestionSelector selector = MathQuestionSelector.getInstance();
      selector.setNewMathQuestions();
      PasscodeController.setCorrectPassCodeString(selector.generatePasscode());

      // Reset the question scenes after we have chosen the new questions
      SceneManager.addUi(AppUi.QUESTION_ONE, loadFxml("questionone"));
      SceneManager.addUi(AppUi.QUESTION_TWO, loadFxml("questiontwo"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {

    // NOTE: All the other rooms get initialized at start button press so that chat is inialized
    // After difficulty is chosen

    // These rooms are only initialized once:
    SceneManager.addUi(AppUi.START, loadFxml("start"));
    SceneManager.addUi(AppUi.SCORE_SCREEN, loadFxml("scorescreen"));

    Parent root = SceneManager.getUiRoot(AppUi.START);
    scene = new Scene(root, 1280, 720);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
    new GlobalController(); // Create a new global controller which checks for time being up

    // Get math questions and set passcode
    MathQuestionSelector selector = MathQuestionSelector.getInstance();
    PasscodeController.setCorrectPassCodeString(selector.generatePasscode());

    // CHEATCODES
    cheatCodes();

    // Ensure app exits cleanly when window is closed
    // This stops any threads or services that the app is using
    stage.setOnCloseRequest(
        e -> {
          Platform.exit();
          System.exit(0);
        });
  }

  /**
   * Handles the combination key pressed to automatically win, lose or show the user all the
   * answers.
   */
  private void cheatCodes() {

    KeyCombination keyCombL =
        new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);
    KeyCombination keyCombW =
        new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);
    KeyCombination keyCombA =
        new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);
    KeyCombination keyCombR =
        new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);
    KeyCombination keyCombT =
        new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);
    KeyCombination keyCombC =
        new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);
    KeyCombination keyCombShiftR =
        new KeyCodeCombination(
            KeyCode.R, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN); // Alt + Shift + R
    KeyCombination keyCombV =
        new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);

    scene.addEventHandler(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (keyCombL.match(event)) {
            System.out.println("Ctrl + Alt + L was pressed!");
            // Instantly lose
            try {
              setRoot("losescreen");
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else if (keyCombW.match(event)) {
            System.out.println("Ctrl + Alt + W was pressed!");
            // Instantly win
            GameState.isGameWon = true;
            try {
              setRoot("winscreen");
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else if (keyCombA.match(event)) {
            System.out.println("Ctrl + Alt + A was pressed!");
            // Get answers for all puzzles

            // Riddle:
            System.out.println("Riddle: " + RoomOneController.getWordToGuess());

            // Passcode:
            System.out.println("Code: " + PasscodeController.getCorrectPassCodeString());

            // Spacesuit:
            System.out.println("Spacesuit: " + SpacesuitPuzzleController.getCorrectWordString());

            // Reactivation:
            ButtonOrder buttonOrder = ButtonOrder.getInstance();
            System.out.println("Order: " + buttonOrder.getCorrectOrderString());

          } else if (keyCombR.match(event)) {
            System.out.println("Ctrl + Alt + R was pressed!");
            System.out.println("Riddle skipped!");
            // Automatically skip riddles
            // TODO: Implement this properly + check implementation
            GameState.isRiddleResolved = true;

          } else if (keyCombT.match(event)) {
            System.out.println("Ctrl + Alt + T was pressed!");
            System.out.println("Passcode skipped!");
            // Automatically skip tools
            // TODO: Implement this properly + check implementation
            GameState.isPasscodeSolved = true;

          } else if (keyCombShiftR.match(event)) {
            System.out.println("Alt + Shift + R was pressed!");
            ScoreScreenController.updateFastestTimes();
          } else if (keyCombC.match(event)) {
            System.out.println("Ctrl + Alt + C was pressed!" + '\n');
            ChatCentralControl.getInstance().printChatCompletionRequestMessages();
          } else if (keyCombV.match(event)) {
            System.out.println("Ctrl + Alt + V was pressed!" + '\n');
            ChatCentralControl.getInstance().printChatPanelMessages();
          }
        });
  }
}
