package assignment03;

import java.util.Random;

public class Timer extends assignment03.TimerTemplate {
    BinarySearchSet<Integer> setUnderTest = new BinarySearchSet<>();
    int numUnderTest = 0;

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
        setUnderTest.clear();
        for (int i = 0; i < n; i++) {
            setUnderTest.add(i);
        }

        // Pick random number to find
        Random random = new Random();
        numUnderTest = random.nextInt(n + 1);

        // testing for add
        // Remove the number so it can be added and removed in timing methods between iterations
        setUnderTest.remove(numUnderTest);
    }

    @Override
    protected void timingIteration(int n) {
//        // Contains testing
//        setUnderTest.contains(numToSearch);
//        System.out.println("FINISHED: " + n);

        // add testing
        // Perform on copy of array so subsequent tests have the same input
        BinarySearchSet<Integer> tempTest = new BinarySearchSet<>();
        for (int element : setUnderTest) {
            tempTest.add(element);
        }
        tempTest.add(numUnderTest);
    }

    @Override
    protected void compensationIteration(int n) {
        BinarySearchSet<Integer> tempTest = new BinarySearchSet<>();
        for (int element : setUnderTest) {
            tempTest.add(element);
        }
    }
}
