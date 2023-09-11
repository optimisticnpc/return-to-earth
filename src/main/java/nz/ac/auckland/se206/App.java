package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GlobalController;
import nz.ac.auckland.se206.controllers.PasscodeController;

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
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    SceneManager.addUi(AppUi.ROOM_ONE, loadFxml("roomone"));
    SceneManager.addUi(AppUi.ROOM_TWO, loadFxml("roomtwo"));
    SceneManager.addUi(AppUi.ROOM_THREE, loadFxml("roomthree"));
    SceneManager.addUi(AppUi.PASSCODE, loadFxml("passcode"));
    SceneManager.addUi(AppUi.CHAT, loadFxml("chat"));
    SceneManager.addUi(AppUi.START, loadFxml("start"));
    SceneManager.addUi(AppUi.QUESTION_ONE, loadFxml("questionone"));
    SceneManager.addUi(AppUi.QUESTION_TWO, loadFxml("questiontwo"));
    SceneManager.addUi(AppUi.CHAT, loadFxml("chat"));
    Parent root = SceneManager.getUiRoot(AppUi.START);
    scene = new Scene(root, 600, 470);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
    new GlobalController(); // Create a new global controller which checks for time being up

    // Get math questions and set passcode
    MathQuestionSelector selector = MathQuestionSelector.getInstance();
    PasscodeController.setCorrectPassCodeString(selector.generatePasscode());

    // Ensure app exits cleanly when window is closed
    // This stops any threads or services that the app is using
    stage.setOnCloseRequest(
        e -> {
          Platform.exit();
          System.exit(0);
        });
  }

  public static Scene getScene() {
    return scene;
  }

  public static void resetChat() {
    try {
      SceneManager.addUi(AppUi.CHAT, loadFxml("chat"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void resetRoomTwo() throws IOException {
    try {
      SceneManager.addUi(AppUi.ROOM_TWO, loadFxml("roomtwo"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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
}
