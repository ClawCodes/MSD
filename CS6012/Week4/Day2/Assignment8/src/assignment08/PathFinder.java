package assignment08;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class PathFinder {
    static Graph maze_;
    static int startVertex_;
    static int endVertex_;
    static int height_;
    static int width_;

    PathFinder(){
        maze_ = new Graph();
    }

    public static void setPath(){
        Node goal = maze_.getVertex(endVertex_);
        Integer cameFrom = goal.getCameFrom();
        if (cameFrom == null){
            return; // no solution found
        }
        while (cameFrom != startVertex_) {
            maze_.setNodeValue(cameFrom, ".");
            cameFrom = maze_.getVertex(cameFrom).getCameFrom();
        }
    }

    public static void writeMaze(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (int row = 0; row < height_; row++) {
                String line = "";
                for (int col = 0; col < width_; col++) {
                    line += maze_.getVertex(row * (width_) + col).getValue();
                }
                writer.write(line + "\n");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void solveMaze(String inputFile, String outputFile) {
        PathFinder pf = new PathFinder();
        try {
            pf.readMaze(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        LinkedList<Integer> queue = new LinkedList<>();
        maze_.setNodeVisited(startVertex_, true);
        queue.offer(startVertex_);
        while(!queue.isEmpty()){
            int nodeIdx = queue.poll();
            if (nodeIdx == endVertex_){
                setPath();
                writeMaze(outputFile);
                break;
            }
            for (Integer neighborIdx : maze_.getVertex(nodeIdx).neighbors()) {
                Node neighbor = maze_.getVertex(neighborIdx);
                // Find neighbors which have not been visited and are not a wall
                if (!neighbor.isVisited() && !neighbor.getValue().equals("X")) {
                    queue.offer(neighborIdx);
                    maze_.setNodeVisited(neighborIdx, true);
                    maze_.setNodeCameFrom(neighborIdx, nodeIdx);
                }
            }
        }
    }

    public void setWidth(int width){
        width_ = width;
    }

    public void setHeight(int height){
        height_ = height;
    }

    public ArrayList<Integer> getNeighbors(int row, int col){
        if (row >= height_ || col >= width_) {
            throw new IndexOutOfBoundsException("Row or column out of bounds.");
        }
        ArrayList<Integer> neighbors = new ArrayList<>();
        // Get the index of the node based on the row and column
        int sourceIndex = row * (width_) + col;
        // top neighbor
        if (row > 0){
            neighbors.add(sourceIndex - width_);
        }
        // left neighbor
        if (col != 0){
            neighbors.add(sourceIndex - 1);
        }
        // right neighbor
        if (col < width_ - 1){
            neighbors.add(sourceIndex + 1);
        }
        // bottom neighbor
        if (row < height_ - 1){
            neighbors.add(sourceIndex + width_);
        }
        return neighbors;
    }

    public void readMaze(String inputFile) throws FileNotFoundException {
        FileInputStream inStream = new FileInputStream(inputFile);
        Scanner scanner = new Scanner(inStream);

        if (scanner.hasNextLine()) {
            height_ = scanner.nextInt();
            width_ = scanner.nextInt();
            scanner.nextLine(); // Move cursor to next line
        }

        int row = 0;
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("");
            for (int i = 0; i < line.length; i++) {
                String value = line[i];
                Node node = new Node(value, row, i);
                maze_.addVertex(node);
                for (Integer neighbor : getNeighbors(row, i)){
                    node.addNeighbor(neighbor);
                }
                if (value.equals("S")) {
                    startVertex_ = maze_.size() - 1;
                } else if (value.equals("G")) {
                    endVertex_ = maze_.size() - 1;;
                }
            }
            row++;
        }
    }
}
