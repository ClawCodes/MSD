package assignment03;

import java.util.Random;

public class Timer extends assignment03.TimerTemplate {
    BinarySearchSet<Integer> setUnderTest = new BinarySearchSet<>();
    Random random = new Random();

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
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            setUnderTest.add(random.nextInt());
        }

        // Pick random number to find
//        numUnderTest = random.nextInt(n + 1);

        // testing for add
        // Remove the number so it can be added and removed in timing methods between iterations
//        setUnderTest.remove(numUnderTest);
    }

    @Override
    protected void timingIteration(int n) {
//        // Contains testing
//        setUnderTest.contains(numToSearch);
//        System.out.println("FINISHED: " + n);

        // add testing
        // Perform on copy of array so subsequent tests have the same input
//        BinarySearchSet<Integer> tempTest = new BinarySearchSet<>();
//        for (int element : setUnderTest) {
//            tempTest.add(element);
//        }
        setUnderTest.remove(setUnderTest.last());
        setUnderTest.add(random.nextInt());
    }

    @Override
    protected void compensationIteration(int n) {
//        BinarySearchSet<Integer> tempTest = new BinarySearchSet<>();
//        for (int element : setUnderTest) {
//            tempTest.add(element);
//        }
//        setUnderTest.remove(numUnderTest);
        setUnderTest.remove(setUnderTest.last());
        random.nextInt();
    }
}
