package assignment08;

import java.util.ArrayList;

/**
 * Representation of a Node for use in assignment08.Graph object
 */
public class Node {
    private String value_;
    private ArrayList<Integer> neighbors_;
    private boolean visited_;
    private Integer cameFrom_;

    public Node(String value) {
        value_ = value;
        neighbors_ = new ArrayList<>();
        visited_ = false;
        cameFrom_ = null;
    }

    /**
     * Add the index of the neighbor to the node.
     * The index is the index of the neighbor in the 1D maze array stored in the Graph class
     * For example:
     * 3 4
     * XXXX
     * XA X
     * XXXX
     * The maze above with height 3 and width 4 as a 1D array follows:
     * XXXXXA XXXXX
     * If this node is A (index) 5, then it's right neighbor is index 6
     * @param neighbor 1D Maze index of
     */
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
