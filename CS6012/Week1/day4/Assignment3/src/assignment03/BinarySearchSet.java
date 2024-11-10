package assignment03;

import java.util.*;
import java.util.function.Consumer;

public class BinarySearchSet<E> implements SortedSet<E>, Iterable<E> {
    private E[] items_ = (E[]) new Object[10];
    Comparator<? super E> comparator_ = null;

    public BinarySearchSet() {
    }

    public BinarySearchSet(Comparator<? super E> comparator) {
        comparator_ = comparator;
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator_;
    }

    private int compare(E element1, E element2) {
        if (comparator_ == null) {
            return ((Comparable<? super E>) element1).compareTo(element2);
        }
        return comparator_.compare(element1, element2);
    }

    private int binarySearch(E element) {
        int low = 0, high = items_.length - 1, mid;
        while (low <= high) {
            mid = (low + high) / 2;
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

    @Override
    public E first() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The set is empty.");
        }
        return items_[0];
    }

    @Override
    public E last() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The set is empty.");
        }
        return items_[size() - 1];
    }

    /**
     * Double the capactiy of the items_ array when there is no room left to add new elements
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

    @Override
    public boolean add(E element) {
        if (binarySearch(element) >= 0) {
            return false; // element already exists
        }
        increaseCapacity();
        E temp = element;
        for (int i = 0; i < size(); i++) {
            if (compare(element, items_[i]) > 0) {
                E current = items_[i];
                items_[i] = temp;
                if (current == null){
                    break;
                }
                temp = current;
            }
        }
        return true;
    }

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

    @Override
    public void clear() {
        for (int i = 0; i < size(); i++) {
            items_[i] = null;
        }
    }

    @Override
    public boolean contains(E element) {
        return binarySearch(element) != -1;
    }

    @Override
    public boolean containsAll(Collection<? extends E> elements) {
        for (E element : elements) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

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

    @Override
    public E[] toArray() {
        E[] arr = (E[]) new Object[items_.length];
        for (int i = 0; i < items_.length; i++) {
            arr[i] = items_[i];
        }
        return arr;
    }
}
