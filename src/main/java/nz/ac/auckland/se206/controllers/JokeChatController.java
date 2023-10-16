package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Controller class for the chat view. */
public class JokeChatController {

  @FXML private ImageView loadingIcon;
  @FXML private ImageView soundIcon;
  @FXML private TextArea inputText;
  @FXML private VBox chatLog;
  @FXML private ScrollPane scrollPane;
  @FXML private Button sendButton;
  @FXML private Button playMessage;

  private ChatCompletionRequest chatCompletionRequest;

  private String messageString = "";

  private List<ChatMessage> messages = new ArrayList<>();

  private TextToSpeech textToSpeech = new TextToSpeech();

  public void initialize() {
    System.out.println("JokeChatController.initialize()");
    hideLoadingIcon();
    setupChatConfiguration();
    messages = new ArrayList<>();

    ChatMessage prompt = new ChatMessage("system", GptPromptEngineering.getJokePrompt());
    ChatMessage firstMessage =
        new ChatMessage("assistant", GptPromptEngineering.getFirstJokeMessage());

    chatCompletionRequest.addMessage(prompt);
    chatCompletionRequest.addMessage(firstMessage);
    addLabel(GptPromptEngineering.getFirstJokeMessage(), Pos.CENTER_LEFT);

    cheatCodes();
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public void runGpt(ChatMessage msg) {
    showLoadingIcon();
    disableTextBox();

    System.out.println("GPT LOADING");
    textToSpeech.stop();

    long startTime = System.currentTimeMillis(); // Record time

    Task<ChatMessage> callGptTask =
        new Task<>() {
          @Override
          public ChatMessage call() throws ApiProxyException {
            chatCompletionRequest.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              recordAndPrintTime(startTime);
              return result.getChatMessage();
            } catch (ApiProxyException e) {
              Platform.runLater(
                  () -> {
                    // Show an alert dialog or some other notification to the user.
                    new Alert(
                            Alert.AlertType.ERROR,
                            "An error occurred while communicating with the OpenAI's servers."
                                + " Please check your API key and internet connection and then"
                                + " reload the game.")
                        .showAndWait();
                    hideLoadingIcon();
                    enableTextBox();
                  });

              e.printStackTrace();
              return null;
            }
          }
        };

    callGptTask.setOnSucceeded(
        event -> {
          ChatMessage result = callGptTask.getValue();

          // Store the message in messageString variable
          if (result.getRole().equals("assistant")) {
            messageString = result.getContent();
          }

          if (result.getRole().equals("assistant") && result.getContent().contains("Hahaha")) {
            GameState.isJokeResolved = true;
            System.out.println("Joke resolved");
            inputText.setDisable(true);
          }

          addLabel(result.getContent(), Pos.CENTER_LEFT);
          // Added message to message list
          messages.add(result);
          hideLoadingIcon();
          enableTextBox();
        });

    callGptTask.setOnFailed(
        event -> {
          Platform.runLater(
              () -> {
                // Show an alert dialog or some other notification to the user.
                new Alert(
                        Alert.AlertType.ERROR,
                        "An error occurred while communicating with the OpenAI's servers. Please"
                            + " check your API key and internet connection and then reload the"
                            + " game.")
                    .showAndWait();
              });
          hideLoadingIcon();
          enableTextBox();
        });

    new Thread(callGptTask).start();
  }

  /** Initiates text-to-speech to read the message and manages the sound icon button state. */
  public void readMessage() {
    // Create a new thread to read the message
    new Thread(
            () -> {
              try {
                textToSpeech.speak(messageString);

              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  protected void recordAndPrintTime(long startTime) {
    long time = System.currentTimeMillis() - startTime;
    System.out.println();
    System.out.println("Search took " + time + "ms");
  }

  public List<ChatMessage> getMessages() {
    return Collections.unmodifiableList(messages);
  }

  private void setupChatConfiguration() {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(0.5).setMaxTokens(100);
  }

  public ChatCompletionRequest getChatCompletionRequest() {
    return chatCompletionRequest;
  }

  @FXML // send the message when the enter key is pressed
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
      sendButton.fire();
    }
  }

  @FXML
  public void setSendButtonAction() {
    String message = inputText.getText().replaceAll("[\n\r]", ""); // Remove all newline characters
    inputText.clear();
    if (message.trim().isEmpty()) {
      return;
    }
    addLabel(message, Pos.CENTER_RIGHT);
    ChatMessage msg = new ChatMessage("user", message);
    runGpt(msg);
  }

  /** PlayMessageAction plays the last message written by GPT. */
  @FXML
  public void playMessageAction() {
    readMessage();
  }

  public void addLabel(String message, Pos position) {
    HBox hBox = new HBox();
    hBox.setAlignment(position);
    hBox.setPadding(new Insets(5, 5, 5, 10));

    Text text = new Text(message);
    if (position == Pos.CENTER_LEFT) {
      text.setFont(javafx.scene.text.Font.font("Arial", 15));
    } else if (position == Pos.CENTER_RIGHT) {
      text.setFont(javafx.scene.text.Font.font("Comic Sans MS", 15));
    } else if (position == Pos.CENTER) {
      text.setFont(javafx.scene.text.Font.font("Franklin Gothic Medium", 15));
    }
    TextFlow textFlow = new TextFlow(text);

    if (position == Pos.CENTER_LEFT) {
      textFlow.setStyle("-fx-background-color: rgb(255,242,102);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER_RIGHT) {
      textFlow.setStyle("-fx-background-color: rgb(255,255,255);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER) {
      textFlow.setStyle("-fx-background-color: rgb(255,117,128);" + "-fx-background-radius: 20px");
    }

    textFlow.setPadding(new Insets(5, 10, 5, 10));

    hBox.getChildren().add(textFlow);
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            chatLog.getChildren().add(hBox);
          }
        });
  }

  public void showLoadingIcon() {
    loadingIcon.setVisible(true);
  }

  public void hideLoadingIcon() {
    loadingIcon.setVisible(false);
  }

  public void enableTextBox() {
    inputText.setDisable(false);
  }

  public void disableTextBox() {
    inputText.setDisable(true);
  }

  private void cheatCodes() {

    KeyCombination keyCombJ =
        new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);

    App.getScene()
        .addEventHandler(
            KeyEvent.KEY_PRESSED,
            event -> {
              if (keyCombJ.match(event)) {
                System.out.println("Ctrl + Alt + J was pressed!");
                chatCompletionRequest.printMessages();
              }
            });
  }
}
