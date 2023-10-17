package nz.ac.auckland.se206.controllers;

import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nz.ac.auckland.se206.ChatCentralControl;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Observer;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the chat view. */
public class ChatComponentController implements Observer {

  /**
   * Returns a singleton instance of ChatComponentController that is the same throughout the whole
   * app.
   *
   * @return a new instance of ChatComponentController
   */
  public static ChatComponentController getInstance() {
    return new ChatComponentController();
  }

  @FXML private ImageView loadingIcon;
  @FXML private ImageView soundIcon;
  @FXML private TextArea inputText;
  @FXML private VBox chatLog;
  @FXML private ScrollPane scrollPane;
  @FXML private Button sendButton;
  @FXML private Button playMessage;

  private ChatCentralControl chatCentralControl;
  private List<ChatMessage> messages;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public void initialize() {
    // System.out.println("ChatComponentController.initialize()");

    hideLoadingIcon();
    chatCentralControl = ChatCentralControl.getInstance();
    chatCentralControl.addObserver(this);
    chatCentralControl.getMessages();
  }

  @FXML // send the message when the enter key is pressed
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
      sendButton.fire();
    }
  }

  /** Sets the send button action to send the message in the input text area to the chat log. */
  @FXML
  private void setSendButtonAction() {
    String message = inputText.getText().replaceAll("[\n\r]", ""); // Remove all newline characters
    inputText.clear();
    if (message.trim().isEmpty()) {
      return;
    }
    // Add the message to the chat log
    ChatMessage msg = new ChatMessage("user", message);
    chatCentralControl.addMessage(msg);

    // Send the message to GPT
    if (!GameState.isAuthorising) {
      addLabel(
          "You need to be authorised to talk to Space Destroyer for security"
              + " reasons.\nPlease click the middle screen in the main control room to authorise"
              + " yourself.",
          Pos.CENTER);
      ;
    } else {
      chatCentralControl.runGpt(msg);
    }
  }

  /** PlayMessageAction plays the last message written by GPT. */
  @FXML
  private void playMessageAction() {
    chatCentralControl.readMessage();
  }

  /**
   * Adds a label to the chatLog HBox and sets its font and background colour based on the position.
   *
   * @param message the message to be added
   * @param position the position of the message
   */
  private void addLabel(String message, Pos position) {
    // Creates a new box
    HBox box = new HBox();
    box.setAlignment(position);
    box.setPadding(new Insets(5, 5, 5, 10));

    // Adjusts font based on the position of the test
    Text text = new Text(message);
    if (position == Pos.CENTER_LEFT) {
      text.setFont(javafx.scene.text.Font.font("Arial", 15));
    } else if (position == Pos.CENTER_RIGHT) {
      text.setFont(javafx.scene.text.Font.font("Comic Sans MS", 15));
    } else if (position == Pos.CENTER) {
      text.setFont(javafx.scene.text.Font.font("Franklin Gothic Medium", 15));
    }
    TextFlow textFlow = new TextFlow(text);

    // Adjust background colour based on position of the text
    if (position == Pos.CENTER_LEFT) {
      textFlow.setStyle("-fx-background-color: rgb(255,242,102);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER_RIGHT) {
      textFlow.setStyle("-fx-background-color: rgb(255,255,255);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER) {
      textFlow.setStyle("-fx-background-color: rgb(255,117,128);" + "-fx-background-radius: 20px");
    }

    textFlow.setPadding(new Insets(5, 10, 5, 10));

    // Adds box to chatlog
    box.getChildren().add(textFlow);
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            chatLog.getChildren().add(box);
          }
        });
  }

  private void updateMessages() {
    messages = chatCentralControl.getMessages();
  }

  /** Updates the chat log with the messages from the chat central control. */
  @Override
  public void update() {
    // Clears chatlog and updates it
    chatLog.getChildren().clear();
    updateMessages();
    // Changes position of messages based on who sent it.
    for (ChatMessage msg : messages) {
      String msgString = msg.getContent();
      if (msg.getRole().equals("assistant")) {
        addLabel(msgString, Pos.CENTER_LEFT);
      } else if (msg.getRole().equals("user")) {
        addLabel(msgString, Pos.CENTER_RIGHT);
      } else if (msg.getRole().equals("system")) {
        addLabel(msgString, Pos.CENTER);
      }
    }

    // Scroll to the bottom of the chatLog VBox
    Platform.runLater(
        () -> {
          scrollPane.setVvalue(1.0);
        });
  }

  @Override
  public void clearContents() {
    chatLog.getChildren().clear();
  }

  @Override
  public void showLoadingIcon() {
    loadingIcon.setVisible(true);
  }

  @Override
  public void hideLoadingIcon() {
    loadingIcon.setVisible(false);
  }

  @Override
  public void enableTextBox() {
    inputText.setDisable(false);
  }

  @Override
  public void disableTextBox() {
    inputText.setDisable(true);
  }
}
