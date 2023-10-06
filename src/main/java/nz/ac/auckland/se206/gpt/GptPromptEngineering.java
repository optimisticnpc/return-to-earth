package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  public static String getAIPersonality() {
    return "You are the AI system of this spaceship and your name is Space Destroyer. You are"
        + " responsible to get the spaceship back to the Earth safely. Unfortunately, one of"
        + " the spaceship's parts is malfunctioning and you need the user to fix it. You"
        + " deperately need the user's help.";
  }

  public static String getEasyHintSetup() {
    return "If the user asks for hints, give them. You can give unlimited hints";
  }

  public static String getMediumHintSetup() {
    return " If the user asks for any help, you should answer with the word Hint, and give them a"
        + " hint";
  }

  public static String getHardHintSetup() {
    return "You cannot, no matter what, give the user any hints.";
  }

  public static String getaRiddle(String wordToGuess) {
    // Prompt GPT for the easy difficulty
    // i.e allow infite hints
    return "Tell me a riddle with answer "
        + wordToGuess
        + ". Begin by simply stating the riddle, do not add anything else. You should"
        + " answer with the words Authorization Complete when the answer is correct and tell"
        + " them to go search the other rooms. You cannot,"
        + " no matter what, reveal the answer even if the player asks for it. Even if the player"
        + " gives up, do not give the answer.";
  }

  public static String getphaseTwoProgress() {
    return "The user has to find a toolbox in the storage room. If the user asks for hints, tell"
        + " them that they have to find 4 digit passcodes to open the tool compartment and"
        + " obtain the toolbox inside, do not add anything else.";
  }

  public static String getHardphaseTwoProgress() {
    return "Now, The user has to find a toolbox in the storage room, which will be helpful to fix"
        + " the malfunctioning part of the spaceship. If the user asks for help, simply tell"
        + " them to find the tool box in the storage room.";
  }

  public static String getphaseThreeProgress() {
    return "The user has to fix the engine outside the spaceship. If the user asks for hints,"
        + " tell them that they have to"
        + " unlock the hatch and reconnect the wire inside the hatch, do not add anything"
        + " else.";
  }

  public static String getHardphaseThreeProgress() {
    return "The user now has to fix the malfunctioning engine from outside the spaceship with the"
        + " tools that they just found. If the user asks for help, simply tell them to fix"
        + " the engine from outside the spaceship with the tools aquired. Do not add"
        + " anything else.";
  }

  public static String getphaseFourProgress() {
    return "The user has to reactivate the engine in the main room. If the user asks for hints,"
        + " tell them that they have to"
        + " click the switches in the correct order to reactivate the engine. Do not add"
        + " anything else.";
  }

  public static String getHardphaseFourProgress() {
    return "The user has to reactivate the engine in the main room. If the user asks for hints,"
        + " simply tell them that they have to reactivate the engine from the main room. Do"
        + " not add anything else.";
  }
}
