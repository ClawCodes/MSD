package assignment08;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Object which parse maze file into graph and finds the shortest path
 */
public class Solver {
    protected Graph maze_;
    protected int startVertex_;
    protected int endVertex_;
    protected int height_;
    protected int width_;

    Solver(){
        maze_ = new Graph();
    }

    /**
     * Initialize Solver object
     * @param filename file containing maze
     * @throws FileNotFoundException when provided file name is not found
     */
    Solver(String filename) throws FileNotFoundException {
        maze_ = new Graph();
        readMaze(filename);
    }

    /**
     * Fill in the path from the start to the end node
     * using the Node.cameFrom values
     */
    private void setPath(){
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

    /**
     * Write the maze to a file
     * When calling after solve(), the shortest path will be included in the written file.
     * @param filename name of file to write the maze to
     */
    public void writeMaze(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write(height_ + " " + width_ + "\n");
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

    /**
     * Solve the maze using breadth first search
     * This method fills in the shortest path within the internal graph
     */
    public void solve() {
        LinkedList<Integer> queue = new LinkedList<>();
        maze_.setNodeVisited(startVertex_, true);
        queue.offer(startVertex_);
        while(!queue.isEmpty()){
            int nodeIdx = queue.poll();
            if (nodeIdx == endVertex_){
                setPath();
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

    /**
     * Get the index of a given node's neighbors
     * @param row row of the node in the original maze file
     * @param col column of the node in the original maze file
     * @return list of indexes neighboring a node
     */
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

    /**
     * Read file containing maze into internal Graph object
     * @param inputFile file to parse
     * @throws FileNotFoundException when file is not found
     */
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
            for (int col = 0; col < line.length; col++) {
                String value = line[col];
                Node node = new Node(value);
                maze_.addVertex(node);
                for (Integer neighbor : getNeighbors(row, col)){
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

    public Graph getMaze_() {
        return maze_;
    }
}
