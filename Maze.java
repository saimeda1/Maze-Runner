public class Maze {
    private final String name;
    private final char[][] grid;
    private final int[] start;
    private final int[] end;
    private int[][] path;

    // Constructor
    public Maze(String name, char[][] grid, int[] start, int[] end) {
        this.name = name;
        this.grid = grid;
        this.start = start;
        this.end = end;
        this.path = null; // Initially, the path is not known
    }

    // Getters
    public String getName() {
        return name;
    }

    public int[] getStart() {
        return start;
    }

    public int[] getEnd() {
        return end;
    }

    public char[][] getGrid() {
        return grid;
    }

    public int[][] getPath() {
        return path;
    }

    // Setter for path
    public void setPath(int[][] path) {
        this.path = path;
    }

    // Method to generate path string representation
    public String pathString() {
        if (path == null || path.length == 0) {
            return "No solution found.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("Moves: ").append(path.length).append("\n");
        sb.append("Start").append("\n");
        for (int[] move : path) {
            sb.append(move[0]).append("-").append(move[1]).append("\n");
        }
        sb.append("End");
        return sb.toString();
    }

    // toString method to replicate the input file format
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("Start: ").append(start[0]).append("-").append(start[1]).append("\n");
        sb.append("End: ").append(end[0]).append("-").append(end[1]).append("\n");
        for (char[] row : grid) {
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i]);
                if (i < row.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }
        return sb.toString().trim(); // To avoid trailing newline
    }
}

