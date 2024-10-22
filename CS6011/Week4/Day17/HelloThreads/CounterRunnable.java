public class CounterRunnable implements Runnable {
    int myId;

    CounterRunnable(int id) {
        myId = id;
    }

    @Override
    public void run() {
//        System.out.println("I am thread: " + myId + " (internal Id: " + Thread.currentThread().threadId() + ")");
    for (int i = 0; i < 100; i++){
        System.out.println(myId + ": " + i);
    }
    }
}
