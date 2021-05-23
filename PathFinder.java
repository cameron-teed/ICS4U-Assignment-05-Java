/*
* This program takes a maze in a txt file and solves it using recursion.
*
* @author  Cameron Teed
* @version 1.0
* @since   2021-05-2THREE
*/

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;  // Import the Scanner class

/**
 * This program takes a maze in a txt file and solves it.
 */
final class PathFinder {

  private PathFinder() {
    // Prevent instantiation
    // Optional: throw an exception e.g. AssertionError
    // if this ever *is* called
    throw new IllegalStateException("Cannot be instantiated");
  }

  /**
  * One of the magic numbers.
  */
  public static final int SIX = 6;
  /**
  * One of the magic numbers.
  */
  public static final int TWLEVE = 12;
  /**
  * One of the magic numbers.
  */
  public static final int NINETEEN = 19;
  /**
  * One of the magic numbers.
  */
  public static final int THREE = 3;
  /**
  * One of the magic numbers.
  */
  public static final int FOUR = 4;
  /**
  * One of the magic numbers.
  */
  public static final int NINE = 9;
  /**
   * A 2D array that has information on a boolean version of the maze.
   */
  private static boolean[][] mazeList;

  /**
   * This function finds the start and end points of the maze.
   *
   * @param startMaze
   * @return finalCoords
   */
  static int[] startEnd(final char[][] startMaze) {
    // Setting up a list for coordinates as well as coordinate variables
    int[] finalCoords = new int[FOUR];
    int posStartX = 0;
    int posStartY = 0;
    int posEndX = 0;
    int posEndY = 0;

    // Searching the array for a starting point
    for (int start1 = 0; start1 < startMaze.length; start1++) {
      for (int start2 = 0; start2 < startMaze[start1].length; start2++) {
        if (startMaze[start1][start2] == 'S') {
          posStartX = start1;
          posStartY = start2;
          break;
        }
      }
    }

    // Searching the array for a starting point
    for (int end1 = 0; end1 < startMaze.length; end1++) {
      for (int end2 = 0; end2 < startMaze[end1].length; end2++) {
        if (startMaze[end1][end2] == 'G') {
          posEndX = end1;
          posEndY = end2;
          break;
        }
      }
    }

    // Adding the coordinates to an array
    finalCoords[0] = posStartX;
    finalCoords[1] = posStartY;
    finalCoords[2] = posEndX;
    finalCoords[THREE] = posEndY;

    // Returning the array with the coordinates
    return finalCoords;
  }

  /**
   * This function prints a maze from a 2D array to the user.
   *
   * @param mazeArray
   * @param length
   * @param width
   */
  static void printMaze(final char[][] mazeArray, final int length,
                        final int width) {
    // Printing the columns containing the maze information
    for (int counterX = 0; counterX < width; counterX++) {
      for (int counterY = 0; counterY < length; counterY++) {
        char printValue = (char) (mazeArray[counterX][counterY]);
        System.out.print(printValue + " ");
      }
      System.out.println("");
    }
  }

  /**
   * This function takes elements from a txt file and adds them to a 2D array
   * to create a maze.
   *
   * @param mazeFile
   * @param length
   * @param width
   * @return newMaze
   */
  static char[][] createMaze(final File mazeFile, final int length,
                             final int width) {
    // Creating array for the function to return containing the maze info
    char[][] newMaze = new char[width][length];

    try {
      // Taking the elements of the txt file and adding them to a 2D array
      Scanner mazeReader = new Scanner(mazeFile);
      for (int row = 0; mazeReader.hasNextLine() && row < width; row++) {
        char[] tempArray = mazeReader.nextLine().toCharArray();
        for (int col = 0; col < length && col < tempArray.length; col++) {
          newMaze[row][col] = tempArray[col];
        }
      }

      // Catching and showing the user what error occurred
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: Unable to read file");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("ERROR: Unable to create maze");
    }

    // Returning the newly created maze
    return newMaze;
  }

  /**
   * This function tests if the maze has a solution.
   *
   * @param viewMaze
   * @param startStop
   * @param rowSize
   * @param columnSize
   * @return solvedMaze
   */
  static char[][] mazeTest(final char[][] viewMaze, final int[] startStop,
                              final int rowSize,  final int columnSize) {
    // Creating a boolean version of the maze
    mazeList = new boolean[columnSize][rowSize];
    boolean[][] mazeArray = new boolean[columnSize][rowSize];
    boolean[][] firstList = new boolean[columnSize][rowSize];
    boolean[][] secondList = new boolean[columnSize][rowSize];

    // Creating a boolean verion of the maze
    for (int mapRow = 0; mapRow < viewMaze.length; mapRow++) {
      for (int mapColumn = 0; mapColumn < viewMaze[0].length; mapColumn++) {
        if (viewMaze[mapRow][mapColumn] == '#') {
          mazeArray[mapRow][mapColumn] = true;
        } else {
          mazeArray[mapRow][mapColumn] = false;
        }
      }
    }

    // Filling the other arrays with boolean values
    for (int booleanRow = 0; booleanRow < viewMaze.length; booleanRow++) {
      for (int booleanCol = 0; booleanCol < viewMaze[booleanRow].length;
           booleanCol++) {
        mazeList[booleanRow][booleanCol] = false;
        firstList[booleanRow][booleanCol] = false;
        secondList[booleanRow][booleanCol] = false;
      }
    }

    // Extracting important coordinates from the passed in array
    int beginX = startStop[0];
    int beginY = startStop[1];
    int stopX = startStop[2];
    int stopY = startStop[THREE];

    // Finding a solution for the maze
    boolean solution = mazeSolution(mazeArray, firstList, secondList,
                                  beginX, beginY, stopX, stopY);

    // Telling the user if a solution was found or not
    if (solution) {
      System.out.println("The Solution:");
    } else {
      System.out.println("No Solution Found");
    }

    // Initializing a character array containing the
    char[][] solvedMaze = new char[columnSize][rowSize];

    // Converting the boolean version of the maze back to normal
    for (int rows = 0; rows < columnSize; rows++) {
      for (int cols = 0; cols < rowSize; cols++) {
        if (mazeList[rows][cols]) {
          solvedMaze[rows][cols] = '+';
        } else if (!mazeList[rows][cols]
                   && viewMaze[rows][cols] == '.') {
          solvedMaze[rows][cols] = '.';
        } else {
          solvedMaze[rows][cols] = '#';
        }
      }
    }

    // Redefining the start and end coordinates
    solvedMaze[beginX][beginY] = 'S';
    solvedMaze[stopX][stopY] = 'G';

    // Returning the newly solved maze
    return solvedMaze;
  }

  /**
   * This function uses recursion to find a solution to a boolean version of the
   * maze.
   *
   * @param boolMaze
   * @param pathTraveled
   * @param finishedMaze
   * @param coordX
   * @param coordY
   * @param goalX
   * @param goalY
   * @return condition
   */
  static boolean mazeSolution(final boolean[][] boolMaze, final boolean[][]
                            pathTraveled, final boolean[][] finishedMaze,
                            final int coordX, final int coordY,
                            final int goalX, final int goalY) {

    // Checking if the user has reached the end of the maze
    if (coordX == goalX && coordY == goalY) {
      return true;
    }

    // Checking if the program is on a wall or has already visited the space
    if (boolMaze[coordX][coordY] || pathTraveled[coordX][coordY]) {
      return false;
    }

    // Marking the spot the program is on as been traveled through before
    pathTraveled[coordX][coordY] = true;

    // Checking if the program is on the left edge of the maze
    if (coordX != 0) {
      if (mazeSolution(boolMaze, pathTraveled, finishedMaze, coordX - 1,
                     coordY, goalX, goalY)) {
        mazeList[coordX][coordY] = true;
        return true;
      }
    }

    // Checking if the program is on the right edge of the maze
    if (coordX != boolMaze.length - 1) {
      if (mazeSolution(boolMaze, pathTraveled, finishedMaze, coordX + 1,
                     coordY, goalX, goalY)) {
        mazeList[coordX][coordY] = true;
        return true;
      }
    }

    // Checking if the program is on the top edge of the maze
    if (coordY != 0) {
      if (mazeSolution(boolMaze, pathTraveled, finishedMaze, coordX,
                     coordY - 1, goalX, goalY)) {
        mazeList[coordX][coordY] = true;
        return true;
      }
    }

    // Checking if the program is on the bottom edge of the maze
    if (coordY != boolMaze[0].length - 1) {
      if (mazeSolution(boolMaze, pathTraveled, finishedMaze, coordX,
                     coordY + 1, goalX, goalY)) {
        mazeList[coordX][coordY] = true;
        return true;
      }
    }

    // Returning false should none of the above conditions be met
    return false;
  }

  /**
   * This function takes three arrays containing mazes and then shows their
   * path from the start to the end of the maze to the user.
   *
   * @param args
   */
  public static void main(final String[] args) {
    // Creating the first maze
    File firstMazeFile = new File("Maze1.txt");
    char[][] firstMaze = createMaze(firstMazeFile, SIX, SIX);

    // Printing the first maze at its starting point
    System.out.println("Maze 1:");
    printMaze(firstMaze, SIX, SIX);
    System.out.println("");

    // Finding the start and stop points of the first maze
    int[] firstBreakPoints = startEnd(firstMaze);

    // Finding a solution to the first maze and printing it out
    char[][] firstSolved = mazeTest(firstMaze, firstBreakPoints, SIX, SIX);
    printMaze(firstSolved, SIX, SIX);
    System.out.println("");
    System.out.println("");

    // Creating the second maze
    File secondMazeFile = new File("Maze2.txt");
    char[][] secondMaze = createMaze(secondMazeFile, SIX, TWLEVE);

    // Printing the second maze at its starting point
    System.out.println("Maze 2:");
    printMaze(secondMaze, SIX, TWLEVE);
    System.out.println("");

    // Finding the start and stop points of the second maze
    int[] secondBreakPoints = startEnd(secondMaze);

    // Finding a solution to the second maze and printing it out
    char[][] secondSolved = mazeTest(secondMaze, secondBreakPoints,
                                     SIX, TWLEVE);
    printMaze(secondSolved, SIX, TWLEVE);
    System.out.println("");
    System.out.println("");

    // Creating the third maze
    File thirdMazeFile = new File("MazeTHREE.txt");
    char[][] thirdMaze = createMaze(thirdMazeFile, NINETEEN, NINE);

    // Printing the third maze at its starting point
    System.out.println("Maze THREE:");
    printMaze(thirdMaze, NINETEEN, NINE);
    System.out.println("");

    // Finding the start and stop points of the third maze
    int[] thirdBreakPoints = startEnd(thirdMaze);

    // Finding a solution to the third maze and printing it out
    char[][] thirdSolved = mazeTest(thirdMaze, thirdBreakPoints,
                                    NINETEEN, NINE);
    printMaze(thirdSolved, NINETEEN, NINE);

    System.out.println("\nDone");
  }
}
