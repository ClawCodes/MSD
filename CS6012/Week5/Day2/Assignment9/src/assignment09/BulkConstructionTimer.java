package assignment09;

import java.util.ArrayList;

public class BulkConstructionTimer extends TimerTemplate{
    ArrayList<Segment> worstCase = new ArrayList<>();
    BSPTree bulkTree;

    public BulkConstructionTimer(int[] problemSizes, int timesToLoop) {
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
        ArrayList<Segment> worstCaseCopy = new ArrayList<>(worstCase);
        bulkTree = new BSPTree(worstCaseCopy);
    }

    @Override
    protected void compensationIteration(int n) {
        ArrayList<Segment> worstCaseCopy = new ArrayList<>(worstCase);
    }
}
