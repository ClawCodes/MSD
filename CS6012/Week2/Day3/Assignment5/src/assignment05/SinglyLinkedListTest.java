package assignment05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SinglyLinkedListTest {
    @Test
    void testSinglyLinkedListInsertFirstWithNoExisting() {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        assertEquals(1, list.getFirst());
    }

    @Test
    void testSinglyLinkedListInsertFirstWithExisting() {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        assertEquals(2, list.getFirst());
    }

    @Test
    void insertAtLastIndexWithSingleElement(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insert(1, 2);
        assertEquals(1, list.getFirst());
        assertEquals(2, list.first.next.data);
    }

    @Test
    void insertAtFirstIndexWithSingleElement(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insert(0, 2);
        assertEquals(2, list.getFirst());
        assertEquals(1, list.first.next.data);
    }

    @Test
    void insertAtMiddleIndexWithTwoElements() {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        list.insert(1, 3);
        assertEquals(1, list.getFirst());
        assertEquals(3, list.first.next.data);
        assertEquals(2, list.first.next.next.data);
    }
}