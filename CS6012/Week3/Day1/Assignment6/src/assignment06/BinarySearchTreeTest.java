package assignment06;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
        assertNull(bst.root_.left_.left_);
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
    void addAllEmptyList(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        ArrayList<Integer> items = new ArrayList<>();
        assertFalse(bst.addAll(items));
        assertNull(bst.root_);
    }

    @Test
    void containsRootWithLeftRightNodes(){
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.add(3)); // root
        assertTrue(bst.add(1));
        assertTrue(bst.add(4));

        assertTrue(bst.contains(3));
        assertTrue(bst.contains(1));
        assertTrue(bst.contains(4));
    }
}