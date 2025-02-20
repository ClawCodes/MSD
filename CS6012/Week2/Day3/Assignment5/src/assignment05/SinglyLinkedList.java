package assignment05;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class SinglyLinkedList<T> implements List<T> {
    protected Node<T> first = null;
    int size = 0;

    public SinglyLinkedList() {
    }

    public static class Node<T> {
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
        // Include size for appending last element
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " is out of bounds " + " of list length " + size()
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
        if (first == null) {
            throw new NoSuchElementException("List is empty");
        }
        return first.data;
    }

    void checkIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " is out of bounds " + " of list length " + size()
            );
        }
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        checkIndex(index);
        Node<T> current = first;
        int listIndex = 0;
        while (listIndex < index) {
            current = current.next;
            if (current == null) {
                throw new IndexOutOfBoundsException("Unexpected null pointer at index " + listIndex);
            }
            listIndex++;
        }
        return current.data;
    }

    @Override
    public T deleteFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T data = first.data;
        first = first.next;
        size--;
        return data;
    }

    @Override
    public T delete(int index) throws IndexOutOfBoundsException {
        checkIndex(index);
        if (index == 0) {
            return deleteFirst();
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
        Node<T> toDelete = current.next;
        current.next = toDelete.next;
        size--;
        return toDelete.data;
    }

    @Override
    public int indexOf(Object element) {
        Node<T> current = first;
        for (int i = 0; i < size(); i++) {
            if (current.data.equals(element)) {
                return i;
            } else {
                current = current.next;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public void clear() {
        first = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        Node<T> current = first;
        for (int i = 0; i < size(); i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    /**
     * Iterator enabling iteration over the linked list
     */
    private class ThisIterator implements Iterator<T> {
        private int position = 0;
        private Node<T> current = first;
        boolean nextCalledLast = false;
        // Save initial size here as the SLL size can change from remove calls
        // But we can still iterate through all elements which existed upon initialization of the iterator
        int iterSize = size();

        @Override
        public boolean hasNext() {
            if (iterSize == 0) {
                throw new NoSuchElementException();
            }
            nextCalledLast = false;
            return position < iterSize;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T returnValue = current.data;
            position++;
            current = current.next;
            nextCalledLast = true;
            return returnValue;
        }

        @Override
        public void remove() {
            if (!nextCalledLast || iterSize == 0) {
                throw new IllegalStateException();
            }
            delete(position - 1);
            nextCalledLast = false;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Iterator.super.forEachRemaining(action);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ThisIterator();
    }

    public SinglyLinkedList<T> reverse(){
        if (isEmpty()){
            return this;
        }
        SinglyLinkedList<T> reversed = new SinglyLinkedList<>();
        Node<T> current = first;
        while(current.next != null){
            reversed.insertFirst(current.data);
            current = current.next;
        }
        reversed.insertFirst(current.data);
        return reversed;
    }
}
