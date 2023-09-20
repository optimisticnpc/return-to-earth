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
        new MathQuestion("What is the next number in this sequence: 4, 4, 8, 12, 20, __", "32"));
    MathQuestions.add(
        new MathQuestion(
            "In a spaceship there are space sheeps and cosmic chickens. There are 22 animals"
                + " altogether. If there are 74 legs, how many space sheep are there?",
            "15"));
    MathQuestions.add(
        new MathQuestion(
            "I have fewer than 20 space cabbages. If I plant them in rows of 3, I have 2 left over"
                + " If I plant them in rows of 5, I have 2 left over, how many space cabbages do I"
                + " have?",
            "17"));
    MathQuestions.add(
        new MathQuestion(
            "A teacher has between 5 to 15 alien fossils. On Monday she gave half of them away and"
                + " then kept one. On Tuesday she gave half of the remaining fossils away and then"
                + " kept one. If she has one alien fossil left on Wednesday, how many did she start"
                + " with on Monday?",
            "10"));
    MathQuestions.add(
        new MathQuestion(
            "On Earth, a spacesuit is about 126kg. On the Moon, the gravitational force is only"
                + " 1/6th as strong as on Earth. How much would the boots weigh on the Moon?",
            "21"));

    // Select two math puzzles for each game
    selectedPuzzles = selectTwoRandomMathQuestions();
  }

  // Singleton design pattern
  public static MathQuestionSelector getInstance() {
    if (instance == null) {
      instance = new MathQuestionSelector();
    }
    return instance;
  }

  private MathQuestion[] selectTwoRandomMathQuestions() {
    int firstIndex = random.nextInt(MathQuestions.size());
    int secondIndex;
    do {
      secondIndex = random.nextInt(MathQuestions.size());
    } while (firstIndex == secondIndex);

    return new MathQuestion[] {MathQuestions.get(firstIndex), MathQuestions.get(secondIndex)};
  }

  public void setNewMathQuestions() {
    selectedPuzzles = selectTwoRandomMathQuestions();
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
