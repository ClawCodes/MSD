package assignment03;

import java.util.*;
import java.util.function.Consumer;

/**
 * An ordered set backed by a standard array
 * @param <E> The data type which you want the set to contain
 */
public class BinarySearchSet<E> implements SortedSet<E>, Iterable<E> {
    private E[] items_ = (E[]) new Object[10];
    Comparator<? super E> comparator_ = null;


    /**
     * Default constructor.
     * Initialize your BinarySearchSet with this constructor to use natural ordering between the elements in the set.
     */
    public BinarySearchSet() {
    }

    /**
     * Initialize your BinarySearchSet with this constructor to use a custom comparator when ordering the set
     * @param comparator Comparator implementation to use when comparing elements of the set
     */
    public BinarySearchSet(Comparator<? super E> comparator) {
        comparator_ = comparator;
    }

    /**
     * @return The comparator used to order the elements in this set, or null if
     *         this set uses the natural ordering of its elements (i.e., uses
     *         Comparable).
     */
    @Override
    public Comparator<? super E> comparator() {
        return comparator_;
    }


    /**
     * Compare two elements that are of the same type as the elements in the set
     * @param element1 first object to compare
     * @param element2 second object to compare
     * @return -1 if element1 < element2
     *          1 if element1 > element2
     *          0 if element1 is equivalent to element2
     */
    public int compare(E element1, E element2) {
        if (comparator_ == null) {
            return ((Comparable<? super E>) element1).compareTo(element2);
        }
        return comparator_.compare(element1, element2);
    }

    /**
     * Get the index of an element in the set
     * @param element the element to get the index of
     * @return The index of the element if found, otherwise -1
     */
    private int binarySearch(E element) {
        if (size() == 0){
            return -1;
        }
        int low = 0, high = items_.length - 1, mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (items_[mid] == null){
                high = mid - 1;
                continue;
            }
            if (compare(element, items_[mid]) == 0) {
                return mid;
            } else if (compare(element, items_[mid]) < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    /**
     * Get the index of an element in which it should be inserted into to preserve the order of the set
     * Example:
     *  set = {1, 2, 4, 5}
     *  result = searchInsertPos(3);
     *  result
     *  >>> 2
     * @param element
     * @return
     */
    public int searchInsertPos(E element) {
        if (size() == 0){
            return -1;
        }
        int low = 0, high = items_.length - 1, mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (items_[mid] == null){
                high = mid - 1;
                continue;
            }
            if (compare(element, items_[mid]) == 0) {
                return -1; // Element already exists
            } else if (compare(element, items_[mid]) < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /**
     * Get the first element of the set
     * @return the first element of the set
     * @throws NoSuchElementException if the set is empty
     */
    @Override
    public E first() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The set is empty.");
        }
        return items_[0];
    }

    /**
     * Get the last element of the set
     * @return the last element of the set
     * @throws NoSuchElementException if the set is empty
     */
    @Override
    public E last() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The set is empty.");
        }
        return items_[size() - 1];
    }

    /**
     * Double the capacity of the items_ array when there is no room left to add new elements
     */
    public void increaseCapacity(){
        if (size() == items_.length){
            E[] temp = (E[]) new Object[items_.length * 2];
            for (int i = 0; i < items_.length; i++) {
                temp[i] = items_[i];
            }
            items_ = temp;
        }
    }

    /**
     * Add an element to the set while preserving the set order and uniqueness
     * @param element element to be added to this set
     * @return true if the element was added to the set, false if the element already exists.
     */
    @Override
    public boolean add(E element) {
        int arrSize = size();
        if (arrSize == 0) {
            items_[0] = element;
            return true;
        }
        int insertPos = searchInsertPos(element);
        if (insertPos == -1) {
            return false; // element already exists
        }
        increaseCapacity();
        E temp = element;
        for (int i = insertPos; i < arrSize + 1; i++) {
            if (items_[i] == null){
                items_[i] = temp;
                break;
            }
            if (compare(element, items_[i]) < 0) {
                E current = items_[i];
                items_[i] = temp;
                temp = current;
            }
        }
        return true;
    }

    /**
     * Add all elements in a collection which are not already in the set to the set
     * @param elements collection containing elements to be added to this set
     * @return True if any element is added, otherwise false
     */
    @Override
    public boolean addAll(Collection<? extends E> elements) {
        boolean itemsAdded = false;
        for (E element : elements) {
            if (!contains(element)) {
                add(element);
                itemsAdded = true;
            }
        }
        return itemsAdded;
    }

    /**
     * Remove all elements from the set, creating an empty set
     */
    @Override
    public void clear() {
        for (int i = 0; i < size(); i++) {
            items_[i] = null;
        }
    }

    /**
     * Determine if an element is contained in the set
     * @param element element whose presence in this set is to be tested
     * @return true if the element is contained in the set, otherwise false
     */
    @Override
    public boolean contains(E element) {
        return binarySearch(element) != -1;
    }

    /**
     * Determine if a collection of elements is a subset of the set
     * @param elements collection to be checked for containment in this set
     * @return true if the collection is a subset, otherwise false
     */
    @Override
    public boolean containsAll(Collection<? extends E> elements) {
        for (E element : elements) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if set is empty, otherwise false
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Iterator enabling iteration over the set
     */
    protected class ThisIterator implements Iterator<E> {
        private int position = 0;

        @Override
        public boolean hasNext() {
            if (size() == 0) {
                throw new NoSuchElementException();
            }
            return position < size() - 1;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return items_[position++];
        }

        @Override
        public void remove() {
            if (size() == 0) {
                throw new IllegalStateException();
            }
            items_[size()] = null;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Iterator.super.forEachRemaining(action);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ThisIterator();
    }

    /**
     * Remove an element from the set while preserving order
     * @param element element to be removed from this set, if present
     * @return true if the element was removed, otherwise false
     */
    @Override
    public boolean remove(E element) {
        int index = binarySearch(element);
        if (index == -1) {
            return false;
        } else {
            for (int i = index + 1; i < this.size(); i++) {
                items_[i - 1] = items_[i]; // shift elements left
            }
            items_[size() - 1] = null; // Remove original shift last value will remain if not set to null
            return true;
        }
    }

    /**
     * Remove all elements in the provided collection from the set
     * @param elements collection containing elements to be removed from this set
     * @return true if any elements were removed, otherwise false
     */
    @Override
    public boolean removeAll(Collection<? extends E> elements) {
        boolean elementsRemoved = false;
        for (E element : elements) {
            if (contains(element)) {
                remove(element);
                elementsRemoved = true;
            }
        }
        return elementsRemoved;
    }

    /**
     * @return the number of elements in the set
     */
    @Override
    public int size() {
        int arrSize = 0;
        for (E item : items_) {
            if (item != null) {
                arrSize++;
            } else {
                break; // Reached end of populated values in array
            }
        }
        return arrSize;
    }

    /**
     * @return An array of the set
     */
    @Override
    public E[] toArray() {
        int newSize = size();
        E[] arr = (E[]) new Object[newSize];
        for (int i = 0; i < newSize; i++) {
            arr[i] = items_[i];
        }
        return arr;
    }

    /**
     * Get the set element at a provided index
     * @param index - index of element to get
     * @return the set element
     * @throws IndexOutOfBoundsException if index is out of set bounds
     */
    public E get(int index) throws IndexOutOfBoundsException{
        if (index > size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        return items_[index];
    }
}
