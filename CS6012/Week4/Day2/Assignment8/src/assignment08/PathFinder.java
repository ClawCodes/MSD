package assignment08;

import java.io.FileNotFoundException;

public class PathFinder {
    public static void solveMaze(String inputFile, String outputFile) {
        try {
            Solver mazeSolver = new Solver(inputFile);
            mazeSolver.solve();
            mazeSolver.writeMaze(outputFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
