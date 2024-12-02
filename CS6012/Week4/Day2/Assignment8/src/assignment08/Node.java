package assignment08;

import java.util.ArrayList;

public class Node {
    private String value_;
    private ArrayList<Integer> neighbors_;
    private int row_;
    private int col_;
    private boolean visited_;
    private Integer cameFrom_;

    public Node(String value, int row, int col) {
        value_ = value;
        neighbors_ = new ArrayList<>();
        row_ = row;
        col_ = col;
        visited_ = false;
        cameFrom_ = null;
    }

    public void addNeighbor(Integer neighbor) {
        neighbors_.add(neighbor);
    }

    public ArrayList<Integer> neighbors(){
        return neighbors_;
    }

    public String getValue() {
        return value_;
    }

    public void setValue(String value) {
        value_ = value;
    }

    public boolean isVisited() {
        return visited_;
    }
    public void setVisited(boolean visited) {
        visited_ = visited;
    }

    public void cameFrom(Integer cameFrom) {
        cameFrom_ = cameFrom;
    }

    public Integer getCameFrom() {
        return cameFrom_;
    }
}
