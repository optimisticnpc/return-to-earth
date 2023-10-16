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

  private TextToSpeech textToSpeech = new TextToSpeech();
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

  private void enableAllTextBoxes() {
    for (Observer observer : observers) {
      observer.enableTextBox();
    }
  }

  private void disableAllTextBoxes() {
    for (Observer observer : observers) {
      observer.disableTextBox();
    }
  }

  private void runChatPromptBasedOnGameState() throws ApiProxyException {
    if (GameState.isPersonalitySetup) {
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

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public void runGpt(ChatMessage msg) {
    AnimationCentralControl.getInstance().playAllAnimation();
    showAllLoadingIcons();
    disableAllTextBoxes();

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
                    hideAllLoadingIcons();
                    AnimationCentralControl.getInstance().stopAllAnimation();
                    enableAllTextBoxes();
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
            System.out.println("Phase 2");
            // after 3 seconds, clear the contents in VBOX and run phase 2

            if (GameState.hard) {
              runGpt(
                  new ChatMessage(
                      "user",
                      GptPromptEngineering.getHardPhaseTwoProgress()
                          + GptPromptEngineering.getHardHintReminder()));
            } else {
              runGpt(new ChatMessage("system", GptPromptEngineering.getPhaseTwoProgress()));
            }
          }

          // Added message to message list
          messages.add(result);
          notifyObservers();

          hideAllLoadingIcons();
          AnimationCentralControl.getInstance().stopAllAnimation();
          enableAllTextBoxes();
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
          enableAllTextBoxes();
          hideAllLoadingIcons();
          AnimationCentralControl.getInstance().stopAllAnimation();
        });

    new Thread(callGptTask).start();
  }

  private void disableAllHintButtons() {

    SceneManager.getController(SceneManager.getUiRoot(AppUi.QUESTION_ONE)).disableHintButton();
    SceneManager.getController(SceneManager.getUiRoot(AppUi.QUESTION_TWO)).disableHintButton();
    SceneManager.getController(SceneManager.getUiRoot(AppUi.WORD_SCRAMBLE)).disableHintButton();
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

  /** Prints all the messages in the request with their corresponding roles. */
  public void printChatCompletionRequestMessages() {
    chatCompletionRequest.printMessages();
  }

  /** Prints all the messages that should be displayed in the chat panel * */
  public void printChatPanelMessages() {
    for (ChatMessage message : messages) {
      System.out.println("-------------------------");
      System.out.println(message.getRole() + ": " + message.getContent());
    }
  }
}
