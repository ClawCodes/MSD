package assignment07;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

class ChainingHashTableTest {
    @Test
    void testConstructor() {
        HashFunctor inputFunctor = new BadHashFunctor();
        ChainingHashTable table = new ChainingHashTable(31, inputFunctor);
        LinkedList<String>[] actual = table.getStorage();

        assertEquals(31, actual.length);
        assertEquals(table.getHashFunctor(), inputFunctor);

        for (LinkedList<String> l : actual) {
            assertNull(l);
        }
    }

    @Test
    void testAddSingleElement() {
        HashFunctor inputFunctor = new BadHashFunctor();
        String item = "test";
        int storageSize = 31;
        int expectedBucketLocation = inputFunctor.hash(item) % storageSize;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);

        assertTrue(table.add(item));
        LinkedList<String>[] actual = table.getStorage();
        assertNotNull(actual[expectedBucketLocation]);
        assertNull(actual[expectedBucketLocation + 1]);
        assertNull(actual[expectedBucketLocation - 1]);
    }

    @Test
    void testReAddDoesNotInsert() {
        HashFunctor inputFunctor = new BadHashFunctor();
        String item = "a";
        int storageSize = 31;
        int expectedBucketLocation = inputFunctor.hash(item) % storageSize;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);

        assertTrue(table.add(item));
        LinkedList<String>[] actual = table.getStorage();
        assertEquals(1, actual[expectedBucketLocation].size());
        assertEquals(1, table.size());
        assertFalse(table.add(item));
        assertEquals(1, actual[expectedBucketLocation].size());
        assertEquals(1, table.size());
    }

    @Test
    void testReAddMultipleBuckets() {
        HashFunctor inputFunctor = new BadHashFunctor();
        String A = "a";
        String B = "b";
        String C = "c";

        int storageSize = 31;

        int aLocation = inputFunctor.hash(A) % storageSize;
        int bLocation = inputFunctor.hash(B) % storageSize;
        int cLocation = inputFunctor.hash(C) % storageSize;

        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);

        assertTrue(table.add(A));
        LinkedList<String>[] actual = table.getStorage();
        assertEquals(1, actual[aLocation].size());
        assertEquals(A, actual[aLocation].getFirst());
        assertEquals(1, table.size());
        assertTrue(table.add(B));
        assertEquals(1, actual[bLocation].size());
        assertEquals(B, actual[bLocation].getFirst());
        assertEquals(2, table.size());
        assertTrue(table.add(C));
        assertEquals(1, actual[cLocation].size());
        assertEquals(C, actual[cLocation].getFirst());
        assertEquals(3, table.size());
    }

    @Test
    void testaddAllEntireCollection(){
        HashFunctor inputFunctor = new BadHashFunctor();
        String A = "a";
        String B = "b";
        String C = "c";

        int storageSize = 31;

        int aLocation = inputFunctor.hash(A) % storageSize;
        int bLocation = inputFunctor.hash(B) % storageSize;
        int cLocation = inputFunctor.hash(C) % storageSize;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);

        ArrayList<String> items = new ArrayList<>(Arrays.asList(A, B, C));
        assertTrue(table.addAll(items));
        LinkedList<String>[] actual = table.getStorage();
        assertEquals(1, actual[aLocation].size());
        assertEquals(A, actual[aLocation].getFirst());
        assertEquals(1, actual[bLocation].size());
        assertEquals(B, actual[bLocation].getFirst());
        assertEquals(1, actual[cLocation].size());
        assertEquals(C, actual[cLocation].getFirst());
        assertEquals(3, table.size());

        int nullCount = 0;
        for (LinkedList<String> l : actual) {
            if (l == null) {
                nullCount++;
            }
        }
        assertEquals(storageSize - items.size(), nullCount);
    }


    @Test
    void testaddAllSecondAddIsFalse(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize  = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);

        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        assertTrue(table.addAll(items));
        assertFalse(table.addAll(items));

        LinkedList<String>[] actual = table.getStorage();
        int nullCount = 0;
        for (LinkedList<String> l : actual) {
            if (l == null) {
                nullCount++;
            } else{
                assertEquals(1, l.size());
            }
        }
        assertEquals(storageSize - items.size(), nullCount);
    }

    @Test
    void testaddAllSecondAddReturnsTrue(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize  = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);

        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        ArrayList<String> newItems = new ArrayList<>(Arrays.asList("d", "e", "f"));
        assertTrue(table.addAll(items));
        assertTrue(table.addAll(newItems));

        LinkedList<String>[] actual = table.getStorage();
        int nullCount = 0;
        for (LinkedList<String> l : actual) {
            if (l == null) {
                nullCount++;
            } else{
                assertEquals(1, l.size());
            }
        }
        assertEquals(storageSize - (items.size() + newItems.size()), nullCount);
    }

    @Test
    void testaddAllMixedAddReturnsTrue(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize  = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);

        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        // Following should only add d as b and c will already be added
        ArrayList<String> newItems = new ArrayList<>(Arrays.asList("d", "b", "c"));
        assertTrue(table.addAll(items));
        assertTrue(table.addAll(newItems));

        LinkedList<String>[] actual = table.getStorage();
        int nullCount = 0;
        for (LinkedList<String> l : actual) {
            if (l == null) {
                nullCount++;
            } else{
                assertEquals(1, l.size());
            }
        }
        assertEquals(storageSize - 4, nullCount);
    }

    @Test
    void testClear(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);
        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        assertTrue(table.addAll(items));
        assertEquals(3, table.size());
        table.clear();
        assertEquals(0, table.size());
        LinkedList<String>[] actual = table.getStorage();
        for (LinkedList<String> l : actual) {
            assertNull(l);
        }
    }

    @Test
    void containsReturnsTrue(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);
        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        assertTrue(table.addAll(items));
        assertTrue(table.contains("a"));
    }

    @Test
    void containsIsCaseSensitive(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);
        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        assertTrue(table.addAll(items));
        assertTrue(table.contains("a"));
        assertFalse(table.contains("A"));
    }

    @Test
    void isEmpty(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);
        assertTrue(table.isEmpty());
        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        assertTrue(table.addAll(items));
        assertFalse(table.isEmpty());
        table.clear();
        assertTrue(table.isEmpty());
    }

    @Test
    void removeSingleItem(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);
        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        int aLocation = inputFunctor.hash("a") % storageSize;
        int bLocation = inputFunctor.hash("b") % storageSize;
        int cLocation = inputFunctor.hash("b") % storageSize;
        assertTrue(table.addAll(items));
        assertEquals(3, table.size());

        assertTrue(table.remove("a"));
        assertEquals(2, table.size());
        LinkedList<String>[] actual = table.getStorage();
        int nullCount = 0;
        for (LinkedList<String> l : actual) {
            if (l == null) {
                nullCount++;
            }
        }
        assertEquals(storageSize - 3, nullCount);
        assertEquals(0, actual[aLocation].size());
        assertEquals(1, actual[bLocation].size());
        assertEquals(1, actual[cLocation].size());
    }

    @Test
    void removeAllItems(){
        HashFunctor inputFunctor = new BadHashFunctor();
        int storageSize = 31;
        ChainingHashTable table = new ChainingHashTable(storageSize, inputFunctor);
        ArrayList<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        int aLocation = inputFunctor.hash("a") % storageSize;
        int bLocation = inputFunctor.hash("b") % storageSize;
        int cLocation = inputFunctor.hash("b") % storageSize;
        assertTrue(table.addAll(items));
        assertEquals(3, table.size());
        assertTrue(table.removeAll(items));
        assertEquals(0, table.size());

        LinkedList<String>[] actual = table.getStorage();
        assertEquals(0, actual[aLocation].size());
        assertEquals(0, actual[bLocation].size());
        assertEquals(0, actual[cLocation].size());
    }
}