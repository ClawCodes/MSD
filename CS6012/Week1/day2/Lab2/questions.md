1. On your computer, how many times per second does the millisecond timer update?
   * 1000 times

2. Is it possible to determine how many times per second the nanosecond timer updates? If so, how many? If not, why not?
   * Yes it is possible. It will vary across runs, due to other processes. You can take an average of the update count per second.

3. Judging by experiment 4, how long does it appear to take to compute System.nanoTime()?
   * ~42 nanoseconds. This was the median time across 100 runs, but this varies slightly.

4. Estimate the precision of your answer above (+/- how many nanoseconds?)
   * Based on several runs the average uncertainty is +/-79 nanoseconds

5. How long does it take to compute the square root of the numbers 1 through 10?
   * Experiment07: ranged from 20000-80000 nanoseconds
   * Experiment08: ranged from 48-64 nanoseconds

6. Estimate the precision of your answer above (+/- how many nanoseconds?)
   * Experiment07: The precision of experiment 7 is poor as it's +/-30000 nanoseconds
   * Experiment08: The precision of experiment 8 is better as it's +/-8 nanoseconds

7. If you repeat the square root test 100x as many times, does the precision improve?
   * Yes, it consistently completed in 3 nanoseconds

8. How could you improve the results (get a more accurate estimate of elapsed time)?
   * You could run the experiment over a range of different times to loop. This will allow us to average over different iteration sizes, thus providing a more general estimation of the time of execution. 