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
        assertEquals(left, actual.left.value);
        assertEquals(right, actual.right.value);
    }

    @Test
    void testSingleRootNodeInsert() {
        Segment segment = new Segment(0, 0, 1, 1);
        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(segment);

        BSPTree tree = new BSPTree();
        tree.insert(segment);

        BSPTree.Node actual = tree.getTree();
        assertEquals(segment, actual.value);
        assertNull(actual.left);
        assertNull(actual.right);
    }

    @Test
    void testTwoChildNodeInsert() {
        Segment root = new Segment(0, 0, 1, 1);
        Segment left = new Segment(-1, 1, -2, 1);
        Segment right = new Segment(2, 1, 3, 1);

        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);
        segments.add(left);
        segments.add(right);

        BSPTree tree = new BSPTree();
        tree.insert(root);
        tree.insert(left);
        tree.insert(right);

        BSPTree.Node actual = tree.getTree();
        assertEquals(root, actual.value);
        assertEquals(left, actual.left.value);
        assertEquals(right, actual.right.value);
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

    @Test
    void testCollisionWithChild(){
        Segment root = new Segment(0, 0, 1, 1);
        Segment left = new Segment(-1, 1, -2, 1);
        Segment right = new Segment(2, 1, 3, 1);

        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);
        segments.add(left);
        segments.add(right);

        BSPTree.rand = new Random(0);
        BSPTree tree = new BSPTree(segments);
        Segment rightCollision = new Segment(2.5, 0, 2.5, 1.5);

        var collision = tree.collision(rightCollision);
        assertEquals(right, collision);
    }

    @Test
    void testCollisionIsNullWithNoCollision(){
        Segment root = new Segment(0, 0, 1, 1);
        Segment left = new Segment(-1, 1, -2, 1);
        Segment right = new Segment(2, 1, 3, 1);

        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);
        segments.add(left);
        segments.add(right);

        BSPTree.rand = new Random(0);
        BSPTree tree = new BSPTree(segments);
        Segment noCollisionSeg = new Segment(5, 1, 7, 1);

        var collision = tree.collision(noCollisionSeg);
        assertNull(collision);
    }

    @Test
    void testFarToNearFromOrigin(){
        Segment root = new Segment(0, 0, 1, 1);
        Segment left = new Segment(-1, 1, -2, 1);
        Segment right = new Segment(2, 1, 3, 1);

        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);
        segments.add(left);
        segments.add(right);

        BSPTree.rand = new Random(0);
        BSPTree tree = new BSPTree(segments);
        ArrayList<Segment> visited = new ArrayList<>();
        tree.traverseFarToNear(0, 0, (segment) -> {
            visited.add(segment);
        });
        assertEquals(right, visited.get(0));
        assertEquals(root, visited.get(1));
        assertEquals(left, visited.get(2));
    }

    @Test
    void testFarToNearFromFarNegative(){
        Segment root = new Segment(0, 0, 1, 1);
        Segment left = new Segment(-1, 1, -2, 1);
        Segment right = new Segment(2, 1, 3, 1);

        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);
        segments.add(left);
        segments.add(right);

        BSPTree.rand = new Random(0);
        BSPTree tree = new BSPTree(segments);
        ArrayList<Segment> visited = new ArrayList<>();
        tree.traverseFarToNear(-10, 0, (segment) -> {
            visited.add(segment);
        });
        assertEquals(right, visited.get(0));
        assertEquals(root, visited.get(1));
        assertEquals(left, visited.get(2));
    }

    @Test
    void testFarToNearFromFarPositive(){
        Segment root = new Segment(0, 0, 1, 1);
        Segment left = new Segment(-1, 1, -2, 1);
        Segment right = new Segment(2, 1, 3, 1);

        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(root);
        segments.add(left);
        segments.add(right);

        BSPTree.rand = new Random(0);
        BSPTree tree = new BSPTree(segments);
        ArrayList<Segment> visited = new ArrayList<>();
        tree.traverseFarToNear(10, 0, (segment) -> {
            visited.add(segment);
        });
        assertEquals(left, visited.get(0));
        assertEquals(root, visited.get(1));
        assertEquals(right, visited.get(2));
    }
}