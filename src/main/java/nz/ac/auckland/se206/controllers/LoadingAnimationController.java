package nz.ac.auckland.se206.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.AnimationCentralControl;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.LoadingAnimationsIterface;

public class LoadingAnimationController implements LoadingAnimationsIterface {

  @FXML private ImageView aiThinkingYellow;

  @FXML private ImageView aiThinkingNormal;

  @FXML private ImageView aiThinkingBlue;

  @FXML private ImageView aiThinkingOrange;

  @FXML private ImageView aiThinkingPink;

  @FXML private ImageView aiThinkingPurple;

  @FXML private ImageView aiNormal;

  @FXML private ImageView speechBubble;

  @FXML private Label textLabel;

  private ImageView[] thinkingCircles;

  private int currentImageIndex = 0;

  private FadeTransition currentTransition;

  /** Called by JavaFX when controller is created (after elements have been initialized). */
  @FXML
  public void initialize() {
    System.out.println("LoadingAnimationController.initialize()");
    speechBubble.setVisible(false);
    textLabel.setVisible(false);

    // Add this controller as an observer of the animation central control
    AnimationCentralControl animationCentralControl = AnimationCentralControl.getInstance();
    animationCentralControl.addObserver(this);

    thinkingCircles =
        new ImageView[] {
          aiThinkingYellow,
          aiThinkingNormal,
          aiThinkingBlue,
          aiThinkingOrange,
          aiThinkingPink,
          aiThinkingPurple
        };

    // Set the opacity of all the thinking circles to 0
    for (ImageView imageView : thinkingCircles) {
      imageView.setOpacity(0);
    }

    aiNormal.setOpacity(1);
  }

  /** Called by the animation central control when the animation should start. */
  public void playAnimation() {
    GameState.isLoadingAnimationlaying.set(true);

    speechBubble.setVisible(true);
    textLabel.setVisible(true);
    aiNormal.setOpacity(0);

    // Fades the loading animation
    currentTransition = new FadeTransition(Duration.seconds(1), thinkingCircles[currentImageIndex]);
    currentTransition.setFromValue(1);
    currentTransition.setToValue(1);
    currentTransition.setOnFinished(
        e -> {
          thinkingCircles[currentImageIndex].setOpacity(0);
          currentImageIndex = (currentImageIndex + 1) % thinkingCircles.length;
          thinkingCircles[currentImageIndex].setOpacity(1);
          playAnimation();
        });
    currentTransition.play();
  }

  /** Called by the animation central control when the animation should stop. */
  public void stopAnimation() {
    GameState.isLoadingAnimationlaying.set(false);

    // Stop the current transition if it exists
    if (currentTransition != null) {
      currentTransition.stop();
    }

    for (ImageView imageView : thinkingCircles) {
      imageView.setOpacity(0);
    }

    aiNormal.setOpacity(1);
    speechBubble.setVisible(false);
    textLabel.setVisible(false);
  }
}
