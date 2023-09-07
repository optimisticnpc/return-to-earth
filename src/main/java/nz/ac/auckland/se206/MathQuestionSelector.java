package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathQuestionSelector {
  private static MathQuestionSelector instance;

  private List<MathQuestion> MathQuestions = new ArrayList<>();
  private Random random = new Random();
  private MathQuestion[] selectedPuzzles;

  private MathQuestionSelector() {
    MathQuestions.add(
        new MathQuestion(
            "There are 5 pieces of reinforced carbon fibre composite. The average length is 8 m,"
                + " the median is 10 m. What is the maximum length of the shortest piece of carbon"
                + " fibre composite?",
            "05"));
    MathQuestions.add(
        new MathQuestion(
            "In a spaceship are space sheep and cosmic chickens. There are 22 animals altogether."
                + " If there are 74 legs, how many space sheep are there?",
            "15"));
    MathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 57. What is the"
                + " largest of these numbers?",
            "21"));
    MathQuestions.add(
        new MathQuestion(
            "What 2 digit numbers revert to itself if you add the digits together and multiply by"
                + " 3?",
            "27"));
    MathQuestions.add(
        new MathQuestion(
            "On Earth, a pair of boots weighs 15 kg. On the Moon, the gravitational force is only"
                + " 1/6th as strong as on Earth. How much would the boots weigh on the Moon?",
            "25"));

    selectedPuzzles = selectTwoRandomMathQuestions();
  }

  // Singleton design pattern
  public static MathQuestionSelector getInstance() {
    if (instance == null) {
      instance = new MathQuestionSelector();
    }
    return instance;
  }

  public MathQuestion[] selectTwoRandomMathQuestions() {
    int firstIndex = random.nextInt(MathQuestions.size());
    int secondIndex;
    do {
      secondIndex = random.nextInt(MathQuestions.size());
    } while (firstIndex == secondIndex);

    return new MathQuestion[] {MathQuestions.get(firstIndex), MathQuestions.get(secondIndex)};
  }

  public String generatePasscode() {
    String firstAnswer = selectedPuzzles[0].getAnswer();
    String secondAnswer = selectedPuzzles[1].getAnswer();

    return firstAnswer + secondAnswer;
  }

  public String getFirstQuestion() {
    return selectedPuzzles[0].getQuestion();
  }

  public String getSecondQuestion() {
    return selectedPuzzles[1].getQuestion();
  }
}
