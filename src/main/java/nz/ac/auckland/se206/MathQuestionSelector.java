package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathQuestionSelector {
  private static MathQuestionSelector instance;

  // Static methods
  // Singleton design pattern
  public static MathQuestionSelector getInstance() {
    if (instance == null) {
      instance = new MathQuestionSelector();
    }
    return instance;
  }

  private List<MathQuestion> mathQuestions = new ArrayList<>();
  private Random random = new Random();
  private MathQuestion[] selectedPuzzles;

  private MathQuestionSelector() {
    initializeQuestions();

    // Select two math puzzles for each game
    selectedPuzzles = selectTwoRandomMathQuestions();
  }

  private MathQuestion[] selectTwoRandomMathQuestions() {
    int firstIndex = random.nextInt(mathQuestions.size());
    int secondIndex;
    do {
      secondIndex = random.nextInt(mathQuestions.size());
    } while (firstIndex == secondIndex);

    return new MathQuestion[] {mathQuestions.get(firstIndex), mathQuestions.get(secondIndex)};
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

  private void initializeQuestions() {
    // Initialize all the math questions
    mathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 0, 1, 1, 2, 3, 5, 8, __", "13"));
    mathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 0, 1, 1, 2, 4, 7, 13, 24, 44, __", "81"));
    mathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, __",
            "31"));
    mathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 10, 20, 30, 40, 50, 60, 70, 80, __", "90"));
    mathQuestions.add(
        new MathQuestion(
            "There are three piles of aliens.  The first pile is 10 more than the second pile, the"
                + " second pile is 10 times the third pile. How many aliens are there altogether if"
                + " the third pile has 1 alien?",
            "31"));
    mathQuestions.add(
        new MathQuestion(
            "In a spaceship there are space sheeps and cosmic chickens. There are 22 animals"
                + " altogether. If there are 74 legs, how many space sheep are there?",
            "15"));
    mathQuestions.add(
        new MathQuestion(
            "There are five aliens. Each of the five want to play a single round of space badminton"
                + " against the other four. How many games will they play in total?",
            "10"));
    mathQuestions.add(
        new MathQuestion(
            "I have somewhere between 15 and 20 space cabbages. If I plant them in rows of 3, I"
                + " have 2 left over. If I plant them in rows of 5, I have 2 left over, how many"
                + " space cabbages do I have?",
            "17"));
    mathQuestions.add(
        new MathQuestion(
            "There is a pile of alien fossils. Person A takes half of the pile for himself. Person"
                + " B takes 1 for himself. If there are 5 fossils remaining in the pile, how many"
                + " were there at the start?",
            "12"));
    mathQuestions.add(
        new MathQuestion(
            "On Earth, a spacesuit is about 126kg. On the Moon, the gravitational force is only"
                + " 1/6th as strong as on Earth. How much would the boots weigh on the Moon?",
            "21"));
    mathQuestions.add(
        new MathQuestion(
            "On the Moon, a spaceboot is about 9.5kg. On Earth, the gravitational force is 6 times"
                + " as strong as on the Moon. How much would the boots weigh on Earth?",
            "57"));
    mathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 57. What is the"
                + " largest of these numbers?",
            "21"));
    mathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 129. What is the"
                + " largest of these numbers?",
            "45"));
    mathQuestions.add(
        new MathQuestion(
            "A spaceship has a weight limit of 1500kg.  If every person inside weighs 75kg, how"
                + " many people are in the spaceship?",
            "20"));
    mathQuestions.add(
        new MathQuestion(
            "A spaceship has a weight limit of 2200kg.  If every person inside weighs 50kg, how"
                + " many people are in the spaceship?",
            "44"));
  }
}
