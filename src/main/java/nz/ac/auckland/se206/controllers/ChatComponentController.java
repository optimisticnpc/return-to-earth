package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import nz.ac.auckland.se206.Observer;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the chat view. */
public class ChatComponentController implements Observer {

  private ChatCentralControl chatCentralControl;
  private List<ChatMessage> messages;

  @FXML private ImageView loadingIcon;
  @FXML private ImageView soundIcon;
  @FXML private TextArea inputText;
  @FXML private VBox chatLog;
  @FXML private ScrollPane scrollPane;
  @FXML private Button sendButton;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public void initialize() {
    System.out.println("ChatComponentController.initialize()");

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

  @FXML
  public void setSendButtonAction() {
    String message = inputText.getText().replaceAll("[\n\r]", ""); // Remove all newline characters
    inputText.clear();
    if (message.trim().isEmpty()) {
      return;
    }
    ChatMessage msg = new ChatMessage("user", message);
    chatCentralControl.addMessage(msg);
    chatCentralControl.runGpt(msg);
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    chatCentralControl.nextPhase();
  }

  public void addLabel(String message, Pos position) {
    HBox hBox = new HBox();
    hBox.setAlignment(position);
    hBox.setPadding(new Insets(5, 5, 5, 10));

    Text text = new Text(message);
    if (position == Pos.CENTER_LEFT) {
      text.setFont(javafx.scene.text.Font.font("Arial", 15));
    } else if (position == Pos.CENTER_RIGHT) {
      text.setFont(javafx.scene.text.Font.font("Comic Sans MS", 15));
    }
    TextFlow textFlow = new TextFlow(text);

    if (position == Pos.CENTER_LEFT) {
      textFlow.setStyle("-fx-background-color: rgb(255,242,102);" + "-fx-background-radius: 20px");
    } else if (position == Pos.CENTER_RIGHT) {
      textFlow.setStyle("-fx-background-color: rgb(255,255,255);" + "-fx-background-radius: 20px");
    }

    textFlow.setPadding(new Insets(5, 10, 5, 10));

    hBox.getChildren().add(textFlow);
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            chatLog.getChildren().add(hBox);
          }
        });
  }

  private void updateMessages() {
    messages = chatCentralControl.getMessages();
  }

  @Override
  public void update() {
    chatLog.getChildren().clear();
    updateMessages();
    for (ChatMessage msg : messages) {
      String msgString = msg.getContent();
      if (msg.getRole().equals("assistant")) {
        addLabel(msgString, Pos.CENTER_LEFT);
      } else if (msg.getRole().equals("user")) {
        addLabel(msgString, Pos.CENTER_RIGHT);
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
}
