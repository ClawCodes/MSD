public class threadExample {

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            CounterRunnable counter = new CounterRunnable(i);
            Thread t = new Thread(counter);
            t.start();
        }
    }
}
