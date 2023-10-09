package nz.ac.auckland.se206;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class Sound {

  private static Sound instance = null;

  public static Sound getInstance() {
    if (instance == null) {
      instance = new Sound();
    }
    return instance;
  }

  private BooleanProperty isSoundOn = new SimpleBooleanProperty(true);
  private ObjectProperty<Image> soundImage = new SimpleObjectProperty<>();

  public ObjectProperty<Image> soundImageProperty() {
    return soundImage;
  }

  public BooleanProperty isSoundOnProperty() {
    return isSoundOn;
  }

  public void resetSoundProperty() {
    isSoundOn.set(true);
  }

  public void toggleImage() throws FileNotFoundException {
    isSoundOn.set(!isSoundOnProperty().get());

    if (isSoundOn.get()) {
      InputStream soundOn = new FileInputStream("src/main/resources/images/soundicon.png");
      Image soundOnImage = new Image(soundOn);
      soundImage.set(soundOnImage);
    } else {
      InputStream soundOff = new FileInputStream("src/main/resources/images/soundicondisable.png");
      Image soundOffImage = new Image(soundOff);
      soundImage.set(soundOffImage);
    }
  }
}
