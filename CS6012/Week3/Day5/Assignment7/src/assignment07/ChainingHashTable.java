package assignment07;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class ChainingHashTable implements Set<String> {
    private LinkedList<String>[] storage_;
    int capacity_;
    HashFunctor hashFunctor_;
    int size_;

    @SuppressWarnings("unchecked")
    public ChainingHashTable(int capacity, HashFunctor functor){
        capacity_ = capacity;
        hashFunctor_ = functor;
        storage_ = (LinkedList<String>[]) new LinkedList[capacity];
        size_ = 0;
    }

    /**
     * Get the index of the array bucket (i.e. linkedlist) in the array for a given item
     * @param item item to use to find its respective bucket
     * @return the index of the bucket
     */
    private int bucketLocation(String item){
        return Math.abs(hashFunctor_.hash(item) % storage_.length);
    }

    /**
     * Get the linked list where an item will be stored
     * @param item the item to use to find the linked list
     * @return the linked list in the underlying array
     */
    private LinkedList<String> getBucket(String item){
        return storage_[bucketLocation(item)];
    }

    @Override
    public boolean add(String item) {
        LinkedList<String> bucket = getBucket(item);
        if (bucket == null) {
            storage_[bucketLocation(item)] = new LinkedList<>();
            bucket = getBucket(item);
        }
        if (bucket.contains(item)) {
            return false;
        } else {
            bucket.add(item);
            size_++;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends String> items) {
        boolean tableAltered = false;
        for (String item : items) {
            if (add(item)) {
                tableAltered = true;
            }
        }
        return tableAltered;
    }

    @Override
    public void clear() {
        Arrays.fill(storage_, null);
        size_ = 0;
    }

    @Override
    public boolean contains(String item) {
        LinkedList<String> bucket = getBucket(item);
        if (bucket == null) {
            return false;
        } else
            return bucket.contains(item);
    }

    @Override
    public boolean containsAll(Collection<? extends String> items) {
        for (String item : items) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size_ == 0;
    }

    @Override
    public boolean remove(String item) {
        LinkedList<String> bucket = getBucket(item);
        if (bucket == null) {
            return false;
        }
        boolean result = getBucket(item).remove(item);
        if (result) {
            size_--;
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<? extends String> items) {
        boolean tableAltered = false;
        for (String item : items) {
            if (remove(item)) {
                tableAltered = true;
            }
        }
        return tableAltered;
    }

    @Override
    public int size() {
        return size_;
    }

    /**
     * @return The underlying storage array containing the added elements
     */
    public LinkedList<String>[] getStorage(){
        return storage_;
    }

    /**
     * @return The hash functor used to initialize this object
     */
    public HashFunctor getHashFunctor(){
        return hashFunctor_;
    }
}
