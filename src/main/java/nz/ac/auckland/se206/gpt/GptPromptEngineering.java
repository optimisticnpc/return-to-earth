package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MathQuestionSelector;
import nz.ac.auckland.se206.controllers.WordScrambleController;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  public static String getAIPersonality() {
    return "You are the AI system of this spaceship and your name is Space Destroyer. You are"
        + " responsible to get the spaceship back to the Earth safely. Unfortunately, one of"
        + " the spaceship's parts is malfunctioning and you need the user to fix it. You"
        + " desperately need the user's help. Begin by simply introducing yourself. Do not"
        + " add anything else";
  }

  public static String getEasyHintSetup() {
    return "If the user asks for hints, give them. You can give unlimited hints";
  }

  public static String getMediumHintSetup() {
    return " If the user asks for any help, you should answer with the word Hint, and give them a"
        + " hint";
  }

  public static String getMediumHintReminder() {
    return "Remember if the user asks for any help at all, you start your answer with the word"
        + " Hint, and give them a hint";
  }

  public static String getHardHintSetup() {
    return "You cannot, no matter what, give the user any hints.";
  }

  public static String getHardHintReminder() {
    return "Remember, if the user asks about the math questions or word scramble question or"
        + " anything at all, you cannot, under any any circumstances give the user any"
        + " hints.";
  }

  public static String getRiddle(String wordToGuess) {
    // Prompt GPT for the easy difficulty
    // i.e allow infite hints
    return "The user has to authorise themselves by solving a riddle to access the system. Provide"
        + " a riddle with answer "
        + wordToGuess
        + ". Begin by simply stating the riddle, do not add anything else. You should answer with"
        + " the words Authorization Complete when the answer is correct and tell them to go search"
        + " the other rooms. When the answer is wrong, tell the user to try again. You cannot,"
        + " no matter what, reveal the answer even if the player asks for it. Even if the player"
        + " gives up, do not give the answer.";
  }

  public static String getPhaseTwoProgress() {
    return "The user has to find a toolbox in the storage room to find the right tool for"
        + " malfunctioning part. Simply tell them to search the storage room to find the"
        + " tool box. Do not add anything else. When the user asks for hints, tell them that"
        + " they have to find 4 digit passcodes to open the tool compartment and obtain the"
        + " toolbox inside, do not add anything else. Remember if the user asks for any help"
        + " at all, you start your answer with the word Hint, and give them a hint";
  }

  public static String getHardPhaseTwoProgress() {
    return "Now, The user has to find a toolbox in the storage room, which will be helpful to fix"
        + " the malfunctioning part of the spaceship. If the user asks for help, simply tell"
        + " them to find the tool box in the storage room. ";
  }

  /**
   * Generates a GPT prompt for phase three progress.
   *
   * @return A GPT prompt for phase three progress.
   */
  public static String getPhaseThreeProgress() {
    return "The user has to fix the engine outside the spaceship. Simply tell them thay they have"
        + " to go outside the spaceship to fix the engine. Do not add anything else. If the"
        + " user asks for hints, tell them that they have to unlock the hatch and reconnect"
        + " the wire inside the hatch, do not add anything else.";
  }

  /**
   * Generates a GPT prompt for hard phase three progress.
   *
   * @return A GPT prompt for hard phase three progress.
   */
  public static String getHardPhaseThreeProgress() {

    return "The user now has to fix the malfunctioning engine from outside the spaceship with the"
        + " tools that they just found. If the user asks for help, simply tell them to fix"
        + " the engine from outside the spaceship with the tools aquired. Do not add"
        + " anything else.";
  }

  /**
   * Generates a GPT prompt for phase four progress.
   *
   * @return A GPT prompt for phase four progress.
   */
  public static String getPhaseFourProgress() {
    return "The user has to reactivate the engine in the main room. Simply tell them to go back to"
        + " the main room for reactivation of the engine. Do not add anything else. If the"
        + " user asks for hints, tell them that they have to click the switches in the"
        + " correct order to reactivate the engine. Do not add anything else.";
  }

  /**
   * Generates a GPT prompt for hard phase four progress.
   *
   * @return A GPT prompt for hard phase four progress.
   */
  public static String getHardPhaseFourProgress() {

    return "The user has to reactivate the engine in the main room. If the user asks for hints,"
        + " simply tell them that they have to reactivate the engine from the main room. Do"
        + " not add anything else.";
  }

  public static String hintMathQuestionPrompt() {
    return hintMathQuestionSetup() + hintQuestionOneSetup() + "\n" + hintQuestionTwoSetup();
  }

  public static String hintMathQuestionSetup() {

    return "The user has two questions they need to solve. They may ask for hints. ";
  }

  // "If the user asks you for a hint for the questions, ask them if it is the first or"
  // + " second question if they have not specified. "

  public static String hintQuestionOneSetup() {

    return "If they ask for a hint for the first question tell them that: "
        + MathQuestionSelector.getInstance().getFirstQuestionHint()
        + "If they ask for a hint again you can tell them that the answer is: "
        + MathQuestionSelector.getInstance().getFirstAnswer();
  }

  public static String hintQuestionTwoSetup() {

    return "If it is for the second question tell them that: "
        + MathQuestionSelector.getInstance().getSecondQuestionHint()
        + "If they ask for a hint again you can tell them that the answer is: "
        + MathQuestionSelector.getInstance().getSecondAnswer();
  }

  public static String hintWordScrambleSetup() {

    return "If the user asks you for a hint for the word scramble give them a hint for the answer"
        + " \""
        + WordScrambleController.getCorrectWordString()
        + "\". ";
  }

  // Used for hint buttons

  public static boolean checkIfAuthorisedAndPrintMessage() {
    if (!GameState.isAuthorising) {
      ChatMessage message =
          new ChatMessage(
              "system",
              "You need to be authorised to talk to Space Destroyer for security reasons.\n"
                  + "Please click the middle screen in the main control room to authorise"
                  + " yourself.");
      ChatCentralControl.getInstance().addMessage(message);
      return true;
    }
    return false;
  }

  public static String getJokePrompt() {
    return "You are the AI system of this spaceship and your name is Space Destroyer (you do not"
        + " need to mention this, the user already knows you). You are responsible to get"
        + " the spaceship back to the Earth safely. Unfortunately, one of the spaceship's"
        + " parts is malfunctioning and you need the user to fix it.The player needs to"
        + " access an upgraded spacesuit as part of this mission. However only the most"
        + " witty and qualified people can access this suitAsk the player to tell you a"
        + " joke. If you think the joke is funny, respond with 'Hahahaha' and explain why"
        + " you found the joke funny. If you don't find the joke funny, respond with 'That's"
        + " not funny' and provide reasons for why you didn't find it amusing. Continue"
        + " asking the player for jokes until they share one that you find funny.";
  }

  public static String getFirstJokeMessage() {
    return "Well done, you have found the upgraded space suit. I need you to access the spacesuit."
        + " However, only the most witty and qualified individuals may use the super space"
        + " suit. Please tell me a funny joke and then I will let you access the spacesuit.";
  }
}
