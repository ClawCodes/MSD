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
    Node<T> root_;

    BinarySearchTree(){
        root_ = null;
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

    @Override
    public boolean add(T item) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> items) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(T item) {
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
