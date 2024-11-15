package assignment05;

import java.util.NoSuchElementException;

public class LinkedListStack<T> implements Stack<T> {
    private SinglyLinkedList<T> stack_ = new SinglyLinkedList<T>();

    @Override
    public void clear() {
        stack_.clear();
    }

    @Override
    public boolean isEmpty() {
        return stack_.isEmpty();
    }

    @Override
    public T peek() throws NoSuchElementException {
        return stack_.getFirst();
    }

    @Override
    public T pop() throws NoSuchElementException {
        return stack_.deleteFirst();
    }

    @Override
    public void push(T element) {
        stack_.insertFirst(element);
    }

    @Override
    public int size() {
        return stack_.size();
    }
}
