package nz.ac.auckland.se206;

public class MathQuestion {
  private String question;
  private String answer;
  private String hint1;
  private String hint2;

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

  public String getQuestion() {
    return question;
  }

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
