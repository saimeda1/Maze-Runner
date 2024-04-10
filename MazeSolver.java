import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * A framework for maze solving.
 *
 * <p>Purdue University -- CS18000 -- Spring 2021</p>
 *
 * @author Sai Meda Purdue CS
 * @version April 03, 2024
 */
public class MazeSolver {
    private Maze maze;

    public MazeSolver() {
    }

    public void readMaze(String filename) throws InvalidMazeException, IOException {
        List<String> lines = new ArrayList<>();

        // Create a BufferedReader to read the file
        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get(filename).toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        if (lines.size() < 4) { // Ensures there's a name, start, end, and at least one row of the maze
            throw new InvalidMazeException("Incomplete maze data");
        }

        String name = lines.get(0).trim();
        if (name.isEmpty()) {
            throw new InvalidMazeException("Maze does not have a name");
        }

        // Parses the start and end positions
        String startLine = lines.get(1).trim();
        if (!startLine.startsWith("Start: ")) {
            throw new InvalidMazeException("Invalid start format");
        }
        int[] start = parseStartEnd(startLine.substring("Start: ".length()));

        String endLine = lines.get(2).trim();
        if (!endLine.startsWith("End: ")) {
            throw new InvalidMazeException("Invalid end format");
        }
        int[] end = parseStartEnd(endLine.substring("End: ".length()));

        // Parses the maze grid
        char[][] grid = new char[lines.size() - 3][];
        for (int i = 3; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            String[] parts = line.split(",");
            if (parts.length == 0) {
                throw new InvalidMazeException("Empty row in maze grid");
            }
            grid[i - 3] = new char[parts.length];
            for (int j = 0; j < parts.length; j++) {
                if (parts[j].length() != 1 || (parts[j].charAt(0) != 'P' && parts[j].charAt(0) != 'W')) {
                    throw new InvalidMazeException("Invalid cell value");
                }
                grid[i - 3][j] = parts[j].charAt(0);
            }
        }

        validateMaze(grid, start, end); // Ensures the maze is valid
        this.maze = new Maze(name, grid, start, end); // Constructs the maze
    }

    private int[] parseStartEnd(String position) throws InvalidMazeException {
        try {
            String[] parts = position.split("-");
            if (parts.length != 2) {
                throw new InvalidMazeException("Invalid start/end position format");
            }
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            return new int[]{row, col};
        } catch (NumberFormatException e) {
            throw new InvalidMazeException("Start/End positions must be integers");
        }
    }

    private void validateMaze(char[][] grid, int[] start, int[] end) throws InvalidMazeException {
        if (grid.length == 0 || grid[0].length == 0) {
            throw new InvalidMazeException("Maze is not rectangular");
        }

        int width = grid[0].length;
        for (char[] row : grid) {
            if (row.length != width) {
                throw new InvalidMazeException("Maze is not rectangular");
            }
            for (char c : row) {
                if (c != 'W' && c != 'P') {
                    throw new InvalidMazeException("Maze contains squares that are not W or P");
                }
            }
        }

        if (!isValidPosition(start, grid) || !isValidPosition(end, grid)) {
            throw new InvalidMazeException("Start or End values are not within the grid");
        }

        if (grid[start[0]][start[1]] != 'P' || grid[end[0]][end[1]] != 'P') {
            throw new InvalidMazeException("Start or End square is not P");
        }
    }

    private boolean isValidPosition(int[] pos, char[][] grid) {
        return pos[0] >= 0 && pos[0] < grid.length && pos[1] >= 0 && pos[1] < grid[0].length;
    }

    public void solveMaze() {
        int rows = maze.getGrid().length;
        int cols = maze.getGrid()[0].length;

        // Queue for BFS that stores the cell positions and the path taken to reach them
        Queue<int[]> queue = new LinkedList<>();

        // Start position
        int[] start = maze.getStart();
        queue.add(new int[]{start[0], start[1], 0}); // The third element is the step count

        // For tracing the path back from end to start
        int[][] prev = new int[rows * cols][2];
        for (int[] a : prev) {
            a[0] = -1;
            a[1] = -1;
        }

        boolean[][] visited = new boolean[rows][cols];
        visited[start[0]][start[1]] = true;

        int[] end = maze.getEnd();
        boolean found = false;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Right, Down, Left, Up

        while (!queue.isEmpty()) {
            int[] cell = queue.remove();
            int r = cell[0];
            int c = cell[1];

            if (r == end[0] && c == end[1]) {
                found = true;
                break; // Exit found
            }

            for (int[] d : directions) {
                int nr = r + d[0];
                int nc = c + d[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc] && maze.getGrid()[nr][nc] == 'P') {
                    queue.add(new int[]{nr, nc, cell[2] + 1});
                    visited[nr][nc] = true;
                    prev[nr * cols + nc] = new int[]{r, c};
                }
            }
        }

        if (found) {
            // Trace back the path from end to start
            LinkedList<int[]> path = new LinkedList<>();
            int[] cur = end;
            while (cur[0] != start[0] || cur[1] != start[1]) {
                path.addFirst(cur);
                int p = prev[cur[0] * cols + cur[1]][0];
                int q = prev[cur[0] * cols + cur[1]][1];
                cur = new int[]{p, q};
            }
            path.addFirst(start); // Add start at the beginning

            // Convert LinkedList to 2D array
            int[][] finalPath = new int[path.size()][2];
            int idx = 0;
            for (int[] p : path) {
                finalPath[idx++] = p;
            }

            maze.setPath(finalPath);
        } else {
            System.out.println("No path found.");
        }
    }


    public void writeSolution(String filename) {
        try {
            FileWriter writer = new FileWriter(new File(filename));
            writer.write(maze.pathString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(); // For this assignment, use e.printStackTrace() in your catch block
        }
    }
}

