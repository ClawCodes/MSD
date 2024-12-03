package assignment09;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BSPTreeTest {
    @Test
    void testSingleRootNodeConstruction() {
        Segment segment = new Segment(0, 0, 1, 1);
        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(segment);

        BSPTree tree = new BSPTree(segments);

        BSPTree.Node actual = tree.getTree();
        assertEquals(segment, actual.value);
        assertNull(actual.left);
        assertNull(actual.right);
    }

    @Test
    void testTwoChildNodeConstruction() {
        Segment root = new Segment(0, 0, 1, 1);
        Segment left = new Segment(-1, 1, -2, 1);
        Segment right = new Segment(2, 1, 3, 1);

        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);
        segments.add(left);
        segments.add(right);

        BSPTree.rand = new Random(0);

        BSPTree tree = new BSPTree(segments);

        BSPTree.Node actual = tree.getTree();
        assertEquals(root, actual.value);
        assertEquals(left, actual.right.value);
        assertEquals(right, actual.left.value);
    }

    @Test
    void testCollisionIsNullWithRootReAdd(){
        Segment root = new Segment(0, 0, 1, 1);
        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);

        BSPTree.rand = new Random(0);
        BSPTree tree = new BSPTree(segments);

        assertNull(tree.collision(root));
    }

    @Test
    void testCollisionWithRoot(){
        Segment root = new Segment(0, 0, 1, 1);
        Segment collidingSegment = new Segment(0, 1, 1, 0);
        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);

        BSPTree.rand = new Random(0);
        BSPTree tree = new BSPTree(segments);

        var collision = tree.collision(collidingSegment);
        assertEquals(root, collision);
    }
}