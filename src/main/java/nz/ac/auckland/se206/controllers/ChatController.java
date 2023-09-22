package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CurrentScene;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.HintCounter;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
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

  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private ImageView loadingIcon;
  @FXML private ImageView soundIcon;
  @FXML private Label timerLabel;
  @FXML private Label hintLabel;
  @FXML private ImageView robot;
  @FXML private ImageView robotThinking;

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
    timerLabel.textProperty().bind(gameTimer.timeDisplayProperty());

    hintCounter.setHintCount();
    hintLabel.textProperty().bind(hintCounter.hintCountProperty());

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(0.5).setMaxTokens(120);

    textToSpeech = new TextToSpeech();

    Random random = new Random();
    wordToGuess = riddles[random.nextInt(riddles.length)];

    // Add a click event to the soundIcon so that the message is read when it is clicked
    soundIcon.setOnMouseClicked(e -> readMessage());

    if (!GameState.isRiddleResolved) {
      if (GameState.easy) {
        runGpt(new ChatMessage("user", GptPromptEngineering.getEasyAiRiddle(wordToGuess)));
      } else if (GameState.medium) {
        runGpt(new ChatMessage("user", GptPromptEngineering.getMediumAiRiddle(wordToGuess)));
      } else if (GameState.hard) {
        runGpt(new ChatMessage("user", GptPromptEngineering.getHardAiRiddle(wordToGuess)));
      }
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

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    // Display the user as "You" and the AI as "Qualy the AI".
    String displayRole;
    switch (msg.getRole()) {
      case "user":
        displayRole = "You";
        break;
      case "assistant":
        displayRole = "AI";
        break;
      default:
        displayRole = msg.getRole(); // default to the original role if not user or assistant
        break;
    }
    chatTextArea.appendText(displayRole + ": " + msg.getContent() + "\n\n");
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
              showAiThinking();
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              fade.setNode(robotThinking);
              fade.setDuration(Duration.millis(300));
              fade.setInterpolator(Interpolator.LINEAR);
              fade.setFromValue(1);
              fade.setToValue(0);
              fade.play();
              fade.setOnFinished(e -> robotThinking.setVisible(false));
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
          appendChatMessage(result);
          if (GameState.phaseTwo || GameState.phaseThree || GameState.phaseFour) {
            chatTextArea.setText("");
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

    appendChatMessage(msg);
    runGpt(msg);
  }

  @FXML
  public void showAiThinking() {
    // Fade in the thinking image in 0.3s
    robotThinking.setVisible(true);
    FadeTransition fade = new FadeTransition();
    fade.setNode(robotThinking);
    fade.setDuration(Duration.millis(300));
    fade.setInterpolator(Interpolator.LINEAR);
    fade.setFromValue(0);
    fade.setToValue(1);
    fade.play();
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
    int current = currentScene.getCurrent();
    AppUi room = AppUi.ROOM_ONE;
    if (current == 11) {
      currentScene.setCurrent(1);
    } else if (current == 12) {
      room = AppUi.ROOM_TWO;
      currentScene.setCurrent(2);
    } else {
      room = AppUi.ROOM_THREE;
      currentScene.setCurrent(3);
    }

    if (GameState.isRiddleResolved && GameState.phaseTwo && !GameState.hard) {
      System.out.println("Phase 2");
      runGpt(new ChatMessage("user", GptPromptEngineering.getphaseTwoProgress()));
    } else if (GameState.isRiddleResolved && GameState.phaseTwo && GameState.hard) {
      System.out.println("Phase 2(Hard)");
      runGpt(new ChatMessage("user", GptPromptEngineering.getHardphaseTwoProgress()));
    }
    Parent roomRoot = SceneManager.getUiRoot(room);
    App.getScene().setRoot(roomRoot);
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
