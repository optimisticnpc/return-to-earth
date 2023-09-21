package nz.ac.auckland.se206;

import java.util.Random;

public class ButtonOrder {
  private static ButtonOrder instance;

  public static ButtonOrder getInstance() {
    if (instance == null) {
      instance = new ButtonOrder();
    }
    return instance;
  }

  private String correctOrderString;
  private String[] switchOrder = {"red", "green", "blue"};

  public ButtonOrder() {
    Random random = new Random();

    for (int i = 0; i < 3; i++) {
      int randomIndexToSwap = random.nextInt(switchOrder.length);
      String temp = switchOrder[randomIndexToSwap];
      switchOrder[randomIndexToSwap] = switchOrder[i];
      switchOrder[i] = temp;
    }

    correctOrderString = switchOrder[0] + " " + switchOrder[1] + " " + switchOrder[2];
  }

  public String getCorrectOrderString() {
    return this.correctOrderString;
  }

  public String[] getCorrectOrderArray() {
    return this.switchOrder;
  }
}
