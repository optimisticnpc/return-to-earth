package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;

/**
 * The AnimationCentralControl class is responsible for managing loading animations in the
 * application. It provides a central control point for playing and stopping loading animations by
 * observing classes that implement the LoadingAnimationsIterface.
 */
public class AnimationCentralControl {

  private static AnimationCentralControl instance;
  private static List<LoadingAnimationsIterface> loadingAnimations = new ArrayList<>();

  /**
   * Gets the singleton instance of the AnimationCentralControl class.
   *
   * @return The instance of the AnimationCentralControl class.
   */
  public static AnimationCentralControl getInstance() {
    if (instance == null) {
      instance = new AnimationCentralControl();
    }
    return instance;
  }

  /** Initializes a new instance of the AnimationCentralControl class. */
  private AnimationCentralControl() {
    initializeChatCentralControl();
  }

  /** Initializes the AnimationCentralControl class. */
  public void initializeChatCentralControl() {
    System.out.println("AnimationCentralControl Iniatialized");
    loadingAnimations = new ArrayList<>();
  }

  /**
   * Adds an observer to the AnimationCentralControl class.
   *
   * @param observer The observer to add.
   */
  public void addObserver(LoadingAnimationsIterface observer) {
    loadingAnimations.add(observer);
  }

  /**
   * Removes an observer from the AnimationCentralControl class.
   *
   * @param observer The observer to remove.
   */
  public void removeObserver(LoadingAnimationsIterface observer) {
    loadingAnimations.remove(observer);
  }

  /** Plays all the animations in the animationcentralcontrol class. */
  public void playAllAnimation() {
    for (LoadingAnimationsIterface animation : loadingAnimations) {
      animation.playAnimation();
    }
  }

  /** Stops all the animations in the animationcentralcontrol class. */
  public void stopAllAnimation() {
    for (LoadingAnimationsIterface animation : loadingAnimations) {
      animation.stopAnimation();
    }
  }
}
