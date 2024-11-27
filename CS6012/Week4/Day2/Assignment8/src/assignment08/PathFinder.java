package assignment08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PathFinder {
    Graph maze_;
    int startVertex_;
    int endVertex_;
    int height_;
    int width_;

    PathFinder(){
        maze_ = new Graph();
    }

    public static void solveMaze(String inputFile, String outputFile){

    }

    public void readMaze(String inputFile) throws FileNotFoundException {
        FileInputStream inStream = new FileInputStream(inputFile);
        Scanner scanner = new Scanner(inStream);

        if (scanner.hasNextLine()) {
            height_ = scanner.nextInt();
            width_ = scanner.nextInt();
        }

        int row = 0;
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("");
            for (int i = 0; i < line.length; i++) {
                String value = line[i];
                Node node = new Node(value, row, i);
                maze_.addVertex(node);
                if (value.equals("S")) {
                    startVertex_ = maze_.size() - 1;
                } else if (value.equals("G")) {
                    endVertex_ = maze_.size() - 1;;
                }
                row++;
            }
        }
    }
}
