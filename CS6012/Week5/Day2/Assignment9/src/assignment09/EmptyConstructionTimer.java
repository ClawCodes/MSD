package assignment09;

import java.util.ArrayList;

public class EmptyConstructionTimer extends TimerTemplate {
    ArrayList<Segment> worstCase = new ArrayList<>();

    public EmptyConstructionTimer(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        worstCase.clear();
        for (int i = 0; i < n; i++) {
            Segment seg = new Segment(i, 0, i, 1);
            worstCase.add(seg);
        }
    }

    @Override
    protected void timingIteration(int n) {
        BSPTree tree = new BSPTree();
        for (Segment seg : worstCase) {
            tree.insert(seg);
        }
    }

    @Override
    protected void compensationIteration(int n) {
        BSPTree tree = new BSPTree();
    }
}
