package nz.ac.auckland.se206;

import java.util.Random;

/**
 * The ButtonOrder class generates a random order of buttons and provides methods to access the
 * correct order. The class is implemented as a singleton to ensure a single instance throughout the
 * application.
 */
public class ButtonOrder {

  /** Singleton instance of the `ButtonOrder` class. */
  private static ButtonOrder instance;

  /**
   * Retrieves the singleton instance of the ButtonOrder class.
   *
   * @return The singleton instance of the ButtonOrder class.
   */
  public static ButtonOrder getInstance() {
    if (instance == null) {
      instance = new ButtonOrder();
    }
    return instance;
  }

  private String correctOrderString;
  private String[] switchOrder = {"red", "green", "blue"};

  /**
   * Constructs a new ButtonOrder instance by randomizing the order of button colors. The correct
   * order is generated at the time of initialization.
   */
  public ButtonOrder() {
    Random random = new Random();

    // Selects the order of the buttons to press.
    for (int i = 0; i < 3; i++) {
      int randomIndexToSwap = random.nextInt(switchOrder.length);
      String temp = switchOrder[randomIndexToSwap];
      switchOrder[randomIndexToSwap] = switchOrder[i];
      switchOrder[i] = temp;
    }

    correctOrderString = switchOrder[0] + " " + switchOrder[1] + " " + switchOrder[2];
  }

  /**
   * Gets the correct order of button colors as a space-separated string.
   *
   * @return The correct order of button colors.
   */
  public String getCorrectOrderString() {
    return this.correctOrderString;
  }

  /**
   * Gets the correct order of button colors as an array.
   *
   * @return The correct order of button colors represented as an array.
   */
  public String[] getCorrectOrderArray() {
    return this.switchOrder;
  }
}
