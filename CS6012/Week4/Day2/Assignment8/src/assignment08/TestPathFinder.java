package assignment08;

import java.io.FileNotFoundException;

public class TestPathFinder {

    public static void main(String[] args) throws FileNotFoundException {

        /*
         * The below code assumes you have a file "tinyMaze.txt" in your project folder.
         * If PathFinder.solveMaze is implemented, it will create a file "tinyMazeOutput.txt" in your project folder.
         *
         * REMEMBER - You have to refresh your project to see the output file in your package explorer.
         * You are still required to make JUnit tests...just lookin' at text files ain't gonna fly.
         */
        PathFinder.solveMaze("tinyMaze.txt", "tinyMazeOutput.txt");
        PathFinder.solveMaze("straight.txt", "straightOutput.txt");
        PathFinder.solveMaze("bigMaze.txt", "bigMazeOutput.txt");
        PathFinder.solveMaze("mediumMaze.txt", "mediumMazeOutput.txt");
    }


}