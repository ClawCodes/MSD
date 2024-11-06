package lab03;

import java.util.ArrayList;

public class Timer extends TimerTemplate {
    ArrayList<Integer> listUnderTest = new ArrayList<>();

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public Timer(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        listUnderTest.clear();
        for (int i = 0; i < n; i++) {
            listUnderTest.add(i);
        }
    }

    @Override
    protected void timingIteration(int n) {
        listUnderTest.clear();
    }

    @Override
    protected void compensationIteration(int n) {
    }
}
