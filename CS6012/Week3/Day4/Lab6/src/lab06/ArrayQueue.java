package lab06;

import java.util.ArrayList;

public class ArrayQueue<T extends Comparable<T>> implements PriorityQueue<T> {
    protected ArrayList<T> list = new ArrayList<>();
    private int size = 0;

    public ArrayQueue() {}

    public ArrayQueue(ArrayList<T> list) {
        buildHeap(list);
    }

    private int getParent(int index) {
        return (index - 1) / 2;
    }

    private void percolateUp(int index) {
        int parentIdx = getParent(index);
        if (parentIdx < 0) {
            return;
        }
        T parent = list.get(parentIdx);
        T child = list.get(index);
        if (parent.compareTo(child) > 0) {
            // swap parent and child
            list.set(parentIdx, child);
            list.set(index, parent);
            percolateUp(parentIdx);
        }
    }

    private int getLeftChild(int index) {
        return (2 * index) + 1;
    }

    private int getRightChild(int index) {
        return (2 * index) + 2;
    }

    private boolean inBounds(int index) {
        return index >= 0 && index < list.size();
    }

    private void percolateDown(int index) {
        int leftChildIdx = getLeftChild(index);
        int rightChildIdx = getRightChild(index);

        // If node doesn't have a left child then we reached the bottom of the tree
        if (!inBounds(leftChildIdx)) {
            return;
        }

        T current = list.get(index);
        T leftChild = list.get(leftChildIdx);

        // Node only has left child - perform single swap only if necessary
        if (!inBounds(rightChildIdx)) {
            if (current.compareTo(leftChild) < 0) {
                return;
            }
            list.set(index, leftChild);
            list.set(leftChildIdx, current);
            return;
        }

        T rightChild = list.get(rightChildIdx);

        // Base case - current node is the minimum
        if (current.compareTo(rightChild) < 0 && current.compareTo(leftChild) < 0) {
            return;
        }

        int swapIndex = leftChildIdx;
        T swapValue = leftChild;
        if (leftChild.compareTo(rightChild) > 0) {
            swapIndex = rightChildIdx;
            swapValue = rightChild;
        }

        list.set(index, swapValue);
        list.set(swapIndex, current);
        percolateDown(swapIndex);
    }

    private void buildHeap(ArrayList<T> list) {
        for (T item : list) {
            add(item);
        }
    }

    @Override
    public void add(T element) {
        list.add(element);
        percolateUp(list.size() - 1);
        size++;
    }

    @Override
    public T removeMin() {
        T min = list.getFirst();
        list.set(0, list.getLast());
        list.removeLast();
        percolateDown(0);
        size--;
        return min;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
