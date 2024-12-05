package assignment09;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class CollisionTimer extends TimerTemplate {
    public static boolean bspCollision = false;
    ArrayList<Segment> segments = new ArrayList<>();
    Segment segment;
    BSPTree bspTree;

    public CollisionTimer(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        segments.clear();
        for (int i = 0; i < n; i++) {
            segments.add(new Segment(i, 0, i, 1));
        }
        bspTree = new BSPTree(segments);
        segment = new Segment(Math.random(), Math.random(), Math.random(), Math.random());
    }

    @Override
    protected void timingIteration(int n) {
        if (bspCollision) {
            bspTree.collision(segment);
        } else {
            AtomicBoolean collisionFound = new AtomicBoolean(false);
            while (!collisionFound.get()) {
                bspTree.traverseFarToNear(0, 0, (segment) -> {
                    if (segment.intersects(segment)) {
                        collisionFound.set(true);
                    }
                });
                break;
            }
        }
    }

    @Override
    protected void compensationIteration(int n) {
//        int idx = new Random().nextInt(segments.size() - 1);
//        segment = new Segment(idx - 0.5, 0.5, idx - 0.5, 0.5); // will only collide with line at idx
        if (bspCollision) {
        } else {
            AtomicBoolean collisionFound = new AtomicBoolean(false);
        }
    }
}
