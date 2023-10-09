package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

  public static ChatCentralControl getInstance() {
    if (instance == null) {
      instance = new ChatCentralControl();
    }
    return instance;
  }

  public static String getWordToGuess() {
    return wordToGuess;
  }

  private ChatCompletionRequest chatCompletionRequest;

  private String messageString = "";

  private String[] riddles = {
    "blackhole", "star", "moon", "sun", "venus", "comet", "satellite", "mars"
  };

  private List<ChatMessage> messages = new ArrayList<>();
  private List<Observer> observers = new ArrayList<>();

  private CurrentScene currentScene = CurrentScene.getInstance();

  private ChatCentralControl() {
    System.out.println("ChatCentralControl Iniatialized");

    setupChatConfiguration();
    selectRandomRiddle();

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

  private void notifyObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }

  private void runChatPromptBasedOnGameState() throws ApiProxyException {
    if (GameState.isSetup) {
      runGpt(new ChatMessage("user", GptPromptEngineering.getAIPersonality()));
      GameState.isSetup = false;
    } else if (!GameState.isRiddleResolved) {
      runGpt(new ChatMessage("user", GptPromptEngineering.getRiddle(wordToGuess)));
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
          // if (GameState.medium) {
          // if (result.getRole().equals("assistant") && result.getContent().startsWith("Hint")) {
          //   int count = countOccurrences("hint", messageString.toLowerCase());
          //   if (hintCounter.getMediumHintCount() > 0) {
          //     if (hintCounter.getMediumHintCount() - count >= 0) {
          //       hintCounter.decrementHintCount(count);
          //     } else {
          //       result =
          //           new ChatMessage(
          //               "AI",
          //               "You can only ask for"
          //                   + " "
          //                   + hintCounter.getMediumHintCount()
          //                   + " "
          //                   + "more hints.");
          // }
          // } else {
          //   result = new ChatMessage("AI", "You cannot get any more hints.");
          // }
          // }
          // }

          // Added message to message list
          messages.add(result);

          // TODO: Find best location for this
          notifyObservers();
          System.out.println("RESULT for class:  " + this + "\n" + result.getContent());

          // addLabel(result.getContent(), Pos.CENTER_LEFT);

          // if (GameState.phaseTwo || GameState.phaseThree || GameState.phaseFour) {
          //   // clear the contents in VBOX
          //   chatLog.getChildren().clear();
          //   GameState.phaseTwo = false;
          //   GameState.phaseThree = false;
          //   GameState.phaseFour = false;
          // }
          if (result.getRole().equals("assistant")
              && result.getContent().startsWith("Authorization Complete")) {
            GameState.isRiddleResolved = true;
            GameState.phaseTwo = true;
            System.out.println("Riddle resolved");
          }

          // loadingIcon.setVisible(false); // hide the loading icon

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
          // loadingIcon.setVisible(false); // hide the loading icon
        });

    new Thread(callGptTask).start();
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
        new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(0.5).setMaxTokens(120);
  }

  private void selectRandomRiddle() {
    Random random = new Random();
    wordToGuess = riddles[random.nextInt(riddles.length)];
  }
}
