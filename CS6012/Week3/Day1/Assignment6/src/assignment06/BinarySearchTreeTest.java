package assignment06;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    @Test
    void addRootNode(){
        BinarySearchTree<String> bst = new BinarySearchTree<>();
        String item = "hello";
        assertTrue(bst.add(item));

        assertEquals(item, bst.root_.data_);
    }

    @Test
    void addChildrenForTreeHeightOne(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(5)); // add root
        assertTrue(bst.add(3)); // add left
        assertTrue(bst.add(7)); // add right

        assertEquals(5, bst.root_.data_);
        assertEquals(3, bst.root_.left_.data_);
        assertNull(bst.root_.left_.right_);
        assertNull(bst.root_.left_.left_);
        assertEquals(7, bst.root_.right_.data_);
        assertNull(bst.root_.right_.right_);
        assertNull(bst.root_.right_.left_);
    }

    @Test
    void addChildrenForTreeHeightTwo(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(5)); // add root
        // Left subtree
        assertTrue(bst.add(3));
        assertTrue(bst.add(1));
        assertTrue(bst.add(2));
        // Right subtree
        assertTrue(bst.add(7)); // add right

        assertEquals(5, bst.root_.data_);
        assertEquals(3, bst.root_.left_.data_);
        assertNull(bst.root_.left_.right_);
        assertEquals(1, bst.root_.left_.left_.data_);
        assertEquals(2, bst.root_.left_.left_.right_.data_);
        assertEquals(7, bst.root_.right_.data_);
        assertNull(bst.root_.right_.right_);
        assertNull(bst.root_.right_.left_);
    }

    @Test
    void addDuplicateItemFails(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(5)); // add root
        assertFalse(bst.add(5));
    }

    @Test
    void addNullFails(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(5)); // add root
        assertFalse(bst.add(null));
    }

    @Test
    void addAllValidItems(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        ArrayList<Integer> items = new ArrayList<>(Arrays.asList(3, 4, 5, 7, 6));
        assertTrue(bst.addAll(items));

        assertEquals(3, bst.root_.data_);
        assertEquals(4, bst.root_.right_.data_);
        assertEquals(5, bst.root_.right_.right_.data_);
        assertEquals(7, bst.root_.right_.right_.right_.data_);
        assertEquals(6, bst.root_.right_.right_.right_.left_.data_);
    }

    @Test
    void addAllThrows(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        ArrayList<Integer> items = new ArrayList<>(Arrays.asList(3, 4, 5, null, 6));

        assertThrows(NullPointerException.class, () -> bst.addAll(items));
    }

    @Test
    void addAllEmptyList(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        ArrayList<Integer> items = new ArrayList<>();
        assertFalse(bst.addAll(items));
        assertNull(bst.root_);
    }

    @Test
    void containsRoot(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(3)); // root

        assertTrue(bst.contains(3));
    }

    @Test
    void containsLeafs(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(6));
        assertTrue(bst.add(4));
        assertTrue(bst.add(1)); // left-side left leaf
        assertTrue(bst.add(5)); // left-side right leaf
        assertTrue(bst.add(8));
        assertTrue(bst.add(9)); // right-side right leaf
        assertTrue(bst.add(7)); // right-side left leaf

        assertTrue(bst.contains(1));
        assertTrue(bst.contains(5));
        assertTrue(bst.contains(9));
        assertTrue(bst.contains(7));
    }

    @Test
    void containsAllValidNodes(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(0));
        assertTrue(bst.add(4));
        assertTrue(bst.add(1));
        assertTrue(bst.add(5));
        assertTrue(bst.add(8));
        assertTrue(bst.add(9));
        assertTrue(bst.add(7));

        ArrayList<Integer> items = new ArrayList<>(Arrays.asList(0, 4, 1, 5, 8, 9, 7));
        assertTrue(bst.containsAll(items));
    }

    @Test
    void containsAllValidNodesExceptOne(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(0));
        assertTrue(bst.add(4));
        assertTrue(bst.add(1));
        assertTrue(bst.add(5));
        assertTrue(bst.add(8));
        assertTrue(bst.add(9));
        assertTrue(bst.add(7));

        ArrayList<Integer> items = new ArrayList<>(Arrays.asList(0, 4, 1, 5, 8, 9, 7, 100));
        assertFalse(bst.containsAll(items));
    }

    @Test
    void containsAllSubSet(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(0));
        assertTrue(bst.add(4));
        assertTrue(bst.add(1));
        assertTrue(bst.add(5));
        assertTrue(bst.add(8));
        assertTrue(bst.add(9));
        assertTrue(bst.add(7));

        ArrayList<Integer> items = new ArrayList<>(Arrays.asList(4, 8, 7));
        assertTrue(bst.containsAll(items));
    }

    @Test
    void containsAllThrows(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(0));
        assertTrue(bst.add(4));
        assertTrue(bst.add(1));
        assertTrue(bst.add(5));
        assertTrue(bst.add(8));
        assertTrue(bst.add(9));
        assertTrue(bst.add(7));

        ArrayList<Integer> items = new ArrayList<>(Arrays.asList(4, null, 7));
        assertThrows(NullPointerException.class, () -> bst.containsAll(items));
    }

    @Test
    void firstReturnsRoot(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(0));
        assertEquals(0, bst.first());
    }

    @Test
    void firstReturnsSmallest(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(3));
        assertTrue(bst.add(4));
        assertTrue(bst.add(5));
        assertTrue(bst.add(2));
        assertTrue(bst.add(1));
        assertEquals(1, bst.first());
    }

    @Test
    void firstThrowsWithEmptySet(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertThrows(NoSuchElementException.class, bst::first);
    }

    @Test
    void lastReturnsRoot(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(0));
        assertEquals(0, bst.first());
    }

    @Test
    void lastReturnsSmallest(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(3));
        assertTrue(bst.add(4));
        assertTrue(bst.add(5));
        assertTrue(bst.add(2));
        assertTrue(bst.add(1));
        assertEquals(5, bst.last());
    }

    @Test
    void lastThrowsWithEmptySet(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertThrows(NoSuchElementException.class, bst::last);
    }

    private static Stream<Arguments> removeLeafs(){
        return Stream.of(
                Arguments.of(Arrays.asList(3, 2, 1, 4, 5), 1),
                Arguments.of(Arrays.asList(3, 2, 1, 4, 5), 5),
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 4),
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 2),
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 6),
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 8),
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 10)
        );
    }

    @ParameterizedTest
    @MethodSource("removeLeafs")
    void removeLeafNode(List<Integer> items, int removeItem){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int item : items) {
            bst.add(item);
        }

        assertTrue(bst.remove(removeItem));
        assertFalse(bst.contains(removeItem));

        for (int item : items) {
            if (item != removeItem) {
                assertTrue(bst.contains(item));
            }
        }
    }

    private static Stream<Arguments> removeNodesWithSingleChild(){
        return Stream.of(
                Arguments.of(Arrays.asList(3, 2, 1, 4, 5), 2),
                Arguments.of(Arrays.asList(3, 2, 1, 4, 5), 4),
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("removeLeafs")
    void removeSingleChildNodes(List<Integer> items, int removeItem){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int item : items) {
            bst.add(item);
        }

        assertTrue(bst.remove(removeItem));
        assertFalse(bst.contains(removeItem));

        for (int item : items) {
            if (item != removeItem) {
                assertTrue(bst.contains(item));
            }
        }
    }

    private static Stream<Arguments> removeNodesWithMultipleChildren(){
        return Stream.of(
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 3),
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 7),
                Arguments.of(Arrays.asList(5, 3, 4, 1, 2, 7, 6, 9, 10, 8), 9)
        );
    }

    @ParameterizedTest
    @MethodSource("removeNodesWithMultipleChildren")
    void removeMultiChildrenNode(List<Integer> items, int removeItem){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int item : items) {
            bst.add(item);
        }

        assertTrue(bst.remove(removeItem));
        assertFalse(bst.contains(removeItem));

        for (int item : items) {
            if (item != removeItem) {
                assertTrue(bst.contains(item));
            }
        }
    }

    @Test
    void removeAllNodesContainedInTree(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        ArrayList<Integer> items = new ArrayList<>(Arrays.asList(4, 2, 3, 1, 0, 6, 5, 7, 8));
        assertTrue(bst.addAll(items));
        assertEquals(items.size(), bst.size());
        assertTrue(bst.removeAll(items));
        assertEquals(0, bst.size());
    }

    @Test
    void removeAllNodesSubsetContainedInTree(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        ArrayList<Integer> items = new ArrayList<>(Arrays.asList(4, 2, 3, 1, 0, 6, 5, 7, 8));
        ArrayList<Integer> subset = new ArrayList<>(Arrays.asList(3, 1, 5));
        assertTrue(bst.addAll(items));
        assertEquals(items.size(), bst.size());
        assertTrue(bst.removeAll(subset));
        assertEquals(items.size() - subset.size(), bst.size());
    }

    @Test
    void toArrayList(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.addAll(Arrays.asList(6, 4, 3, 5, 2, 1, 8, 9, 7, 10));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        ArrayList<Integer> actual = bst.toArrayList();
        assertEquals(expected, actual);
    }
}