package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** ChatControllerBase acts as a base for all chat components and controllers in the application. */
public class ChatBase {
  private TextToSpeech textToSpeech = new TextToSpeech();

  /** Initiates text-to-speech to read the message and manages the sound icon button state. */
  public void readMessage(String messageString) {
    // Create a new thread to read the message
    new Thread(
            () -> {
              try {
                // Read message, else print exception
                textToSpeech.speak(messageString);
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  /**
   * Records and prints the time taken by a GPT request.
   *
   * @param startTime The start time of the request.
   */
  public void recordAndPrintTime(long startTime) {
    long time = System.currentTimeMillis() - startTime;
    // Print time taken
    System.out.println();
    System.out.println("Search took " + time + "ms");
  }

  public static void addLabel(String message, Pos position, VBox chatLog) {
    // Creates a new box
    HBox box = new HBox();
    box.setAlignment(position);
    box.setPadding(new Insets(5, 5, 5, 10));

    // Adjusts font based on the position of the test
    Text text = new Text(message);
    if (position == Pos.CENTER_LEFT) {
      text.setFont(javafx.scene.text.Font.font("Arial", 15));
    } else if (position == Pos.CENTER_RIGHT) {
      text.setFont(javafx.scene.text.Font.font("Comic Sans MS", 15));
    } else if (position == Pos.CENTER) {
      text.setFont(javafx.scene.text.Font.font("Franklin Gothic Medium", 15));
    }
    TextFlow textFlow = new TextFlow(text);

    // Adjust background colour based on position of the text
    if (position == Pos.CENTER_LEFT) {
      textFlow.setStyle("-fx-background-color: rgb(255,242,102);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER_RIGHT) {
      textFlow.setStyle("-fx-background-color: rgb(255,255,255);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER) {
      textFlow.setStyle("-fx-background-color: rgb(255,117,128);" + "-fx-background-radius: 20px");
    }

    textFlow.setPadding(new Insets(5, 10, 5, 10));

    // Adds box to chatlog
    box.getChildren().add(textFlow);
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            chatLog.getChildren().add(box);
          }
        });
  }

  public static Choice getGptMessage(ChatCompletionRequest chatCompletionRequest)
      throws ApiProxyException {
    // Get the chat message from GPT and return it
    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
    Choice result = chatCompletionResult.getChoices().iterator().next();
    chatCompletionRequest.addMessage(result.getChatMessage());
    return result;
  }

  public static void showApiAlert() {
    // Show an alert dialog or some other notification to the user.
    new Alert(
            Alert.AlertType.ERROR,
            "An error occurred while communicating with the OpenAI's servers."
                + " Please check your API key and internet connection and then"
                + " reload the game.")
        .showAndWait();
  }
}
