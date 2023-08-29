package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;

public class GlobalController {

  private final GameTimer gameTimer = GameTimer.getInstance();

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
