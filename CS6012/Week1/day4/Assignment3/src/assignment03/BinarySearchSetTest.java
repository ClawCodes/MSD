package assignment03;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchSetTest {
    @Test
    public void testNaturalOrderConstructorInt(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();

        assertEquals(set.compare(1, 2), -1);
        assertEquals(set.compare(2, 1), 1);
        assertEquals(set.compare(1, 1), 0);
    }

    @Test
    public void testNaturalOrderConstructorString(){
        BinarySearchSet<String> set = new BinarySearchSet<>();

        assertEquals(set.compare("A", "B"), -1);
        assertEquals(set.compare("B", "A"), 1);
        assertEquals(set.compare("A", "A"), 0);
    }

    @Test
    public void testAddSingleElement(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();

        assertTrue(set.isEmpty());
        assertTrue(set.add(1));
        assertFalse(set.isEmpty());
        assertEquals(1, set.get(0));
    }

    @Test
    public void testAddMultipleElements(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();

        assertTrue(set.isEmpty());
        assertTrue(set.add(3));
        assertFalse(set.isEmpty());
        assertEquals(3, set.get(0));

        assertTrue(set.add(1));
        assertEquals(1, set.get(0));
        assertEquals(3, set.get(1));

        assertTrue(set.add(2));
        assertEquals(1, set.get(0));
        assertEquals(2, set.get(1));
        assertEquals(3, set.get(2));
    }

    @Test
    public void testAddMultipleElementsWithCapacityIncrease(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        assertTrue(set.add(11));
        assertTrue(set.add(10));
        assertTrue(set.add(9));
        assertTrue(set.add(8));
        assertTrue(set.add(7));
        assertTrue(set.add(6));
        assertTrue(set.add(5));
        assertTrue(set.add(4));
        assertTrue(set.add(3));
        assertTrue(set.add(2));
        assertTrue(set.add(1));
        assertTrue(set.add(0));

        assertEquals(12, set.size());
        assertEquals(0, set.get(0));
        assertEquals(11, set.get(11));
    }

    @Test
    public void testAddDuplicateElements(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        assertTrue(set.add(1));

        assertEquals(1, set.size());
        assertFalse(set.add(1));
        assertEquals(1, set.size()); // No size change
    }

    @Test
    public void testAddAllIntegers(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allInts = Arrays.asList(2, 5, 3, 1, 4);

        assertTrue(set.addAll(allInts));
        assertEquals(5, set.size());
        assertEquals(1, set.get(0));
        assertEquals(2, set.get(1));
        assertEquals(3, set.get(2));
        assertEquals(4, set.get(3));
        assertEquals(5, set.get(4));
    }

    @Test
    public void testAddAllIntegersThatAlreadyExist(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allInts = Arrays.asList(2, 5, 3, 1, 4);

        assertTrue(set.addAll(allInts));
        assertEquals(5, set.size());

        List<Integer> newInts = Arrays.asList(1, 2, 3);
        assertFalse(set.addAll(newInts));

        assertEquals(5, set.size());
        assertEquals(1, set.get(0));
        assertEquals(2, set.get(1));
        assertEquals(3, set.get(2));
        assertEquals(4, set.get(3));
        assertEquals(5, set.get(4));
    }

    @Test
    public void testAddAllEmptyList(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        ArrayList<Integer> allInts = new ArrayList<>();

        assertFalse(set.addAll(allInts));
        assertEquals(0, set.size());
    }

    @Test
    public void clearPopulatedArray(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allInts = Arrays.asList(2, 5, 3, 1, 4);

        assertTrue(set.addAll(allInts));
        assertEquals(5, set.size());

        set.clear();
        assertEquals(0, set.size());
    }

    @Test
    public void clearEmptyArray(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        assertEquals(0, set.size());
        set.clear();
        assertEquals(0, set.size());
    }

    @Test
    public void containsExistingElement(){
        BinarySearchSet<String> set = new BinarySearchSet<>();
        List<String> allElems = Arrays.asList("Hi", "This", "Is", "A", "Test");
        assertTrue(set.addAll(allElems));

        assertTrue(set.contains("Hi"));
        assertTrue(set.contains("Test"));
    }

    @Test
    public void doesNotContainExistingElement(){
        BinarySearchSet<String> set = new BinarySearchSet<>();
        List<String> allElems = Arrays.asList("Hi", "This", "Is", "A", "Test");
        assertTrue(set.addAll(allElems));

        assertFalse(set.contains("NOT AN ELEMENT"));

        set.clear();
        assertFalse(set.contains("NOT AN ELEMENT")); // Check in empty set
    }

    @Test
    public void containsAllElements(){
        BinarySearchSet<String> set = new BinarySearchSet<>();
        List<String> allElems = Arrays.asList("Hi", "This", "Is", "A", "Test");
        assertTrue(set.addAll(allElems));

        assertTrue(set.containsAll(allElems));
    }

    @Test
    public void doesNotContainAllElements(){
        BinarySearchSet<String> set = new BinarySearchSet<>();
        List<String> allElems = Arrays.asList("Hi", "This", "Is", "A", "Test");
        assertTrue(set.addAll(allElems));

        List<String> newElems = Arrays.asList("Hi", "This", "Is", "A", "Test", "NEW ELEMENT");
        assertFalse(set.containsAll(newElems));

        set.clear();
        assertFalse(set.containsAll(allElems)); // assert check against empty set
    }

    @Test
    public void iteratorWithForLoop(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allElems = Arrays.asList(1, 2, 3, 4, 5);
        assertTrue(set.addAll(allElems));

        int expected = 1;
        for (Integer i : set) {
            assertEquals(expected, i);
            expected++;
        }
    }

    @Test
    public void iteratorWithWhileLoop(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allElems = Arrays.asList(1, 2, 3, 4, 5);
        assertTrue(set.addAll(allElems));

        var it = set.iterator();

        int expected = 1;
        while (it.hasNext()) {
            var elem = it.next();
            assertEquals(expected, elem);
            expected++;
        }
    }

    @Test
    public void remove(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allElems = Arrays.asList(1, 2, 3, 4, 5);
        assertTrue(set.addAll(allElems));
        assertEquals(5, set.size());

        // Remove element from start
        assertTrue(set.remove(1));
        assertEquals(4, set.size());
        assertEquals(2, set.get(0));
        assertEquals(3, set.get(1));
        assertEquals(4, set.get(2));
        assertEquals(5, set.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> set.get(4));

        // Remove element from middle
        assertTrue(set.remove(4));
        assertEquals(3, set.size());
        assertEquals(2, set.get(0));
        assertEquals(3, set.get(1));
        assertEquals(5, set.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> set.get(3));


        // Remove element from end
        assertTrue(set.remove(5));
        assertEquals(2, set.size());
        assertEquals(2, set.get(0));
        assertEquals(3, set.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> set.get(2));

        // Remove from empty set
        set.clear();
        assertEquals(0, set.size());
        assertFalse(set.remove(2));
    }

    @Test
    public void removeAllElements(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allElems = Arrays.asList(1, 2, 3, 4, 5);
        assertTrue(set.addAll(allElems));
        assertEquals(5, set.size());

        assertTrue(set.removeAll(allElems));
        assertEquals(0, set.size());
    }

    @Test
    public void removeSomeElements(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allElems = Arrays.asList(1, 2, 3, 4, 5);
        assertTrue(set.addAll(allElems));
        assertEquals(5, set.size());

        assertTrue(set.removeAll(Arrays.asList(1, 3, 4)));
        assertEquals(2, set.size());
        assertEquals(2, set.get(0));
        assertEquals(5, set.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> set.get(2));
    }

    @Test
    public void toArrayWithPopulatedValues(){
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        List<Integer> allElems = Arrays.asList(2, 3, 1, 5, 4);
        assertTrue(set.addAll(allElems));

        Object[] arr = set.toArray();

        assertEquals(5, arr.length);
        assertEquals(1, arr[0]);
        assertEquals(2, arr[1]);
        assertEquals(3, arr[2]);
        assertEquals(4, arr[3]);
        assertEquals(5, arr[4]);
    }
}