package assignment06;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;


public class BinarySearchTree<T extends Comparable<? super T>> implements SortedSet<T>{
    protected Node<T> root_;
    protected int size_;

    public BinarySearchTree(){
        root_ = null;
        size_ = 0;
    }

    protected static class Node<T extends Comparable<? super T>>{
        Node(T data){
            data_ = data;
            left_ = null;
            right_ = null;
        }

        T data_;
        Node<T> left_;
        Node<T> right_;
    }

    /**
     * Recursive method intended to be called by `add` driver method.
     * Add new node to tree preserving BST rules.
     * Set the provided root to the return of the method.
     * Example:
     * T item = 5;
     * root = add_(item, root);
     *
     * @param item the item to add to the tree
     * @param node root of tree to add the item too
     * @return the new tree with added item
     */
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

    /**
     * Get the minimum value in the tree
     *
     * @param node root node to tree to traverse
     * @return minimum value in tree
     */
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
        if (node == null){
            return node;
        }
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
        if (isEmpty()){
            return false;
        }
        int originalCount = size_;
        root_ = remove_(root_, item);

        return originalCount > size_;
    }

    @Override
    public boolean removeAll(Collection<? extends T> items) {
        boolean treeAltered = false;
        for (T item : items) {
            if (remove(item)){
                treeAltered = true;
            };
        }
        return treeAltered;
    }

    @Override
    public int size() {
        return size_;
    }

    /**
     * Insert elements of the tree into provided ArrayList instance in ascending order.
     * @param out the array list to append to
     * @param node root node of tree to traverse
     */
    private void DFTAdd(ArrayList<T> out, Node<T> node){
        if (node == null){
            return;
        }
        DFTAdd(out, node.left_);
        out.add(node.data_);
        DFTAdd(out, node.right_);
    }

    @Override
    public ArrayList<T> toArrayList() {
        ArrayList<T> out = new ArrayList<>();
        DFTAdd(out, root_);
        return out;
    }
}
