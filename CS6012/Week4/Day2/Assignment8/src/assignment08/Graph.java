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

    public int size(){
        return size_;
    }
}
