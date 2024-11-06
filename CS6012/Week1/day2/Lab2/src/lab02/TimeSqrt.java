package lab02;

public class TimeSqrt {
    public static void main() {
        long start = System.nanoTime();
        for(int i = 1; i < 11; i++){
            Math.sqrt(i);
        }
        long end = System.nanoTime();
        System.out.println("Time elapsed (ns): " + (end - start));
    }
}
