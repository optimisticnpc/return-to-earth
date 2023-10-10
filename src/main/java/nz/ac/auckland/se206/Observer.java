package nz.ac.auckland.se206;

public interface Observer {
  void update();

  void clearContents();

  void showLoadingIcon();

  void hideLoadingIcon();
}
