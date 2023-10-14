package nz.ac.auckland.se206.gpt;

import javafx.geometry.Pos;
import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MathQuestionSelector;
import nz.ac.auckland.se206.controllers.SpacesuitPuzzleController;

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
        + " toolbox inside, do not add anything else.";
  }

  public static String getHardPhaseTwoProgress() {
    return "Now, The user has to find a toolbox in the storage room, which will be helpful to fix"
        + " the malfunctioning part of the spaceship. If the user asks for help, simply tell"
        + " them to find the tool box in the storage room.";
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
    return hintMathQuestionSetup() + hintQuestionOneSetup() + hintQuestionTwoSetup();
  }

  public static String hintMathQuestionSetup() {

    return "The user has two questions they need to solve. They may ask for hints. ";
  }

  // "If the user asks you for a hint for the questions, ask them if it is the first or"
  // + " second question if they have not specified. "

  public static String hintQuestionOneSetup() {

    return "If it is for the first question tell them that: "
        + MathQuestionSelector.getInstance().getFirstQuestionHint();
  }

  public static String hintQuestionTwoSetup() {

    return "If it is for the second question tell them that: "
        + MathQuestionSelector.getInstance().getSecondQuestionHint();
  }

  public static String hintWordScrambleSetup() {

    return "If the user asks you for a hint for the word scramble give them a hint for the answer"
        + " \""
        + SpacesuitPuzzleController.getCorrectWordString()
        + "\". ";
  }


  // Used for hint buttons

  public static boolean checkIfAuthorisedAndPrintMessage() {
  if (!GameState.isAuthorising) {
      ChatMessage message = new ChatMessage("system", "You need to be authorised to talk to Space Destroyer for security"
              + " reasons.\nPlease click the middle screen in the main control room to authorise"
              + " yourself.");
      ChatCentralControl.getInstance().addMessage(message);
      return true;
    }
return false;
  }
}
