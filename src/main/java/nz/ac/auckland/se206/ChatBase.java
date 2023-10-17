package nz.ac.auckland.se206;

import nz.ac.auckland.se206.speech.TextToSpeech;

/** ChatControllerBase acts as a base for all chat components and controllers in the application. */
public class ChatBase {
  private TextToSpeech textToSpeech = new TextToSpeech();

  /** Initiates text-to-speech to read the message and manages the sound icon button state. */
  public void readMessage(String messageString) {
    // Create a new thread to read the message
    new Thread(
            () -> {
              try {
                // Read message, else print exception
                textToSpeech.speak(messageString);
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  /**
   * Records and prints the time taken by a GPT request.
   *
   * @param startTime The start time of the request.
   */
  public void recordAndPrintTime(long startTime) {
    long time = System.currentTimeMillis() - startTime;
    // Print time taken
    System.out.println();
    System.out.println("Search took " + time + "ms");
  }
}
