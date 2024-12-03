package assignment08;

import java.io.FileNotFoundException;

public class PathFinder {
    public static void solveMaze(String inputFile, String outputFile) throws FileNotFoundException{
            Solver mazeSolver = new Solver(inputFile);
            mazeSolver.solve();
            mazeSolver.writeMaze(outputFile);
    }
}
