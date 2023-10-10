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

    // Fibonacci
    mathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 0, 1, 1, 2, 3, 5, 8, __",
            "13",
            "Have you heard of the Fibonacci sequence?",
            "Add the last two numbers in the sequence to get the next number."));
    // Prime numbers
    mathQuestions.add(
        new MathQuestion(
            "What is the next number in this sequence: 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, __",
            "31",
            "The sequence is prime numbers",
            "Almost all prime numbers are odd numbers"));
    mathQuestions.add(
        new MathQuestion(
            "There are three groups of aliens. The first group has 10 more than the second group,"
                + " the second group is 10 times the third group. How many aliens are there"
                + " altogether if the third group has 1 alien?",
            "31",
            "Group 1 = Group 2 + 10, Group 2 = Group 3 * 10",
            "Group 1 has 20 aliens, Group 2 has 10 aliens"));
    mathQuestions.add(
        new MathQuestion(
            "In a spaceship there are space sheeps and cosmic chickens. There are 10 animals"
                + " altogether. If there are 32 legs, how many space sheep are there?",
            "6",
            "There are 8 chicken legs altogether",
            "There are 24 sheep legs altogether"));
    mathQuestions.add(
        new MathQuestion(
            "There are five aliens. Each of the five want to play a single round of space badminton"
                + " against the other four. How many games will they play in total?",
            "10",
            "The first alien has to play with 4 others. The second alien will play with everyone"
                + " except the first alien...",
            "Maybe something to do with 4+3+2+1"));

    mathQuestions.add(
        new MathQuestion(
            "I have somewhere between 15 and 20 space cabbages. If I plant them in rows of 3, I"
                + " have 2 left over. If I plant them in rows of 5, I have 2 left over, how many"
                + " space cabbages do I have?",
            "17",
            "It is 2 greater than a multiple of 5",
            "You can literally just guess")); // 17 is the only number thats fits either of those
    // requirements individually
    mathQuestions.add(
        new MathQuestion(
            "There is a pile of alien fossils. Person A takes half of the pile for himself. Person"
                + " B takes 1 from the remaining pile for himself. If there are 5 fossils remaining"
                + " in the pile, how many were there at the start?",
            "12",
            "Try to work backwards or guess!",
            "There were 6 fossils remaining after person A had taken."));
    mathQuestions.add(
        new MathQuestion(
            "On Earth, a spacesuit is about 126kg. On the Moon, the gravitational force is only"
                + " 1/6th as strong as on Earth. How much would the boots weigh on the Moon?",
            "21",
            "120 is euqal to 20 time 6",
            "The answer is very close to 20"));

    // Divide by three to get the middle number, then find the next largest odd number
    mathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 57. What is the"
                + " largest of these numbers?",
            "21",
            "19+19+19 is equal to 57",
            "The answer is close to 19. Don't forget it is odd!"));
    mathQuestions.add(
        new MathQuestion(
            "Three consecutive odd numbers are added together to give a sum of 129. What is the"
                + " largest of these numbers?",
            "45",
            "43+43+43 is equal to 126",
            "The answer is close to 43. Don't forget it is odd!"));
  }
}
