package assignment08;

import java.util.ArrayList;

public class Graph {
    ArrayList<Node> vertices_;
    int size_;

    Graph(){
        vertices_ = new ArrayList<>();
    }
    public void addVertex(Node v){
        vertices_.add(v);
        size_++;
    }

    public Node getVertex(int i){
        return vertices_.get(i);
    }

    public void setNodeValue(int i, String val){
        vertices_.get(i).setValue(val);
    }

    public void setNodeVisited(int i, boolean visited){
        vertices_.get(i).setVisited(visited);
    }

    public void setNodeCameFrom(int i, Integer cameFrom){
        vertices_.get(i).cameFrom(cameFrom);
    }

    public int size(){
        return size_;
    }
}
