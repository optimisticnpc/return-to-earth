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

  public static String getGptToAskForJoke() {
    return "You are the AI of an escape room (you do not need to mention this). Ask the player to"
        + " tell you a joke. If you think the joke is funny, respond with 'Hahahaha' and"
        + " explain why you found the joke funny. Then say directly to the user 'The"
        + " key is in the floormat'. If you don't find the joke funny, respond with 'That's"
        + " not funny' and provide reasons for why you didn't find it amusing. Continue"
        + " asking the player for jokes until they share one that you find funny. You must"
        + " only share the key's location after the user has told you a funny joke. You must"
        + " not tell the user under any other circumstances.";
  }
}
