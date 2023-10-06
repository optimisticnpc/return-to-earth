package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Controller class for the chat view. */
public class ChatController {

  private static String wordToGuess;

  public static String getWordToGuess() {
    return wordToGuess;
  }

  // @FXML private TextArea chatTextArea;
  // @FXML private TextField inputText;
  // @FXML private Button sendButton;
  @FXML private ImageView loadingIcon;
  @FXML private ImageView soundIcon;
  @FXML private Label timerLabel;
  @FXML private Label hintLabel;
  @FXML private ImageView robot;
  @FXML private ImageView robotThinking;
  @FXML private TextArea inputText;
  @FXML private VBox chatLog;
  @FXML private ScrollPane scrollPane;
  @FXML private Button sendButton;

  private FadeTransition fade = new FadeTransition();

  private TextToSpeech textToSpeech; // Text to speech object

  private ChatCompletionRequest chatCompletionRequest;

  private String messageString = "";

  private String[] riddles = {
    "blackhole", "star", "moon", "sun", "venus", "comet", "satellite", "mars"
  };

  private CurrentScene currentScene = CurrentScene.getInstance();

  private HintCounter hintCounter = HintCounter.getInstance();

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("ChatController.initialize()");
    GameTimer gameTimer = GameTimer.getInstance();

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(0.5).setMaxTokens(120);

    textToSpeech = new TextToSpeech();

    Random random = new Random();
    wordToGuess = riddles[random.nextInt(riddles.length)];

    // Add a click event to the soundIcon so that the message is read when it is clicked
    soundIcon.setOnMouseClicked(e -> readMessage());

    if (GameState.isSetup) {
      runGpt(new ChatMessage("user", GptPromptEngineering.getAIPersonality()));
      GameState.isSetup = false;
    } else if (!GameState.isRiddleResolved) {
      runGpt(new ChatMessage("user", GptPromptEngineering.getaRiddle(wordToGuess)));
    } else {
      if (GameState.phaseThree && !GameState.hard) {
        System.out.println("Phase 3");
        runGpt(new ChatMessage("user", GptPromptEngineering.getphaseThreeProgress()));
      } else if (GameState.phaseThree && GameState.hard) {
        System.out.println("Phase 3(Hard)");
        runGpt(new ChatMessage("user", GptPromptEngineering.getHardphaseThreeProgress()));
      } else if (GameState.phaseFour && !GameState.hard) {
        System.out.println("Phase 4");
        runGpt(new ChatMessage("user", GptPromptEngineering.getphaseFourProgress()));
      } else if (GameState.phaseFour && GameState.hard) {
        System.out.println("Phase 4(Hard)");
        runGpt(new ChatMessage("user", GptPromptEngineering.getHardphaseFourProgress()));
      }
    }
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
    }
    TextFlow textFlow = new TextFlow(text);

    if (position == Pos.CENTER_LEFT) {
      textFlow.setStyle("-fx-background-color: rgb(255,242,102);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER_RIGHT) {
      textFlow.setStyle("-fx-background-color: rgb(255,255,255);" + "-fx-background-radius: 20px");
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

  public void setSendButtonAction() {
    String message = inputText.getText().replaceAll("[\n\r]", "");
    inputText.clear();
    try {
      if (!message.isEmpty()) {
        // show message on the sending client window
        addLabel(message, Pos.CENTER_RIGHT);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    ChatMessage msg = new ChatMessage("user", message);
    runGpt(msg);
  }

  public VBox getChatLog() {
    return chatLog;
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private void runGpt(ChatMessage msg) {
    // Disable the input text area so the user cannot type while GPT is loading
    // Also disable send button
    inputText.setDisable(true);
    sendButton.setDisable(true);
    loadingIcon.setVisible(true); // show the loading icon
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
                    loadingIcon.setVisible(false); // hide the loading icon
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
          if (GameState.medium) {
            if (result.getRole().equals("assistant") && result.getContent().startsWith("Hint")) {
              int count = countOccurrences("hint", messageString.toLowerCase());
              if (hintCounter.getMediumHintCount() > 0) {
                if (hintCounter.getMediumHintCount() - count >= 0) {
                  hintCounter.decrementHintCount(count);
                } else {
                  result =
                      new ChatMessage(
                          "AI",
                          "You can only ask for"
                              + " "
                              + hintCounter.getMediumHintCount()
                              + " "
                              + "more hints.");
                }
              } else {
                result = new ChatMessage("AI", "You cannot get any more hints.");
              }
            }
          }
          addLabel(result.getContent(), Pos.CENTER_LEFT);
          if (GameState.phaseTwo || GameState.phaseThree || GameState.phaseFour) {
            // clear the contents in VBOX
            chatLog.getChildren().clear();
            GameState.phaseTwo = false;
            GameState.phaseThree = false;
            GameState.phaseFour = false;
          }
          if (result.getRole().equals("assistant")
              && result.getContent().startsWith("Authorization Complete")) {
            GameState.isRiddleResolved = true;
            GameState.phaseTwo = true;
            System.out.println("Riddle resolved");
          }

          loadingIcon.setVisible(false); // hide the loading icon

          sendButton.setDisable(false); // Re-enable send button
          inputText.setDisable(false); // Re-enable the input text area
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
          inputText.setDisable(false); // Re-enable the input text area
          loadingIcon.setVisible(false); // hide the loading icon
        });

    new Thread(callGptTask).start();
  }

  protected void recordAndPrintTime(long startTime) {
    long time = System.currentTimeMillis() - startTime;
    System.out.println();
    System.out.println("Search took " + time + "ms");
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    textToSpeech.stop(); // Stop the text to speech
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();
    ChatMessage msg = new ChatMessage("user", message);

    runGpt(msg);
  }

  @FXML // send the message when the enter key is pressed
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
      sendButton.fire();
    }
  }

  /** Count the number of occurrences of a given word in the sentence */
  private int countOccurrences(String word, String sentence) {
    // Split the sentence into an array of words
    String[] words = sentence.split("\\s+");

    // Initialize a counter
    int count = 0;

    // Iterate through the words and check for matches
    for (String w : words) {
      if (w.contains(word)) {
        count++;
      }
    }
    System.out.println(count);
    return count;
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    textToSpeech.stop(); // Stop the text to speech

    // TODO: Delete code
    // int current = currentScene.getCurrent();
    // AppUi room = AppUi.ROOM_ONE;
    // if (current == 11) {
    //   currentScene.setCurrent(1);
    // } else if (current == 12) {
    //   room = AppUi.ROOM_TWO;
    //   currentScene.setCurrent(2);
    // } else {
    //   room = AppUi.ROOM_THREE;
    //   currentScene.setCurrent(3);
    // }

    if (GameState.isRiddleResolved && GameState.phaseTwo && !GameState.hard) {
      System.out.println("Phase 2");
      runGpt(new ChatMessage("user", GptPromptEngineering.getphaseTwoProgress()));
    } else if (GameState.isRiddleResolved && GameState.phaseTwo && GameState.hard) {
      System.out.println("Phase 2(Hard)");
      runGpt(new ChatMessage("user", GptPromptEngineering.getHardphaseTwoProgress()));
    }
    // Parent roomRoot = SceneManager.getUiRoot(room);
    // App.getScene().setRoot(roomRoot);
  }

  private void readMessage() {
    // Disable the soundIcon button when the message starts to be read
    // Prevent the user from clicking the button multiple times
    soundIcon.setDisable(true);
    soundIcon.setOpacity(0.2);

    // Create a new thread to read the message
    new Thread(
            () -> {
              try {
                textToSpeech.speak(messageString);
              } catch (Exception e) {
                e.printStackTrace();
              }

              // Re-enable the soundIcon button when the message has been read
              Platform.runLater(() -> soundIcon.setDisable(false));
              Platform.runLater(() -> soundIcon.setOpacity(1.0));
            })
        .start();
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  public void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
