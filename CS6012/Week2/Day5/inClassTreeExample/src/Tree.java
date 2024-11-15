import java.util.ArrayList;

public class Tree<T> {
    private Node<T> root;

    private class Node<T> {
        private T data;
        private ArrayList<Node<T>> children;
    }
}


public class BinaryTree<T> {
    private Node<T> root;
    private Node<T> leftChild;
    private Node<T> rightChild;

    private class Node<T> {
        private T data;
        private ArrayList<Node<T>> children;
    }
}
