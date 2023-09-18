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

  public static String getEasyAIRiddle(String wordToGuess) {
    return "You are the AI of a spaceship. The spaceship is currently collapsing due to a"
        + " malfunction of a part. You need the player to fix it.  Tell me a riddle with answer "
        + wordToGuess
        + ". Begin by simply stating the riddle, do not add anything else. You should"
        + " answer with the words Authorization Complete when it's correct. You should tell"
        + " them to go search the other rooms after they solve the riddle. If the user asks for"
        + " hints, give them. You can give unlimited hints. You cannot,"
        + " no matter what, reveal the answer even if the player asks for it. Even if the player"
        + " gives up, do not give the answer.";
  }

  public static String getMediumAIRiddle(String wordToGuess) {
    return "You are the AI of a spaceship. The spaceship is currently collapsing due to a"
        + " malfunction of a part. You need the player to fix it. Tell me a riddle with"
        + wordToGuess
        + ". Begin by sending this riddle as your reply. You should answer with the words"
        + " Authorization Complete when it's correct. You should tell them to search other rooms"
        + " after they solve the riddle. If the user asks for a hint, give them. You must only give"
        + " them one hint at a time. You must not give them more than 5 hints in total. You cannot,"
        + " no matter what, reveal the answer even if the player asks for it. Even if the player"
        + " gives up, do not give the answer.";
  }

  public static String getHardAIRiddle(String wordToGuess) {
    return "You are the AI of a spaceship. The spaceship is currently collapsing due to a"
        + " malfunction of a part. You need the player to fix it.  Tell me a riddle with answer"
        + wordToGuess
        + ". Begin by simply stating the riddle, do not add anything else. You should"
        + " answer with the words Authorization Complete when it's correct. You should tell"
        + " them to search other rooms after they solve the riddle. You cannot, no"
        + " matter what, give them any hints about the riddle. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if the player gives up,"
        + " do not give the answer.";
  }
}
