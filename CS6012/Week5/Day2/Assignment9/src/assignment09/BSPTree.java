package assignment09;

import java.util.ArrayList;
import java.util.Random;

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

    protected static class Node {
        Segment value;
        Node left;
        Node right;

        Node(Segment value){
            this.value = value;
        }
    }

    void insert(Segment segment) {
        try {
            root_ = insert_(segment, root_);
            size_++;
        } catch (IllegalArgumentException | NullPointerException _){}
    }

    private Node insert_(Segment segment, Node node) {
        if (node == null) {
            node = new Node(segment);
        } else if (segment.whichSide(node.value) < 0) {
            node.left = insert_(segment, node.left);
        } else if (segment.whichSide(node.value) > 0) {
            node.right = insert_(segment, node.right);
        } else if (segment.whichSide(node.value) == 0) {
            Segment[] ret = segment.split(node.value);
            node.left = insert_(ret[0], node.left);
            node.right = insert_(ret[1], node.right);
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

    public void traverseFarToNear(double x, double y, SegmentCallback callback){
        farToNear(root_, x, y, callback);
    }

    private Segment findCollision(Node n, Segment query){
        Segment collision = null;
        if (n != null) {
            if (n.value.whichSide(query) == -1) {
                findCollision(n.left, query);
            } else if (n.value.whichSide(query) == 1) {
                findCollision(n.right, query);
            } else if (n.value.intersects(query)) {
                collision = n.value;
            } else {
                Segment[] splitSeg = query.split(n.value);
                findCollision(n.left, splitSeg[0]);
                findCollision(n.right, splitSeg[1]);
            }
        }
        return collision;
    }

    public Segment collision(Segment query){
        return findCollision(root_, query);
    }

    public Node getTree(){
        return root_;
    }
}
