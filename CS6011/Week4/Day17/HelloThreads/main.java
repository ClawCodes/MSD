import java.util.ArrayList;

public class main {

    static int answer = 0;
    static int numThreads = 10;

    static void badSum(){
        answer = 0;
        int maxValue = 40000;
        ArrayList<Thread> threads = new ArrayList<>();
        int expected = (maxValue * (maxValue - 1) / 2);
        for (int i = 0; i < numThreads; i++) {
            int finalI = i;
            Thread newThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int start = finalI *maxValue/numThreads;
                    int end = Math.min((finalI+1)*maxValue/numThreads, maxValue);
                    for (int i = start; i < end; i++) {
                        answer += i;
                    }
                    System.out.println("EXPECTED: " + expected);
                    System.out.println("ACTUAL: " + answer);
                }
            });
            threads.add(newThread);
            newThread.start();
        }
    }

    public static void sayHello(){
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            HelloRunnable runnable = new HelloRunnable();
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }
    }

    public static void main(String[] args) {
        sayHello();
        // What happens above?
        // The threads run in a psuedo-random order. They are not executed in serial
        // The same thread does NOT print the first and/or last hello. The OS determines the order or execution.
        badSum();
        // We are getting a race condition with badSum at higher iterations (i.e. when maxValue is larger).
        // This is happening because the operating system with pause and start threads and each thread then
        // alters the value of the static (global) variable 'answer'. Since once thread can alter answer
        // when the loop of another thread hasn't completed, then we end up not getting perfectly incrementing answer.

        // When I set maxValue to 100, I see a consistent answer.
        // It does not appear a race condition occurs, so the operating system must hit thread pauses and starts (concurrency)
        // when a thread takes a long time to execute.
    }
}
