package assignment08;

import java.io.FileNotFoundException;

public class PathFinder {
    /**
     * Find the shortest path for a given maze
     * @param inputFile input file containing unsolved maze
     * @param outputFile output file of maze with populated shortest path
     * @throws FileNotFoundException when the input file in not found
     */
    public static void solveMaze(String inputFile, String outputFile) throws FileNotFoundException{
            Solver mazeSolver = new Solver(inputFile);
            mazeSolver.solve();
            mazeSolver.writeMaze(outputFile);
    }
}
