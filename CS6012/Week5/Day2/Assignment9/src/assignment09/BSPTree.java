package assignment09;

import java.util.ArrayList;
import java.util.Random;


/**
 * Binary Space Partitioning Tree
 */
public class BSPTree {
    private Node root_;
    private int size_;
    static Random rand = new Random();

    /**
     * Default constructor with and empty tree (i.e. root is null)
     */
    public BSPTree(){
        root_ = null;
        size_ = 0;
    }

    /**
     * Constructor which constructs the tree from the segments provided and a randomly chosen segment as the root
     * @param segments segments to build the tree with
     */
    public BSPTree(ArrayList<Segment> segments){
        int rootIdx = rand.nextInt(segments.size());

        Segment rootSeg = segments.get(rootIdx);
        root_ = new Node(rootSeg);
        // remove root from segments
        segments.remove(rootIdx);
        ArrayList<Segment>[] partitionedSegs = partitionSegments(rootSeg, segments);
        root_.left = buildNode(partitionedSegs[0]);
        root_.right = buildNode(partitionedSegs[1]);
    }

    /**
     * Build a BSP tree recursively using a segment array.
     * The nodes will be randomly chosen in each frame.
     * @param segments segments to build the tree from
     * @return the top-level node of the tree
     */
    private Node buildNode(ArrayList<Segment> segments){
        Node newNode = null;
        if (!segments.isEmpty()){
            int idx = rand.nextInt(segments.size());
            newNode = new Node(segments.get(idx));
            segments.remove(idx);
            ArrayList<Segment>[] partitionedSegs = partitionSegments(newNode.value, segments);
            newNode.left = buildNode(partitionedSegs[0]);
            newNode.right = buildNode(partitionedSegs[1]);
        }
        return newNode;
    }

    /**
     * Partition a segment array around another segment
     * @param pivot the segment to partition the segments array around
     * @param segments the segments to partition
     * @return Array of length 2 with the first element as the segments which are on the left side of the pivot
     *         and the second element as the segments which are on the right side of the pivot
     */
    private ArrayList<Segment>[] partitionSegments(Segment pivot, ArrayList<Segment> segments){
        ArrayList<Segment>[] partitions = new ArrayList[2];
        ArrayList<Segment> left = new ArrayList<>();
        ArrayList<Segment> right = new ArrayList<>();

        for (Segment segment : segments) {
            if (pivot.whichSide(segment) == 0) {
                Segment[] splitSeg = segment.split(pivot);
                left.add(splitSeg[0]);
                right.add(splitSeg[1]);
            } else if (pivot.whichSide(segment) == 1) {
                right.add(segment);
            } else {
                left.add(segment);
            }
        }
        partitions[0] = left;
        partitions[1] = right;
        return partitions;
    }

    /**
     * Representation of a Node in the BSP tree.
     * The value of the node is the Node's segment instance
     */
    protected static class Node {
        Segment value;
        Node left;
        Node right;

        Node(Segment value){
            this.value = value;
        }
    }

    /**
     * Insert a Segment into the tree
     * @param segment segment to insert
     */
    public void insert(Segment segment) {
        try {
            root_ = insert_(segment, root_);
            size_++;
        } catch (IllegalArgumentException | NullPointerException e){
            System.out.println("Failed to insert segment.");
        }
    }

    /**
     * Recursively insert segment into the tree
     * @param segment segment to insert
     * @param node current node to compare the segment to
     * @return The top-level node of the BSP containing the newly inserted segment
     */
    private Node insert_(Segment segment, Node node) {
        if (node == null) {
            node = new Node(segment);
        } else if (segment.whichSide(node.value) < 0) {
            node.right = insert_(segment, node.right);
        } else if (segment.whichSide(node.value) > 0) {
            node.left = insert_(segment, node.left);
        } else if (segment.whichSide(node.value) == 0) {
            Segment[] ret = segment.split(node.value);
            node.right = insert_(ret[0], node.right);
            node.left = insert_(ret[1], node.left);
        }
        return node;
    }

    private void farToNear(Node node, double x, double y, SegmentCallback callback){
        if (node == null) {
            return;
        }
        if (node.value.whichSidePoint(x, y) == -1){
            farToNear(node.right, x, y, callback);
            callback.callback(node.value);
            farToNear(node.left, x, y, callback);
        } else {
            farToNear(node.left, x, y, callback);
            callback.callback(node.value);
            farToNear(node.right, x, y, callback);
        }
    }

    /**
     * Traverse the BSP tree and perform the callback action in an in-order like fashion.
     * @param x x of point to compare to the segments
     * @param y y of point to compare to the segments
     * @param callback class implementing a callback method to invoke
     */
    public void traverseFarToNear(double x, double y, SegmentCallback callback){
        farToNear(root_, x, y, callback);
    }

    /**
     * Recursively search for a node with intersects with a given segment
     * @param n node to compare segment to
     * @param query segment to see if it intersects with a node in the tree
     * @return the segment in the tree the query intersects with
     */
    private Segment findCollision(Node n, Segment query){
        Segment collision = null;
        if (n != null) {
            if (query.whichSide(n.value) == -1) {
                return findCollision(n.right, query);
            } else if (query.whichSide(n.value) == 1) {
                return findCollision(n.left, query);
            } else if (query.intersects(n.value)) {
                collision = n.value;
            } else {
                Segment[] splitSeg = query.split(n.value);
                findCollision(n.right, splitSeg[0]);
                findCollision(n.left, splitSeg[1]);
            }
        }
        return collision;
    }

    /**
     * Find a node which intersects with a given segment
     * @param query Segment to see if it intersects with a node in the tree
     * @return the segment in the tree the query intersects with
     */
    public Segment collision(Segment query){
        return findCollision(root_, query);
    }

    public Node getTree(){
        return root_;
    }
}
