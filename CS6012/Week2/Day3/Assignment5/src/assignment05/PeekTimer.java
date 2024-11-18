package assignment05;

public class PeekTimer extends StackTimingExperiment {
    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public PeekTimer(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void timingIteration(int n) {
        if (classUndertest.equals("ArrayStack")) {
            arrayStack.peek();
        } else {
            linkedStack.peek();
        }
    }

    @Override
    protected void compensationIteration(int n) {
        if (classUndertest.equals("ArrayStack")) {
        } else {
        }
    }

    public static void main(String[] args) {
        runTest(PopTimer.generateProblemSizes(), "peek");
    }
}
