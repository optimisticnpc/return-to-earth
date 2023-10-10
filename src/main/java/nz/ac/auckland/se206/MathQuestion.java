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

  public String getHint1() {
    return hint1;
  }

  public String getHint2() {
    return hint2;
  }
}
