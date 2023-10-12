package nz.ac.auckland.se206.ball;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * The BouncingBallPane class represents a JavaFX Pane that displays a bouncing ball animation. It
 * creates a ball and continuously updates its position to make it bounce off the pane's boundaries.
 */
public class BouncingBallPane extends Pane {

  /** The radius of the bouncing ball. */
  private double ballRadius = 10;

  /** The horizontal speed of the bouncing ball. */
  private double horizontalSpeed = 0.5;

  /** The vertical speed of the bouncing ball. */
  private double verticalSpeed = 0.5;

  /** Constructs a new BouncingBallPane by creating a ball and initializing the animation. */
  public BouncingBallPane() {
    Circle ball = new Circle(ballRadius);

    // Places ball in the middle of the screen.
    getChildren().add(ball);
    ball.setLayoutX(getWidth() / 2);
    ball.setLayoutY(getHeight() / 2);

    // Sets ball to play indefinitely.
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), e -> move(ball)));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   * Moves the given ball according to its current position and speed, making it bounce off the
   * boundaries of the pane when necessary.
   *
   * @param ball The circle representing the bouncing ball.
   */
  private void move(Circle ball) {
    ball.setLayoutX(ball.getLayoutX() + horizontalSpeed);
    ball.setLayoutY(ball.getLayoutY() + verticalSpeed);

    if (ball.getLayoutX() > getWidth() || ball.getLayoutX() < 0) {
      horizontalSpeed *= -1;
    }
    if (ball.getLayoutY() > getHeight() || ball.getLayoutY() < 0) {
      verticalSpeed *= -1;
    }
  }
}
