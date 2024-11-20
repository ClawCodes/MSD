package assignment06;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;


/*
Notes:

* Removal operations to consider:
    1. Node is a leaf - removal does not disconnect the tree
    2. Node has only one child - remove node after attaching its parent to child
    3. Removal of node with two children - replace with the smallest item in the right subtree
        - note the smallest will not have a left child, so it's a simple parent-child reconnection
*/

public class BinarySearchTree<T extends Comparable<? super T>> implements SortedSet<T>{
    protected Node<T> root_;
    protected int size_;

    BinarySearchTree(){
        root_ = null;
        size_ = 0;
    }

    class Node<T extends Comparable<? super T>>{
        Node(T data){
            data_ = data;
            left_ = null;
            right_ = null;
        }

        T data_;
        Node<T> left_;
        Node<T> right_;
    }

    private Node<T> add_(T item, Node<T> node){
        if (node == null)
            node = new Node<>(item);
        else if (item.compareTo(node.data_) < 0)
            node.left_ = add_(item, node.left_);
        else if (item.compareTo(node.data_) > 0)
            node.right_ = add_(item, node.right_);
        else
            throw new IllegalArgumentException("item already exists");
        return node;
    }

    @Override
    public boolean add(T item) {
        try {
            root_ = add_(item, root_);
            size_++;
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> items) {
        boolean treeAltered = false;
        for (T item : items) {
            if (item == null) {
                throw new IllegalArgumentException("item cannot be null");
            }
            if(add(item))
                treeAltered = true;
        }
        return treeAltered;
    }

    @Override
    public void clear() {
        root_ = null;
        size_ = 0;
    }

    @Override
    public boolean contains(T item) {
        if (item == null)
            throw new NullPointerException("item cannot be null");

        Node<T> node = root_;
        while (node != null) {
            if (item.compareTo(node.data_) == 0){
                return true;
            } else if (item.compareTo(node.data_) < 0) {
                node = node.left_;
            } else {
                node = node.right_;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<? extends T> items) {
        return false;
    }

    @Override
    public T first() throws NoSuchElementException {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T last() throws NoSuchElementException {
        return null;
    }

    @Override
    public boolean remove(T item) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<? extends T> items) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public ArrayList<T> toArrayList() {
        return null;
    }
}
