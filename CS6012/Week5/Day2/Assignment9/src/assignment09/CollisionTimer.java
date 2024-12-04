package assignment09;

import java.util.ArrayList;
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
        if (bspCollision) {
        } else {
            AtomicBoolean collisionFound = new AtomicBoolean(false);
        }
    }
}
