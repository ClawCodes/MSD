package assignment03;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class BinarySearchSet<E> implements Set<E>, Iterable<E> {
    E[] items_ = (E[]) new Object[10];

    public BinarySearchSet(E element) {
        // Create the sorted set
        // Use natural ordering (i.e. compareTo)
        // Need to cast E to Comparable to call compareTo
    }

    public BinarySearchSet(Comparator<? super E> comparator){
        // Elements are orded using the provided comparator
        //
    }

    // TODO: It would be a good idea to write a helper method to perform the comparison using either compareTo or a Comparator as appropriate to simplify code that needs to compare elements!
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }
}
