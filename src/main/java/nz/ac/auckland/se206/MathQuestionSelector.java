package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The MathQuestionSelector class manages the selection and generation of mathematical questions for
 * the game. It follows the singleton design pattern to ensure a single instance throughout the
 * application.
 */
public class MathQuestionSelector {

  /** The singleton instance of the `MathQuestionSelector` class. */
  private static MathQuestionSelector instance;

  /**
   * Gets the singleton instance of the `MathQuestionSelector` class.
   *
   * @return The singleton instance of the `MathQuestionSelector` class.
   */
  public static MathQuestionSelector getInstance() {
    if (instance == null) {
      instance = new MathQuestionSelector();
    }
    return instance;
  }

  /** The list of available mathematical questions. */
  private List<MathQuestion> mathQuestions = new ArrayList<>();

  /** A random number generator for selecting questions. */
  private Random random = new Random();

  /** An array to store the selected mathematical puzzles. */
  private MathQuestion[] selectedPuzzles;

  /**
   * Constructs a new MathQuestionSelector object and initializes the available mathematical
   * questions.
   */
  private MathQuestionSelector() {
    initializeQuestions();

    // Select two math puzzles for each game
    selectedPuzzles = selectTwoRandomMathQuestions();
  }

  /**
   * Selects two random maths questions to be used for that instance of the game.
   *
   * @return two randomly selected maths questions.
   */
  private MathQuestion[] selectTwoRandomMathQuestions() {
    int firstIndex = random.nextInt(mathQuestions.size());
    int secondIndex;
    do {
      secondIndex = random.nextInt(mathQuestions.size());
    } while (firstIndex == secondIndex);

    return new MathQuestion[] {mathQuestions.get(firstIndex), mathQuestions.get(secondIndex)};
  }

  /** Selects and sets two new random mathematical questions. */
  public void setNewMathQuestions() {
    selectedPuzzles = selectTwoRandomMathQuestions();
  }

  /**
   * Generates a passcode based on the answers to the selected mathematical questions.
   *
   * @return The generated passcode.
   */
  public String generatePasscode() {
    String firstAnswer = selectedPuzzles[0].getAnswer();
    String secondAnswer = selectedPuzzles[1].getAnswer();
    return firstAnswer + secondAnswer;
  }

  /**
   * Gets the text of the first selected mathematical question.
   *
   * @return The text of the first selected mathematical question.
   */
  public String getFirstQuestion() {
    return selectedPuzzles[0].getQuestion();
  }

  /**
   * Gets the text of the second selected mathematical question.
   *
   * @return The text of the second selected mathematical question.
   */
  public String getSecondQuestion() {
    return selectedPuzzles[1].getQuestion();
  }

  public String getFirstAnswer() {
    return selectedPuzzles[0].getAnswer();
  }

  public String getSecondAnswer() {
    return selectedPuzzles[1].getAnswer();
  }

  public String getFirstQuestionHint() {
    return selectedPuzzles[0].getHint1();
  }

  public String getSecondQuestionHint() {
    return selectedPuzzles[1].getHint1();
  }

  /** Initializes the list of available mathematical questions. */
  private void initializeQuestions() {
    // Initialize all the math questions

    // Fibonacci
    mathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 0, 1, 1, 2, 3, 5, 8, __",
            "13",
            "Have you heard of the Fibonacci sequence? ",
            "Add the last two numbers in the sequence to get the next number. "));
    // Prime numbers
    mathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, __",
            "31",
            "The sequence is the sequence of prime numbers. ",
            "Almost all prime numbers are odd numbers. "));
    mathQuestions.add(
        new MathQuestion(
            "There are three groups of aliens. The first group has 10 more than the second group,"
                + " the second group is 10 times the third group. How many aliens are there"
                + " altogether if the third group has 1 alien?",
            "31",
            "Group 1 = Group 2 + 10, Group 2 = Group 3 * 10. Just work backwards from what you know"
                + " about group 3.",
            "Group 1 has 20 aliens, Group 2 has 10 aliens. "));
    mathQuestions.add(
        new MathQuestion(
            "In a spaceship there are space sheeps and cosmic chickens. There are 20 animals"
                + " altogether. If there are 50 legs, how many space chickens are there?",
            "15",
            "There are 20 sheep legs altogether. ",
            "There are 30 chicken legs altogether. "));
    mathQuestions.add(
        new MathQuestion(
            "There are five aliens. Each of the five want to play a single round of space badminton"
                + " against the other four. How many games will they play in total?",
            "10",
            "The first alien has to play with 4 others. The second alien will play with everyone"
                + " except the first alien... ",
            "Maybe it has something to do with 4+3+2+1. "));

    mathQuestions.add(
        new MathQuestion(
            "I have somewhere between 15 and 20 space cabbages. If I plant them in rows of 3, I"
                + " have 2 left over. If I plant them in rows of 5, I have 2 left over, how many"
                + " space cabbages do I have?",
            "17",
            "It is 2 greater than a multiple of 5. ",
            "You best strategy at this point is to just guess. ")); // 17 is the only number thats
    // fits either of those
    // requirements individually
    mathQuestions.add(
        new MathQuestion(
            "There is a pile of alien fossils. Person A takes half of the pile for himself. Person"
                + " B takes 1 from the remaining pile for himself. If there are 5 fossils remaining"
                + " in the pile, how many were there at the start?",
            "12",
            "Try to work backwards or guess! ",
            "There were 6 fossils remaining after person A had taken. "));
    mathQuestions.add(
        new MathQuestion(
            "On Earth, a spacesuit is about 126kg. On the Moon, the gravitational force is only"
                + " 1/6th as strong as on Earth. How much would the boots weigh on the Moon?",
            "21",
            "120 is equal to 20 time 6. ",
            "The answer is very close to 20. "));

    // Divide by three to get the middle number, then find the next largest odd number
    mathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 57. What is the"
                + " largest of these numbers?",
            "21",
            "19+19+19 is equal to 57. ",
            "The answer is close to 19. Don't forget it is odd! "));
    mathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 129. What is the"
                + " largest of these numbers?",
            "45",
            "43+43+43 is equal to 129. ",
            "The answer is close to 43. Don't forget it is odd! "));
  }
}
