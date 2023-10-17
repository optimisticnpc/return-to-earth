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

  /**
   * Adds a labeled message to the chat log.
   *
   * @param message The message to display.
   * @param position The position of the message (e.g., Pos.CENTER_LEFT).
   * @param chatLog The VBox representing the chat log.
   */
  public static void addLabel(String message, Pos position, VBox chatLog) {
    // Creates a new box
    HBox box = new HBox();
    box.setAlignment(position);
    box.setPadding(new Insets(5, 5, 5, 10));

    // Adjusts font based on the position of the text
    Text text = new Text(message);
    if (position == Pos.CENTER_LEFT) {
      text.setFont(javafx.scene.text.Font.font("Arial", 15));
    } else if (position == Pos.CENTER_RIGHT) {
      text.setFont(javafx.scene.text.Font.font("Comic Sans MS", 15));
    } else if (position == Pos.CENTER) {
      text.setFont(javafx.scene.text.Font.font("Franklin Gothic Medium", 15));
    }
    TextFlow textFlow = new TextFlow(text);

    // Adjust background color based on the position of the text
    if (position == Pos.CENTER_LEFT) {
      textFlow.setStyle("-fx-background-color: rgb(255,242,102);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER_RIGHT) {
      textFlow.setStyle("-fx-background-color: rgb(255,255,255);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER) {
      textFlow.setStyle("-fx-background-color: rgb(255,117,128);" + "-fx-background-radius: 20px");
    }

    textFlow.setPadding(new Insets(5, 10, 5, 10));

    // Adds box to chat log
    box.getChildren().add(textFlow);
    Platform.runLater(
        () -> {
          chatLog.getChildren().add(box);
        });
  }

  /**
   * Gets a message from the GPT API and returns it as a Choice.
   *
   * @param chatCompletionRequest The ChatCompletionRequest to execute.
   * @return The Choice containing the GPT response.
   * @throws ApiProxyException if there is an error communicating with the API proxy.
   */
  public static Choice getGptMessage(ChatCompletionRequest chatCompletionRequest)
      throws ApiProxyException {
    // Get the chat message from GPT and return it
    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
    Choice result = chatCompletionResult.getChoices().iterator().next();
    chatCompletionRequest.addMessage(result.getChatMessage());
    return result;
  }

  /** Shows an API error alert to the user. */
  public static void showApiAlert() {
    // Show an alert dialog or some other notification to the user.
    new Alert(
            Alert.AlertType.ERROR,
            "An error occurred while communicating with the OpenAI's servers."
                + " Please check your API key and internet connection and then"
                + " reload the game.")
        .showAndWait();
  }

  private TextToSpeech textToSpeech = new TextToSpeech();

  /**
   * Initiates text-to-speech to read the message and manages the sound icon button state.
   *
   * @param messageString The message to be read.
   */
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
}
