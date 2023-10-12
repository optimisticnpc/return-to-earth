package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class ChatCentralControl {

  private static String wordToGuess;

  private static ChatCentralControl instance;
  private static List<Observer> observers = new ArrayList<>();

  public static ChatCentralControl getInstance() {
    if (instance == null) {
      instance = new ChatCentralControl();
    }
    return instance;
  }

  public static String getWordToGuess() {
    return wordToGuess;
  }

  private HintCounter hintCounter = HintCounter.getInstance();

  private ChatCompletionRequest chatCompletionRequest;

  private String messageString = "";

  private List<ChatMessage> messages = new ArrayList<>();

  private ChatCentralControl() {
    initializeChatCentralControl();
  }

  public void initializeChatCentralControl() {
    System.out.println("ChatCentralControl Iniatialized");

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

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  public void addMessage(ChatMessage message) {
    messages.add(message);
    notifyObservers();
  }

  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }

  private void showAllLoadingIcons() {
    for (Observer observer : observers) {
      observer.showLoadingIcon();
    }
  }

  private void hideAllLoadingIcons() {
    for (Observer observer : observers) {
      observer.hideLoadingIcon();
    }
  }

  private void clearContentsOfChats() {
    for (Observer observer : observers) {
      observer.clearContents();
    }
  }

  private void runChatPromptBasedOnGameState() throws ApiProxyException {
    if (GameState.isPersonalitySetup) {
      // TODO: TEMPORARY BYPASS bc idk why its not working
      //   System.out.println("PERSONALITY RUN");
      //   runGpt(new ChatMessage("system", GptPromptEngineering.getAIPersonality()));
      //   GameState.isSetup = false;
      // } else if (!GameState.isRiddleResolved) {
      System.out.println("System setup completed!");
      runGpt(new ChatMessage("system", GptPromptEngineering.getAIPersonality()));
      GameState.isPersonalitySetup = false;
    } else {
      if (GameState.phaseThree && !GameState.hard) {
        System.out.println("Phase 3");
        runGpt(new ChatMessage("user", GptPromptEngineering.getPhaseThreeProgress()));
      } else if (GameState.phaseThree && GameState.hard) {
        System.out.println("Phase 3(Hard)");
        runGpt(new ChatMessage("user", GptPromptEngineering.getHardPhaseThreeProgress()));
      } else if (GameState.phaseFour && !GameState.hard) {
        System.out.println("Phase 4");
        runGpt(new ChatMessage("user", GptPromptEngineering.getPhaseFourProgress()));
      } else if (GameState.phaseFour && GameState.hard) {
        System.out.println("Phase 4(Hard)");
        runGpt(new ChatMessage("user", GptPromptEngineering.getHardPhaseFourProgress()));
      }
    }
  }

  public void nextPhase() {
    if (GameState.isRiddleResolved && GameState.phaseTwo && !GameState.hard) {
      System.out.println("Phase 2");
      runGpt(new ChatMessage("user", GptPromptEngineering.getPhaseTwoProgress()));
    } else if (GameState.isRiddleResolved && GameState.phaseTwo && GameState.hard) {
      System.out.println("Phase 2(Hard)");
      runGpt(new ChatMessage("user", GptPromptEngineering.getHardPhaseTwoProgress()));
    }
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public void runGpt(ChatMessage msg) {
    showAllLoadingIcons();
    System.out.println("GPT LOADING");

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
                    hideAllLoadingIcons();
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

          if (GameState.phaseTwo || GameState.phaseThree || GameState.phaseFour) {
            // clear the contents in VBOX
            messages.clear();
            clearContentsOfChats();
            GameState.phaseTwo = false;
            GameState.phaseThree = false;
            GameState.phaseFour = false;
          }

          if (result.getRole().equals("assistant")
              && result.getContent().startsWith("Authorization Complete")) {
            GameState.isRiddleResolved = true;
            GameState.phaseTwo = true;
            System.out.println("Riddle resolved");
            // after 3 seconds, clear the contents in VBOX and run phase 2

            runGpt(new ChatMessage("system", GptPromptEngineering.getPhaseTwoProgress()));
          }

          // Added message to message list
          messages.add(result);
          // TODO: Find best location for this
          notifyObservers();

          // Added message to message list
          messages.add(result);
          // TODO: Find best location for this
          notifyObservers();

          hideAllLoadingIcons();

          // sendButton.setDisable(false); // Re-enable send button
          // inputText.setDisable(false); // Re-enable the input text area
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
          // inputText.setDisable(false); // Re-enable the input text area
          hideAllLoadingIcons();
        });

    new Thread(callGptTask).start();
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

  /** Prints all the messages in the request with their corresponding roles. */
  public void printChatCompletionRequestMessages() {
    chatCompletionRequest.printMessages();
  }
}
