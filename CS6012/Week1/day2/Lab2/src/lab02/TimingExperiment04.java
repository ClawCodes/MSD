package lab02;

import java.util.ArrayList;

public class TimingExperiment04 {

    public static void main(String[] args) {
        long lastTime = System.nanoTime();
        int advanceCount = 0;
        long[] advanceAmounts = new long[100];
        while (advanceCount < 100) {
            long currentTime = System.nanoTime();
            if (currentTime == lastTime)
                continue;
            advanceAmounts[advanceCount++] = currentTime - lastTime;
            lastTime = currentTime;
        }
        ArrayList<Long> diffs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            System.out.println("Time advanced " + advanceAmounts[i] + " nanoseconds.");
            diffs.add(Math.abs(42 - advanceAmounts[i]));
        }
        System.out.println("AVG uncertainty: " + diffs.stream().mapToLong(Long::longValue).sum() / diffs.size());
    }
}