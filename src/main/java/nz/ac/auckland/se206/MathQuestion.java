package nz.ac.auckland.se206;

public class MathQuestion {
  private String question;
  private String answer;

  public MathQuestion(String question, String answer) {
    this.question = question;
    this.answer = answer;
  }

  public String getQuestion() {
    return question;
  }

  public String getAnswer() {
    return answer;
  }
}
