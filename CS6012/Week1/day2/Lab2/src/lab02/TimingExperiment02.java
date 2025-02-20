package lab02;

public class TimingExperiment02 {

  public static void main(String[] args) {
    long secondsToRun = 500;
    long startTime = System.currentTimeMillis();
    long lastTime = startTime;
    int advanceCount = 0;
    int loopCount = 0;
    while (lastTime - startTime < 1000 * secondsToRun) {
      loopCount++;
      long currentTime = System.currentTimeMillis();
      if (currentTime == lastTime)
        continue;
      // Do this when the loop took >= 1 millisecond
      lastTime = currentTime;
      advanceCount++;
    }
    double advancesPerSecond = advanceCount / (double) secondsToRun;
    System.out.println("Time advanced " + advancesPerSecond + " times per second.");
    System.out.println("The loop tested the time " + loopCount + " times.");
  }
}
