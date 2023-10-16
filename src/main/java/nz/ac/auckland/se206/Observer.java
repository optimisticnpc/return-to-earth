package nz.ac.auckland.se206;

/**
 * The Observer interface defines methods that must be implemented by classes that act as observers
 * in the observer pattern. These methods allow subjects to notify observers of changes and for
 * observers to respond to those changes.
 */
public interface Observer {
  /**
   * This method is called when the subject being observed has changed. It's used to notify the
   * observer of updates.
   */
  void update();

  /**
   * Clears the contents of the observer, usually representing a display or component, when
   * requested. This is useful for refreshing the observer's content.
   */
  void clearContents();

  /**
   * Shows a loading icon or indicator in the observer's user interface. This method is called to
   * indicate that an operation is in progress.
   */
  void showLoadingIcon();

  /**
   * Hides a loading icon or indicator in the observer's user interface. This method is called to
   * indicate that a previously ongoing operation has completed.
   */
  void hideLoadingIcon();

  /** Enables the observer's text box or input field, allowing interaction from the user. */
  void enableTextBox();

  /** Disables the observer's text box or input field, preventing user interaction. */
  void disableTextBox();
}
