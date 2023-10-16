package nz.ac.auckland.se206;

/**
 * The MathQuestion class represents a mathematical question with a question text and an answer. It
 * is used to store and retrieve mathematical questions and their corresponding answers.
 */
public class MathQuestion {
  /** The text of the mathematical question. */
  private String question;

  /** The correct answer to the mathematical question. */
  private String answer;

  private String hint1;
  private String hint2;

  /**
   * Constructs a new MathQuestion object with the specified question text and answer.
   *
   * @param question The text of the mathematical question.
   * @param answer The correct answer to the mathematical question.
   */
  public MathQuestion(String question, String answer) {
    this.question = question;
    this.answer = answer;
  }

  /**
   * Constructs a new MathQuestion object with the specified question text, answer, and two hints.
   *
   * @param question The text of the mathematical question.
   * @param answer The correct answer to the mathematical question.
   * @param hint1 The first hint for the mathematical question.
   * @param hint2 The second hint for the mathematical question.
   */
  public MathQuestion(String question, String answer, String hint1, String hint2) {
    this.question = question;
    this.answer = answer;
    this.hint1 = hint1;
    this.hint2 = hint2;
  }

  /**
   * Gets the text of the mathematical question.
   *
   * @return The text of the mathematical question.
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Gets the correct answer to the mathematical question.
   *
   * @return The correct answer to the mathematical question.
   */
  public String getAnswer() {
    return answer;
  }

  /**
   * Gets the first hint for the mathematical question.
   *
   * @return The first hint for the mathematical question, or null if no hint is available.
   */
  public String getHint1() {
    return hint1;
  }

  /**
   * Gets the second hint for the mathematical question.
   *
   * @return The second hint for the mathematical question, or null if no hint is available.
   */
  public String getHint2() {
    return hint2;
  }
}
