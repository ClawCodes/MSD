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
                throw new NullPointerException("item cannot be null");
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
        boolean containsAll = true;
        for (T item : items) {
            if (item == null)
                throw new NullPointerException("item cannot be null");
            if (!contains(item)){
                containsAll = false;
            }
        }
        return containsAll;
    }

    private T min(Node<T> node){
        Node<T> current = node;
        while( current.left_ != null){
            current = current.left_;
        }
        return current.data_;
    }

    @Override
    public T first() throws NoSuchElementException {
        if (root_ == null)
            throw new NoSuchElementException("the set is empty");

        return min(root_);
    }

    @Override
    public boolean isEmpty() {
        return root_ == null;
    }

    @Override
    public T last() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException("the set is empty");

        Node<T> current = root_;
        while( current.right_ != null){
            current = current.right_;
        }

        return current.data_;
    }

    private Node<T> remove_(Node<T> node, T item){
        Node<T> newNode = node;
        if (item.compareTo(node.data_) < 0)
            node.left_ = remove_(node.left_, item);
        else if (item.compareTo(node.data_) > 0)
            node.right_ = remove_(node.right_, item);
        else {
            // successor replacement
            if (node.left_ != null && node.right_ != null){
                T minVal = min(node.right_);
                newNode = new Node<>(minVal);
                remove_(node.right_, minVal);
                newNode.left_ = node.left_;
                newNode.right_ = node.right_;
                return newNode;
            }
            else if (node.left_ != null){
                newNode = node.left_;
            }
            else if (node.right_ != null){
                newNode = node.right_;
            }
            else {
                newNode = null;
            }
            size_--;
        }
        return newNode;
    }

    @Override
    public boolean remove(T item) {
        if (item == null){
            throw new NullPointerException("item cannot be null");
        }
        int originalCount = size_;
        root_ = remove_(root_, item);

        return originalCount > size_;
    }

    @Override
    public boolean removeAll(Collection<? extends T> items) {
        return false;
    }

    @Override
    public int size() {
        return size_;
    }

    @Override
    public ArrayList<T> toArrayList() {
        return null;
    }
}
