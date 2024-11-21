package lab06;

import java.util.TreeSet;

public class TreeSetQueue<T extends Comparable<T>> implements PriorityQueue<T> {
    TreeSet<T> queue;

    public TreeSetQueue() {
        queue = new TreeSet<>();
    }

    @Override
    public void add(T element) {
        queue.add(element);
    }

    @Override
    public T removeMin() {
        return queue.pollFirst();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
