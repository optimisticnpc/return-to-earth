package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;

/**
 * Controller class responsible for handling global game events and navigation. This class manages
 * the game timer and determines when to navigate to the lose screen.
 */
public class GlobalController {

  private final GameTimer gameTimer = GameTimer.getInstance();

  /**
   * Constructs a new instance of the GlobalController. Initializes an event listener on the game
   * timer to detect when the time is up. If the time is up and the user has not won, it navigates
   * to the lose screen.
   */
  public GlobalController() {
    gameTimer
        .timeUpProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              // If the time is up and the user has not won, navigate to the exit screen
              if (newValue && !GameState.isGameWon) {
                try {
                  App.setRoot("losescreen");
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            });
  }
}
