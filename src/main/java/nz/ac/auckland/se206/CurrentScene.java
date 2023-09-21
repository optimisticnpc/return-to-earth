package nz.ac.auckland.se206;

public class CurrentScene {
  private static CurrentScene instance = null;

  public static CurrentScene getInstance() {
    if (instance == null) {
      instance = new CurrentScene();
    }
    return instance;
  }

  private int current;

  public void setCurrent(int current) {
    this.current = current;
  }

  public int getCurrent() {
    return this.current;
  }
}
