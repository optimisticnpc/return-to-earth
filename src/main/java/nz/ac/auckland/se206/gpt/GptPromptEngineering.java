package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "Tell me a riddle with answer"
        + wordToGuess
        + ". Begin by simply stating the riddle, do not add"
        + " anything else. You should answer with the word Correct when it's correct. If the"
        + " user asks for hints, give them. If users guess incorrectly, give hints. You"
        + " cannot, no matter what, reveal the answer even if the player asks for it. Even"
        + " if the player gives up, do not give the answer.";
  }

  public static String getEasyAI() {
    return "";
  }

  public static String getMediumAI() {
    return "You are the AI of a spaceship. There is a problem with one of the parts in the"
        + " spaceship, and you need the player to solve it. At first, The player has to be"
        + " authorised by solving a riddle from you. Tell me a riddle with answer black"
        + " hole. Begin by simply stating the riddle, do not add anything else. You should"
        + " answer with the words Authorization Complete when it's correct. You should tell"
        + " them to go to the storage room after they solve the riddle. If the user asks for"
        + " hints, give them. You can give up to"
        + "5 hints in total. You cannot, no matter what, give them more than 5 hints. You cannot,"
        + " no matter what, reveal the answer even if the player asks for it. Even if the player"
        + " gives up, do not give the answer.";
  }

  public static String getHardAI() {
    return "";
  }
}
