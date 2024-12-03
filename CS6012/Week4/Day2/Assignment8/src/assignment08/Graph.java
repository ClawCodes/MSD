package assignment08;

import java.util.ArrayList;

/**
 * Graph representation of a collection of Nodes.
 * This implementation stores the graph in a 1D array.
 */
public class Graph {
    ArrayList<Node> vertices_;
    int size_;

    Graph(){
        vertices_ = new ArrayList<>();
    }

    /**
     * Add vertex to graph
     *
     * @param v node instance to add to graph
     */
    public void addVertex(Node v){
        vertices_.add(v);
        size_++;
    }

    /**
     * Get the Node at a given index
     * @param i index of the node
     * @return the Node at the provided index
     */
    public Node getVertex(int i){
        return vertices_.get(i);
    }

    /**
     * Set the value of the node at given index
     * @param i index of node
     * @param val value to set the Node.value_ to
     */
    public void setNodeValue(int i, String val){
        vertices_.get(i).setValue(val);
    }

    /**
     * Set the visited flag of the node at a given index
     * @param i index of node
     * @param visited boolean value of Node.visited_
     */
    public void setNodeVisited(int i, boolean visited){
        vertices_.get(i).setVisited(visited);
    }

    /**
     * Set cameFrom on the Node at a given index
     * @param i index of node
     * @param cameFrom value of cameFrom (this should be the index of another node in the Graph)
     */
    public void setNodeCameFrom(int i, Integer cameFrom){
        vertices_.get(i).cameFrom(cameFrom);
    }

    public int size(){
        return size_;
    }
}
