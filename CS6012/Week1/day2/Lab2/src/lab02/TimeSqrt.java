package lab02;

import java.util.ArrayList;

public class TimeSqrt {
    public static void timeSqrt() {
        ArrayList<Long> times = new ArrayList<>();
        long lastTime = System.nanoTime();
        for(int i = 1; i < 11; i++){
            long currentTime = System.nanoTime();
            Math.sqrt(i);
            times.add(currentTime - lastTime);
            lastTime = currentTime;
        }

        for (long time : times) {
            System.out.print(time + " ");
        }
    }
}
