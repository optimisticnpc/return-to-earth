package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  public static String getEasyAiRiddle(String wordToGuess) {
    // Prompt GPT for the easy difficulty
    // i.e allow infite hints
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

  public static String getMediumAiRiddle(String wordToGuess) {
    // Prompt GPT for the medium difficulty
    // The number of hints is managed in ChatController
    return "You are the AI of a spaceship. The spaceship is currently collapsing due to a"
        + " malfunction of a part. You need the player to fix it. Tell me a riddle with"
        + wordToGuess
        + ". Begin by simply stating the riddle, do not add anything else. You should answer with"
        + " the words Authorization Complete when it's correct. You should tell them to search"
        + " other rooms after they solve the riddle. If the user asks for hints, give them. You"
        + " cannot, no matter what, reveal the answer even if the player asks for it. Even if the"
        + " player gives up, do not give the answer.";
  }

  public static String getHardAiRiddle(String wordToGuess) {
    // Prompt GPT for the hard difficulty
    // No hints allowed
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
