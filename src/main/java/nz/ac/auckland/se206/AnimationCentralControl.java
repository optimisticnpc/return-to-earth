package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;

public class AnimationCentralControl {

  private static AnimationCentralControl instance;
  private static List<LoadingAnimationsIterface> loadingAnimations = new ArrayList<>();

  public static AnimationCentralControl getInstance() {
    if (instance == null) {
      instance = new AnimationCentralControl();
    }
    return instance;
  }

  private AnimationCentralControl() {
    initializeChatCentralControl();
  }

  public void initializeChatCentralControl() {
    System.out.println("AnimationCentralControl Iniatialized");
    loadingAnimations = new ArrayList<>();
  }

  public void addObserver(LoadingAnimationsIterface observer) {
    loadingAnimations.add(observer);
  }

  public void removeObserver(LoadingAnimationsIterface observer) {
    loadingAnimations.remove(observer);
  }

  public void playAllAnimation() {
    for (LoadingAnimationsIterface animation : loadingAnimations) {
      animation.playAnimation();
    }
  }

  public void stopAllAnimation() {
    for (LoadingAnimationsIterface animation : loadingAnimations) {
      animation.stopAnimation();
    }
  }
}
