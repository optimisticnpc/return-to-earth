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
            "What is the next number in this sequence: 0, 1, 1, 2, 3, 5, 8, __", "13"));
    MathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 0, 1, 1, 2, 4, 7, 13, 24, 44, __", "81"));
    MathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, __",
            "31"));
    MathQuestions.add(
        new MathQuestion(
            "There are four piles of aliens.  The first pile is 4 more than the second pile,"
                + " the second pile is four times the fourth pile, and the third pile is half the"
                + " first pile. How many aliens are there altogether if the forth pile has one"
                + " alien?",
            "17"));
    MathQuestions.add(
        new MathQuestion(
            "In a spaceship there are space sheeps and cosmic chickens. There are 22 animals"
                + " altogether. If there are 74 legs, how many space sheep are there?",
            "15"));
    MathQuestions.add(
        new MathQuestion(
            "Abigail, Barbara, Constance, Diana and Erica want to play each other at space"
                + " badminton. How many games will they play?",
            "10"));
    MathQuestions.add(
        new MathQuestion(
            "I have fewer than 20 space cabbages. If I plant them in rows of 3, I have 2 left over"
                + " If I plant them in rows of 5, I have 2 left over, how many space cabbages do I"
                + " have?",
            "17"));
    MathQuestions.add(
        new MathQuestion(
            "A teacher has somewhere between 5 to 15 alien fossils. On Monday she gave half of them"
                + " away and then kept one for herself. On Tuesday she gave half of the remaining"
                + " fossils away and then kept another for herself. If she has one alien fossil"
                + " left on Wednesday, how many did she start with on Monday?",
            "10"));
    MathQuestions.add(
        new MathQuestion(
            "On Earth, a spacesuit is about 126kg. On the Moon, the gravitational force is only"
                + " 1/6th as strong as on Earth. How much would the boots weigh on the Moon?",
            "21"));
    MathQuestions.add(
        new MathQuestion(
            "On the Moon, a spaceboot is about 9.5kg. On Earth, the gravitational force is 6 times"
                + " as strong as on the Moon. How much would the boots weigh on Earth?",
            "57"));
    MathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 57. What is the"
                + " largest of these numbers?",
            "21"));
    MathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 129. What is the"
                + " largest of these numbers?",
            "45"));
    MathQuestions.add(
        new MathQuestion(
            "A spaceship has a weight limit of 1500kg.  If the average weight of the people in the"
                + " spaceship is 80kg and the combined weight of all the people is 100kg over the"
                + " limit, how many people are in the spaceship?",
            "20"));
    MathQuestions.add(
        new MathQuestion(
            "A spaceship has a weight limit of 2200kg.  If the average weight of the people in the"
                + " spaceship is 120kg and the combined weight of all the people is 200kg over the"
                + " limit, how many people are in the spaceship?",
            "20"));

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
