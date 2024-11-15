package assignment05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

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

    private static Stream<Arguments> insertionsWithExpected(){
        return Stream.of(
                Arguments.of(0, 4, 3, 2, 1),
                Arguments.of(1, 3, 4, 2, 1),
                Arguments.of(2, 3, 2, 4, 1),
                Arguments.of(3, 3, 2, 1, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("insertionsWithExpected")
    void insertAtMiddleIndexWithMultipleElements(int insertIdx, int first, int second, int third, int fourth) {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        list.insertFirst(3);
        list.insert(insertIdx, 4);
        assertEquals(first, list.getFirst());
        assertEquals(second, list.first.next.data);
        assertEquals(third, list.first.next.next.data);
        assertEquals(fourth, list.first.next.next.next.data);
        assertEquals(4, list.size());
    }

    @Test
    void insertOutOfBounds(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        list.insertFirst(3);

        assertThrows(IndexOutOfBoundsException.class, () -> {list.insert(-1, 0);});
        // Assert 4 as 3 is allowed for appending a new element
        assertThrows(IndexOutOfBoundsException.class, () -> {list.insert(4, 0);});
    }

    @Test
    void testGetRetrievesExistingData(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insert(1, 2);
        list.insert(2, 3);
        list.insert(3, 4);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
    }

    @Test
    void getOutOfBounds(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        list.insertFirst(3);

        assertThrows(IndexOutOfBoundsException.class, () -> {list.get(-1);});
        assertThrows(IndexOutOfBoundsException.class, () -> {list.get(3);});
    }

    @Test
    void deleteFirst(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        assertEquals(1, list.size());
        assertEquals(1, list.deleteFirst());
        assertEquals(0, list.size());
        assertNull(list.first);

        list.insertFirst(2);
        list.insertFirst(1);
        assertEquals(2, list.size());
        assertEquals(1, list.deleteFirst());
        assertEquals(1, list.size());
        assertEquals(2, list.first.data);
    }

    @Test
    void deleteFirstThrows(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        assertThrows(NoSuchElementException.class, list::deleteFirst);
    }

    private static Stream<Arguments> deletionsWithExpected(){
        return Stream.of(
                Arguments.of(0, 2, 3, 4, 1),
                Arguments.of(1, 1, 3, 4, 2),
                Arguments.of(2, 1, 2, 4, 3),
                Arguments.of(3, 1, 2, 3, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("deletionsWithExpected")
    void singleDeleteWithMultipleElements(int deleteIdx, int first, int second, int third, int deletedElem) {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        assertEquals(0, list.size());
        list.insertFirst(4);
        list.insertFirst(3);
        list.insertFirst(2);
        list.insertFirst(1);
        assertEquals(4, list.size());
        assertEquals(deletedElem, list.delete(deleteIdx));
        assertEquals(first, list.getFirst());
        assertEquals(second, list.first.next.data);
        assertEquals(third, list.first.next.next.data);
        assertEquals(3, list.size());
    }

    @Test
    void deleteOutOfBounds(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        list.insertFirst(3);

        assertThrows(IndexOutOfBoundsException.class, () -> {list.delete(-1);});
        assertThrows(IndexOutOfBoundsException.class, () -> {list.delete(3);});
        assertEquals(3, list.deleteFirst());
        assertEquals(2, list.deleteFirst());
        assertEquals(1, list.deleteFirst());
        // delete empty list
        assertThrows(IndexOutOfBoundsException.class, () -> {list.delete(0);});
    }

    private static Stream<Arguments> indexOfValues(){
        return Stream.of(
                Arguments.of(-1, -1),
                Arguments.of(1, 0),
                Arguments.of(2, 1),
                Arguments.of(3, 2),
                Arguments.of(4, 3),
                Arguments.of(5, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("indexOfValues")
    void indexOfMultipleElements(int element, int expected) {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(4);
        list.insertFirst(3);
        list.insertFirst(2);
        list.insertFirst(1);
        assertEquals(expected, list.indexOf(element));
    }

    @Test
    void testClear(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        list.insertFirst(3);
        list.insertFirst(4);

        assertEquals(4, list.size());
        list.clear();
        assertEquals(0, list.size());
        assertNull(list.first);

        list.clear(); // Clear empty list
        assertEquals(0, list.size());
        assertNull(list.first);
    }

    @Test
    void toArrayWithValues(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        list.insertFirst(3);
        list.insertFirst(4);

        Object[] expected = new Object[]{4, 3, 2, 1};
        assertEquals(expected.length, list.size());
        assertArrayEquals(expected, list.toArray());

        list.clear();
        assertEquals(0, list.size());
        assertArrayEquals(new Object[]{}, list.toArray());
    }

    @Test
    void iteratorWithForLoop(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insert(1, 2);
        list.insert(2, 3);
        list.insert(3, 4);

        int expected = 1;
        for (int i : list) {
            assertEquals(expected, i);
            expected++;
        }
    }

    @Test
    void iteratorWhileLoop(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insert(1, 2);
        list.insert(2, 3);
        list.insert(3, 4);

        Iterator<Integer> iterator = list.iterator();
        int expected = 1;
        while(iterator.hasNext()){
            assertEquals(expected, iterator.next());
            expected++;
        }

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iteratorRemoveAltersCollection(){
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.insertFirst(1);
        list.insertFirst(2);
        list.insertFirst(3);
        list.insertFirst(4);

        Iterator<Integer> iterator = list.iterator();
        iterator.remove();

        assertEquals(3, list.size());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next());
        assertEquals(3, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, list.size()); // Size remains unchanged after next calls
        assertFalse(iterator.hasNext());
    }
}