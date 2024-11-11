package nz.ac.auckland.se206.gpt.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import nz.ac.auckland.se206.gpt.ChatMessage;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/** Responsible for preparing and executing an OpenAI Chat Completion request. */
public class ChatCompletionRequest {

  private static final int NOT_SET = -1;
  private static final String URL_COMPLETION_ENDPOINT = "https://api.openai.com/v1/chat/completions";
  // private static final String URL_COMPLETION_ENDPOINT =
      // "https://us-central1-api-proxies-and-wrappers.cloudfunctions.net/proxy/openai-chat-completion";
  private static final OpenAiService openAiServiceFromFile = new OpenAiService("apiproxy.config");

  private final OpenAiService openAiService;
  private final List<ChatMessage> messages;

  // Optional parameters
  private int maxCompletionTokens = NOT_SET;
  private double temperature = NOT_SET;
  private double topP = NOT_SET;
  private int numChoice = NOT_SET;

  /**
   * Constructs a ChatCompletionRequest using the specified OpenAiService.
   *
   * @param openAiService the OpenAiService to use for the request.
   */
  public ChatCompletionRequest(OpenAiService openAiService) {
    this.messages = new ArrayList<>();
    this.openAiService = openAiService;
  }

  /** Constructs a ChatCompletionRequest using the OpenAiService loaded from file. */
  public ChatCompletionRequest() {
    this(openAiServiceFromFile);
  }

  public List<ChatMessage> getMessages() {
    return messages;
  }

  /**
   * Adds the specified ChatMessage to the request.
   *
   * @param message the ChatMessage to add.
   * @return the current ChatCompletionRequest instance.
   */
  public ChatCompletionRequest addMessage(ChatMessage message) {
    messages.add(message);
    return this;
  }

  public void removeLastMessage() {
    messages.remove(messages.size() - 1);
  }

  /**
   * Adds a message to the request with the specified role and content.
   *
   * @param role the role of the message.
   * @param content the content of the message.
   * @return the current ChatCompletionRequest instance.
   */
  public ChatCompletionRequest addMessage(String role, String content) {
    return addMessage(new ChatMessage(role, content));
  }

  /**
   * Sets the maximum number of tokens to generate.
   *
   * @param maxCompletionTokens the maximum number of tokens to generate.
   * @return the current ChatCompletionRequest instance.
   * @throws IllegalArgumentException if maxCompletionTokens is less than 1.
   */
  public ChatCompletionRequest setMaxCompletionTokens(int maxCompletionTokens) {
    if (maxCompletionTokens < 1) {
      throw new IllegalArgumentException(
          "'max_completion_ tokens' must be at least 1, but was given " + maxCompletionTokens);
    }
    this.maxCompletionTokens = maxCompletionTokens;
    return this;
  }

  /**
   * Sets the temperature parameter for the request.
   *
   * @param temperature the temperature parameter.
   * @return the current ChatCompletionRequest instance.
   * @throws IllegalArgumentException if temperature is not between 0 and 2 inclusive.
   */
  public ChatCompletionRequest setTemperature(double temperature) {
    if (temperature < 0 || temperature > 2) {
      throw new IllegalArgumentException(
          "'temperature' must be between 0 and 2 inclusive, but was given " + temperature);
    }
    this.temperature = temperature;
    return this;
  }

  /**
   * Sets the top_p parameter for the request.
   *
   * @param topP the top_p parameter.
   * @return the current ChatCompletionRequest instance.
   * @throws IllegalArgumentException if topP is not between 0 and 1 inclusive.
   */
  public ChatCompletionRequest setTopP(double topP) {
    if (topP < 0 || topP > 1) {
      throw new IllegalArgumentException(
          "'top_p' must be between 0 and 1 inclusive, but was given " + topP);
    }
    this.topP = topP;
    return this;
  }

  /**
   * Sets the n parameter for the request.
   *
   * @param n the n parameter.
   * @return the current ChatCompletionRequest instance.
   * @throws IllegalArgumentException if n is less than 1.
   */
  public ChatCompletionRequest setN(int n) {
    if (n < 1) {
      throw new IllegalArgumentException("'n' must be at least 1, but was given " + n);
    }
    this.numChoice = n;
    return this;
  }

  /**
   * Executes the chat completion request and returns the result.
   *
   * @return the result of the chat completion request
   * @throws ApiProxyException if there is a problem executing the request
   */
  public ChatCompletionResult execute() throws ApiProxyException {
    try {
      // Build JSON array for messages
      JsonArrayBuilder jsonMessages = Json.createArrayBuilder();
      for (ChatMessage message : messages) {
        jsonMessages.add(
            Json.createObjectBuilder()
                .add("role", message.getRole())
                .add("content", message.getContent()));
      }

      // Build JSON object for overall request
      JsonObjectBuilder jsonOverallBuilder =
          Json.createObjectBuilder()
              .add("model", "gpt-3.5-turbo")
              .add("messages", jsonMessages);

      // Add optional parameters to the request if set
      if (maxCompletionTokens != NOT_SET) {
        jsonOverallBuilder.add("max_tokens", maxCompletionTokens);
      }

      if (temperature > NOT_SET) {
        jsonOverallBuilder.add("temperature", temperature);
      }

      if (topP > NOT_SET) {
        jsonOverallBuilder.add("top_p", topP);
      }

      if (numChoice != NOT_SET) {
        jsonOverallBuilder.add("n", numChoice);
      }

      JsonObject value = jsonOverallBuilder.build();

      // Create and configure the HTTP request
      HttpPost httpPost = new HttpPost(URL_COMPLETION_ENDPOINT);
      httpPost.setHeader("Content-Type", "application/json");
      httpPost.setHeader("Accept", "application/json");
      httpPost.setHeader("Authorization", "Bearer " + openAiService.getApiKey());
      httpPost.setEntity(new StringEntity(value.toString()));

      // Send the HTTP request and print the response
      CloseableHttpClient client = HttpClients.createDefault();

      // String responseString =
      //     client.execute(
      //         httpPost,
      //         httpResponse -> {
      //           return EntityUtils.toString(httpResponse.getEntity());
      //         });

      // // Print the raw JSON response to the terminal
      // System.out.println(responseString);


      ObjectMapper mapperApiMapper = new ObjectMapper();

      ResponseChatCompletion responseChat =
    client.execute(
        httpPost,
        httpResponse -> {
            // Read the response content as a JsonNode
            JsonNode responseJson = mapperApiMapper.readTree(httpResponse.getEntity().getContent());

            // Create a new ObjectNode to wrap the responseJson into "chat_completion"
            ObjectNode wrappedJson = mapperApiMapper.createObjectNode();
            wrappedJson.set("chat_completion", responseJson);

            // Map the wrappedJson into ResponseChatCompletion
            return mapperApiMapper.treeToValue(wrappedJson, ResponseChatCompletion.class);
        });


      // // Check for API call success and handle any errors
      // if (!responseChat.success && responseChat.code != 0) {
      //   throw new ApiProxyException("Problem calling API: " + responseChat.message);
      // }

      // Return the chat completion result
      return new ChatCompletionResult(responseChat.chatCompletion);

    } catch (Exception e) {
      throw new ApiProxyException("Problem calling API: " + e.getMessage());
    }
  }

  /** Prints all the messages in the request with their corresponding roles. */
  public void printMessages() {
    for (ChatMessage message : messages) {
      System.out.println("-------------------------");
      System.out.println(message.getRole() + ": " + message.getContent());
    }
  }
}
