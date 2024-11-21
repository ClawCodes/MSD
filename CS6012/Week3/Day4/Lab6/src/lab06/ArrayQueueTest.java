package lab06;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ArrayQueueTest {
    @Test
    void addRoot(){
        ArrayQueue<Integer> queue = new ArrayQueue<>();
        queue.add(1);

        assertEquals(1, queue.list.size());
        assertEquals(1, queue.list.getFirst());
    }

    @Test
    void addDescendingOrderArray(){
        ArrayList<Integer> list = new ArrayList<>(
                Arrays.asList(5, 4, 3, 2, 1, 0)
        );
        ArrayQueue<Integer> queue = new ArrayQueue<>(list);
        assertEquals(6, queue.list.size());
        assertEquals(0, queue.list.getFirst());
        assertEquals(2, queue.list.get(1));
        assertEquals(1, queue.list.get(2));
        assertEquals(5, queue.list.get(3));
        assertEquals(3, queue.list.get(4));
        assertEquals(4, queue.list.get(5));
    }

    @Test
    void removeRoot(){
        ArrayList<Integer> list = new ArrayList<>(
                Arrays.asList(5, 4, 3, 2, 1, 0)
        );
        ArrayQueue<Integer> queue = new ArrayQueue<>(list);
        queue.removeMin();
        assertEquals(5, queue.list.size());
        assertEquals(1, queue.list.getFirst());
        assertEquals(2, queue.list.get(1));
        assertEquals(4, queue.list.get(2));
        assertEquals(5, queue.list.get(3));
        assertEquals(3, queue.list.get(4));
    }
}