package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
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
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.AnimationCentralControl;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatBase;
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

  private ChatBase chatBase = new ChatBase();

  /** Called by JavaFX when controller is created (after elements have been initialized). */
  public void initialize() {
    System.out.println("JokeChatController.initialize()");
    hideLoadingIcon();
    disableTextBox();
    setupChatConfiguration();
    messages = new ArrayList<>();

    cheatCodes();

    // Add a listener to isJokeResolved property
    GameState.isJokeChallengeAccepted.addListener(
        (observable, oldValue, newValue) -> {
          if (!oldValue && newValue) { // If it changes from false to true
            startJokeChallenge();
            System.out.println("Joke challenge Started");
          }
        });
  }

  /** Starts the joke challenge by sending the first message from the assistant. */
  private void startJokeChallenge() {
    // Send the first message from the assistant
    ChatMessage prompt = new ChatMessage("system", GptPromptEngineering.getJokePrompt());
    ChatMessage firstMessage =
        new ChatMessage("assistant", GptPromptEngineering.getFirstJokeMessage());

    chatCompletionRequest.addMessage(prompt);
    chatCompletionRequest.addMessage(firstMessage);
    addLabel(GptPromptEngineering.getFirstJokeMessage(), Pos.CENTER_LEFT);
    enableTextBox();
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   */
  public void runGpt(ChatMessage msg) {
    // Play the loading animation
    AnimationCentralControl.getInstance().playAllAnimation();
    showLoadingIcon();
    disableTextBox();

    System.out.println("GPT LOADING");
    textToSpeech.stop();

    long startTime = System.currentTimeMillis(); // Record time

    // Create a new thread to run the GPT model
    Task<ChatMessage> callGptTask =
        new Task<>() {
          @Override
          public ChatMessage call() throws ApiProxyException {
            chatCompletionRequest.addMessage(msg);
            try {
              // Get the chat message from GPT and return it
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              chatBase.recordAndPrintTime(startTime);
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
                    AnimationCentralControl.getInstance().stopAllAnimation();
                    enableTextBox();
                  });

              e.printStackTrace();
              return null;
            }
          }
        };

    // get value and return it and check for hints
    callGptTask.setOnSucceeded(
        event -> {
          ChatMessage result = callGptTask.getValue();

          // Store the message in messageString variable
          if (result.getRole().equals("assistant")) {
            messageString = result.getContent();
          }

          addLabel(result.getContent(), Pos.CENTER_LEFT);
          // Added message to message list
          messages.add(result);
          hideLoadingIcon();
          AnimationCentralControl.getInstance().stopAllAnimation();

          // If the joke is resolved, disable the text box
          if (result.getRole().equals("assistant") && result.getContent().contains("Hahaha")) {
            GameState.isJokeResolved.set(true);
            System.out.println("Joke resolved");
            disableTextBox();
          } else {
            enableTextBox();
          }
        });

    // If GPT task fails show an alert and stop
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
          AnimationCentralControl.getInstance().stopAllAnimation();
          enableTextBox();
        });

    new Thread(callGptTask).start();
  }

  /** Sets up the chat configuration. */
  private void setupChatConfiguration() {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(0.5).setMaxTokens(100);
  }

  /**
   * Returns the chat completion request.
   *
   * @return the chat completion request
   */
  public ChatCompletionRequest getChatCompletionRequest() {
    return chatCompletionRequest;
  }

  /**
   * Sets the chat completion request.
   *
   * @param event the chat completion request
   */
  @FXML
  private void onEnterPressed(KeyEvent event) {
    // send the message when the enter key is pressed
    if (event.getCode() == KeyCode.ENTER) {
      sendButton.fire();
    }
  }

  /** Sets the send button action. */
  @FXML
  private void handleSendButtonAction() {
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
  private void handlePlayMessageAction() {
    chatBase.readMessage(messageString);
  }

  /**
   * Adds a label to the chat log.
   *
   * @param message the message to add
   * @param position the position of the message
   */
  private void addLabel(String message, Pos position) {
    ChatBase.addLabel(message, position, chatLog);
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

  /** Adds a message to the chat log when user enters a certain key combo. */
  private void cheatCodes() {
    // Sets the key combo
    KeyCombination keyCombB =
        new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN);
    // Prints messages if the key combo is pressed
    App.getScene()
        .addEventHandler(
            KeyEvent.KEY_PRESSED,
            event -> {
              if (keyCombB.match(event)) {
                System.out.println("Ctrl + Alt + B was pressed!");
                chatCompletionRequest.printMessages();
              }
            });
  }
}
