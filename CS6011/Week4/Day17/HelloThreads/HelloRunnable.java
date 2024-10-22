public class HelloRunnable implements Runnable {
    public void run(){
        long threadNum = Thread.currentThread().threadId();
        for (int i = 0; i < 100; i++){
            if (i%10 == 0){
                System.out.println("hello number " + i + " from thread number " + threadNum);
            } else{
                System.out.print("hello number " + i + " from thread number " + threadNum);
            }
        }
    }
}
