package nz.ac.auckland.se206;

import javafx.scene.image.ImageView;

/**
 * The RoomInitializer class is responsible for initializing the room. It provides a central control
 * for changing the visibility of the robot when the loading animation is playing.
 */
public class RoomInitializer {
  public void setupAiHoverImageListeners(ImageView robot) {

    // Hide the hover image of the AI when loading animation is playing
    GameState.isLoadingAnimationlaying.addListener(
        (observable, oldValue, newValue) -> {
          if (!oldValue && newValue) { // If it changes from false to true
            robot.setVisible(false);
          }
        });

    GameState.isLoadingAnimationlaying.addListener(
        (observable, oldValue, newValue) -> {
          if (oldValue && !newValue) { // If it changes true to false
            robot.setVisible(true);
          }
        });
  }
}
