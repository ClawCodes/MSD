package assignment05;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SinglyLinkedList<T> implements List<T> {
    protected Node<T> first = null;
    protected Node<T> last = null;
    int size = 0;

    SinglyLinkedList(){}

    protected static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    @Override
    public void insertFirst(Object element) {
        if (first == null) {
            first = new Node(element);
        } else {
            Node<T> newNode = new Node(element);
            newNode.next = first;
            first = newNode;
        }
        size++;
    }

    @Override
    public void insert(int index, Object element) throws IndexOutOfBoundsException {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException(
                    "Element " + index + " is out of bounds " + " of list length " + size()
            );
        }

        if (index == 0) {
            insertFirst(element);
            return;
        }

        Node<T> current = first;
        int listIndex = 1;
        while (listIndex < index) {
            current = current.next;
            if (current == null) {
                throw new IndexOutOfBoundsException("Unexpected null pointer at index " + listIndex);
            }
            listIndex++;
        }
        Node<T> newNode = new Node(element);
        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    @Override
    public T getFirst() throws NoSuchElementException {
        return first.data;
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public T deleteFirst() throws NoSuchElementException {
        return null;
    }

    @Override
    public T delete(int index) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public int indexOf(Object element) {
        return 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
