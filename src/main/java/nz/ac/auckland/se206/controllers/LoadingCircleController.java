package nz.ac.auckland.se206.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.AnimationCentralControl;
import nz.ac.auckland.se206.LoadingAnimationsIterface;

public class LoadingCircleController implements LoadingAnimationsIterface {

  @FXML private ImageView thinkingCircleYellow;

  @FXML private ImageView thinkingCircleNormal;

  @FXML private ImageView thinkingCircleBlue;

  @FXML private ImageView thinkingCircleOrange;

  @FXML private ImageView thinkingCirclePink;

  @FXML private ImageView thinkingCirclePurple;

  @FXML private ImageView aiCircle;

  @FXML private ImageView speechBubble;

  @FXML private Label textLabel;

  private ImageView[] thinkingCircles;

  private int currentImageIndex = 0;

  private FadeTransition currentTransition;

  @FXML
  public void initialize() {
    System.out.println("LoadingCircleController.initialize()");
    speechBubble.setVisible(false);
    textLabel.setVisible(false);

    AnimationCentralControl animationCentralControl = AnimationCentralControl.getInstance();
    animationCentralControl.addObserver(this);

    thinkingCircles =
        new ImageView[] {
          thinkingCircleYellow,
          thinkingCircleNormal,
          thinkingCircleBlue,
          thinkingCircleOrange,
          thinkingCirclePink,
          thinkingCirclePurple
        };

    for (ImageView imageView : thinkingCircles) {
      imageView.setOpacity(0);
    }

    aiCircle.setOpacity(1);
  }

  public void playAnimation() {

    speechBubble.setVisible(true);
    textLabel.setVisible(true);

    aiCircle.setOpacity(0);

    currentTransition = new FadeTransition(Duration.seconds(1), thinkingCircles[currentImageIndex]);
    currentTransition.setFromValue(1);
    currentTransition.setToValue(0.8);
    currentTransition.setOnFinished(
        e -> {
          thinkingCircles[currentImageIndex].setOpacity(0);
          currentImageIndex = (currentImageIndex + 1) % thinkingCircles.length;
          thinkingCircles[currentImageIndex].setOpacity(1);
          playAnimation();
        });
    currentTransition.play();
  }

  public void stopAnimation() {
    if (currentTransition != null) {
      currentTransition.stop();
    }

    for (ImageView imageView : thinkingCircles) {
      imageView.setOpacity(0);
    }

    aiCircle.setOpacity(1);
    speechBubble.setVisible(false);
    textLabel.setVisible(false);
  }
}
