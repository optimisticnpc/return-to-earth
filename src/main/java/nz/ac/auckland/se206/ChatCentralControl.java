package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Controller class for the chat view. */
public class ChatCentralControl {

  private static String wordToGuess;

  private static ChatCentralControl instance;
  private static List<Observer> observers = new ArrayList<>();

  /**
   * Retrieves the singleton instance of ChatCentralControl.
   *
   * @return The ChatCentralControl instance.
   */
  public static ChatCentralControl getInstance() {
    if (instance == null) {
      instance = new ChatCentralControl();
    }
    return instance;
  }

  /**
   * Retrieves the word to guess in the authorization riddle.
   *
   * @return The word to guess.
   */
  public static String getWordToGuess() {
    return wordToGuess;
  }

  private HintCounter hintCounter = HintCounter.getInstance();

  private ChatCompletionRequest chatCompletionRequest;

  private String messageString = "";

  private List<ChatMessage> messages = new ArrayList<>();

  private TextToSpeech textToSpeech = new TextToSpeech();

  private ChatBase chatBase = new ChatBase();

  /** Private constructor to enforce the singleton pattern. */
  private ChatCentralControl() {
    initializeChatCentralControl();
  }

  /** Initializes the ChatCentralControl and sets up chat configuration. */
  public void initializeChatCentralControl() {
    System.out.println("ChatCentralControl Iniatialized");
    // Setup the configuration of the chat
    setupChatConfiguration();

    observers = new ArrayList<>();
    messages = new ArrayList<>();

    try {
      runChatPromptBasedOnGameState();
    } catch (ApiProxyException e) {
      // Handle the exception and provide feedback if needed.
      e.printStackTrace();
    }
  }

  /**
   * Adds an observer to the list of observers to notify about chat messages.
   *
   * @param observer The observer to add.
   */
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  /**
   * Removes an observer from the list of observers.
   *
   * @param observer The observer to remove.
   */
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  public void addMessageButDontUpdate(ChatMessage message) {
    messages.add(message);
  }

  /**
   * Adds a chat message to the message list and notifies observers.
   *
   * @param message The chat message to add.
   */
  public void addMessage(ChatMessage message) {
    messages.add(message);
    notifyObservers();
  }

  /** Notifies all registered observers about new chat messages. */
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }

  /** Shows loading icons for all observers. */
  private void showAllLoadingIcons() {
    for (Observer observer : observers) {
      observer.showLoadingIcon();
    }
  }

  /** Hides loading icons for all observers. */
  private void hideAllLoadingIcons() {
    for (Observer observer : observers) {
      observer.hideLoadingIcon();
    }
  }

  /** Clears the contents of the chat messages for all observers. */
  private void clearContentsOfChats() {
    for (Observer observer : observers) {
      observer.clearContents();
    }
  }

  /** Enables text input boxes for all observers. */
  private void enableAllTextBoxes() {
    for (Observer observer : observers) {
      observer.enableTextBox();
    }
  }

  /** Disables text input boxes for all observers. */
  private void disableAllTextBoxes() {
    for (Observer observer : observers) {
      observer.disableTextBox();
    }
  }

  /**
   * Runs the chat prompt based on the game state, thereby changing the phase prompts in the game.
   * Depending on the current game state, this method sends specific chat prompts to the AI to
   * advance the game's storyline or provide assistance.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy.
   */
  private void runChatPromptBasedOnGameState() throws ApiProxyException {
    // If the game is just starting, run the personality setup prompt
    if (GameState.isPersonalitySetup) {
      System.out.println("System setup completed!");
      runGpt(new ChatMessage("system", GptPromptEngineering.getAiPersonality()));
      GameState.isPersonalitySetup = false;
    }
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
    showAllLoadingIcons();
    disableAllTextBoxes();

    System.out.println("GPT LOADING");
    textToSpeech.stop();

    long startTime = System.currentTimeMillis(); // Record time

    // Create a new task to run the GPT model
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
                    hideAllLoadingIcons();
                    AnimationCentralControl.getInstance().stopAllAnimation();
                    enableAllTextBoxes();
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
          // If medium difficulty and the player asked for a hint, decrement the hint count.
          if (GameState.medium) {
            if (result.getRole().equals("assistant") && result.getContent().startsWith("Hint")) {
              int count = countOccurrences("hint", messageString.toLowerCase());
              if (hintCounter.getMediumHintCount() > 0) {
                if (hintCounter.getMediumHintCount() - count >= 0) {
                  hintCounter.decrementHintCount(count);
                } else {
                  result =
                      new ChatMessage(
                          "assistant",
                          "You can only ask for"
                              + " "
                              + hintCounter.getMediumHintCount()
                              + " "
                              + "more hints.");

                  // Remove what GPT returned and replace it with our message
                  chatCompletionRequest.removeLastMessage();
                  chatCompletionRequest.addMessage(result);
                }
              } else {
                result = new ChatMessage("assistant", "You cannot get any more hints.");

                // Remove what GPT returned and replace it with our message
                chatCompletionRequest.removeLastMessage();
                chatCompletionRequest.addMessage(result);
                disableAllHintButtons();
              }
            }
          }

          // if GPT responds with 'authorization complete', move to phase 2
          if (result.getRole().equals("assistant")
              && result.getContent().startsWith("Authorization Complete")) {

            GameState.isRiddleResolved = true;
            GameState.isPhaseChange.set(true);
            System.out.println("Riddle resolved");
            System.out.println("Phase 2");

            // after 5 seconds send a message regarding the second phase so GPT knows player's
            // progress
            new java.util.Timer()
                .schedule(
                    new java.util.TimerTask() {
                      @Override
                      public void run() {
                        Platform.runLater(
                            () -> {
                              if (GameState.hard) {
                                runGpt(
                                    new ChatMessage(
                                        "user",
                                        GptPromptEngineering.getHardPhaseTwoProgress()
                                            + GptPromptEngineering.getHardHintReminder()));
                              } else {
                                runGpt(
                                    new ChatMessage(
                                        "user", GptPromptEngineering.getPhaseTwoProgress()));
                              }
                            });
                      }
                    },
                    5000);
          }

          if (GameState.isPhaseChange.getValue()) {
            messages.clear();
            clearContentsOfChats();
          }
          // Added message to message list
          messages.add(result);
          notifyObservers();

          if (GameState.isPhaseChange.getValue()) {
            GameState.isPhaseChange.set(false);

            // 5 second delay
            new java.util.Timer()
                .schedule(
                    new java.util.TimerTask() {
                      @Override
                      public void run() {
                        Platform.runLater(
                            () -> {
                              hideAllLoadingIcons();
                              AnimationCentralControl.getInstance().stopAllAnimation();
                              enableAllTextBoxes();
                            });
                      }
                    },
                    5000);
          } else {
            // Otherwise, hide loading animation
            hideAllLoadingIcons();
            AnimationCentralControl.getInstance().stopAllAnimation();
            enableAllTextBoxes();
          }
        });

    // If failed, notify user
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
          enableAllTextBoxes();
          hideAllLoadingIcons();
          AnimationCentralControl.getInstance().stopAllAnimation();
        });

    new Thread(callGptTask).start();
  }

  /** Disables hint buttons for all relevant question panels. */
  private void disableAllHintButtons() {
    // Disable hint buttons for q1 q2 and word scramble
    SceneManager.getController(SceneManager.getUiRoot(AppUi.QUESTION_ONE)).disableHintButton();
    SceneManager.getController(SceneManager.getUiRoot(AppUi.QUESTION_TWO)).disableHintButton();
    SceneManager.getController(SceneManager.getUiRoot(AppUi.WORD_SCRAMBLE)).disableHintButton();
  }

  /**
   * Count the number of occurrences of a given word in the sentence.
   *
   * @param word a string word that is compared against words in sentence
   * @param sentence a string sentence that is taken apart to be compared
   * @return
   */
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

  /** Sends a message string to chatBase so that chatBase can read the message using TTS. */
  public void readMessage() {
    chatBase.readMessage(messageString);
  }

  /**
   * Retrieves the list of chat messages.
   *
   * @return An unmodifiable list of chat messages.
   */
  public List<ChatMessage> getMessages() {
    return Collections.unmodifiableList(messages);
  }

  /** Sets up the chat configuration for GPT. */
  private void setupChatConfiguration() {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(0.5).setMaxTokens(100);
  }

  /**
   * Retrieves the ChatCompletionRequest used for GPT conversations.
   *
   * @return The ChatCompletionRequest.
   */
  public ChatCompletionRequest getChatCompletionRequest() {
    return chatCompletionRequest;
  }

  /** Prints all the messages in the request with their corresponding roles. */
  public void printChatCompletionRequestMessages() {
    chatCompletionRequest.printMessages();
  }

  /** Prints all the messages that should be displayed in the chat panel. */
  public void printChatPanelMessages() {
    // Print all messages
    for (ChatMessage message : messages) {
      System.out.println("-------------------------");
      System.out.println(message.getRole() + ": " + message.getContent());
    }
  }
}
