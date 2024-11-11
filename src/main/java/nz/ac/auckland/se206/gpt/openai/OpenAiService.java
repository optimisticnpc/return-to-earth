package nz.ac.auckland.se206.gpt.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.File;

/** OpenAI services delegated to store the login credentials. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiService {

  /**
   * Utility method to check if a string is empty or null.
   *
   * @param value The string to check.
   * @return True if the string is null or empty, false otherwise.
   */
  private static boolean isEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  /**
   * Checks if the providedapiKey are valid input.
   *

   * @param apiKey The apiKey to check.
   * @throws IllegalArgumentException if the apiKey is null or empty.
   */
  private static void checkValidInput(String apiKey) {
    if (isEmpty(apiKey)) {
      throw new IllegalArgumentException("apiKey cannot be null or empty");
    }
  }

  private String apiKey;

  /**
   * Creates an instance of OpenAiService with the apiKey read from the specified file.
   *
   * @param fileName The name of the file containing the API proxy configuration.
   */
  public OpenAiService(String fileName) {
    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig(new File(fileName));
      checkValidInput(config.getApiKey());
      this.apiKey = config.getApiKey();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
    }
  }

  /**
   * Returns the API key associated with the OpenAI service.
   *
   * @return The API key.
   */
  public String getApiKey() {
    return apiKey;
  }

}
