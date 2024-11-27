package assignment08;

import java.util.ArrayList;

public class Node {
    String value_;
    ArrayList<Node> neighbors_;
    int row_;
    int col_;

    public Node(String value, int row, int col) {
        value_ = value;
        neighbors_ = new ArrayList<Node>();
        row_ = row;
        col_ = col;
    }

    public void addNeighbor(Node neighbor) {
        neighbors_.add(neighbor);
    }

    public ArrayList<Node> neighbors(){
        return neighbors_;
    }

    public String getValue() {
        return value_;
    }
}
