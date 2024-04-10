import java.io.IOException;
import java.util.Scanner;

public class MazeRunner {

    private final static String MESSAGE_1 = "Welcome Maze Runner!";
    private final static String MESSAGE_2 = "You will need a map to get started. Do you have a map?";
    private final static String STARTUP_EXIT_1 = "Without a map I can not solve the maze. Do you want to leave?";
    private final static String MESSAGE_3 = "Let me import it for you. What's the file name?";
    private final static String MESSAGE_4 = "Lets get you out of this maze!";
    private final static String MESSAGE_5 = "Where do you want me to print the map?";
    private final static String MESSAGE_6 = "Alright, I saved the map for you.";
    private final static String IO_EXCEPTION = "Something is wrong with that file or it's the wrong file name. Do you want to try again?";
    private final static String INVALID_MAZE_EXCEPTION = "That file does not contain a valid maze! Do you want to try again?";
    private final static String TRY_AGAIN = "Hmm, I don't understand that, try again.";
    private final static String EXIT_MESSAGE_1 = "I hope you return when you have a map. Goodbye Maze Runner.";
    private final static String EXIT_MESSAGE_2 = "Good luck out there! Until next time Maze Runner!";


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println(MESSAGE_1);
        MazeSolver mazeSolver = new MazeSolver();
        System.out.println(MESSAGE_2);
        if (yesNoInput(scan)) {
            while (true) {
                System.out.println(MESSAGE_3);
                boolean flag;
                try {
                    mazeSolver.readMaze(scan.nextLine());
                    break;
                } catch (InvalidMazeException e) {
                    System.out.println(INVALID_MAZE_EXCEPTION);
                    flag = true;
                } catch (IOException f) {
                    System.out.println(IO_EXCEPTION);
                    flag = true;
                }
                if (flag) {
                    if (!yesNoInput(scan)) {
                        System.out.println(EXIT_MESSAGE_1);
                        return;
                    }
                }
            }
        } else {
            System.out.println(STARTUP_EXIT_1);

            if (yesNoInput(scan)) {
                System.out.println(EXIT_MESSAGE_1);
                return;
            }
        }
        System.out.println(MESSAGE_4);
        mazeSolver.solveMaze();
        System.out.println(MESSAGE_5);
        mazeSolver.writeSolution(scan.nextLine());
        System.out.println(MESSAGE_6);
        System.out.println(EXIT_MESSAGE_2);
    }

    public static boolean yesNoInput(Scanner scan) {
        while (true) {
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("YES")) {
                return true;
            } else if (input.equalsIgnoreCase("NO")) {
                return false;
            } else {
                System.out.println(TRY_AGAIN);
            }
        }
    }
}
