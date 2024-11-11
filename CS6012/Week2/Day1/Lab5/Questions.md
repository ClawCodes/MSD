### Question 1 - What activities did you do as the driver? What activities did you do as the navigator?
As a driver I wrote the `findMedian2` method and the accompanying tests. Before any implementation, I discussed the approach for each method with the navigator.
As a navigator I observed the driver's code in real time and actively caught syntax and logic errors. I also planned the implementation with the driver before the driver implemented any code.

### Question 2 - What are the advantages and disadvantages of serving in each role?
* Driver:
  * Advantages:
    * Having discussed the solution with the navigator earlier on, the implementation becomes smoother to implement
  * Disadvantages:
    * If you make mistakes often 
* Navigator:
  * Advantages:
    * The lack of active programming can release some mental energy to think about the solution more as it is evolving. This could potentially lead to catching errors earlier than the driver would.
  * Disadvantages:

### Question 3 - How often did you and your partner switch roles? Did you switch often enough?
We switched once so each person could implement a method and it's respective tests.
I think switching once was sufficient given the size of the assignment.

### Questions 4 - Which is your preferred role and why?
I preferred the navigator role because I felt I was able to refine the solution real time. 
It freed up mental space for me to ponder other solutions/code refinement while the driver implemented.
I felt I was able to create substantial impact this way.

### Question 5 - Explain why this is a BAD test case for your method
This is a bad test case, because the ordering is implicit and the expected output can be different depending on the ordering.
For example, if the ordering is length of string, then the median would be one of `cat`, `dog`, or `ant`, not `bird`.
If the ordering is the first character, then the median could be `bird` or `cat` depending on if your median method returns the upper or lower bound when finding the median of an even length set.
Overall, the test is not explicit enough in what it is testing.

### Question 6 - Explain how you could change the test to make it "valid" and explain how you could change the specification so that this is considered a "good" test
I would change the test to be more explicit in what is being tested and ensure the test case is written in such a way that is unambiguous. 
```java
// median method uses natural ordering of input. Natural order of strings is alphabetical
String[] input = new String[]{"bird", "cat", "dogs", "anteater"};
String expected = "bird";
assertEquals(expected, MedianSolver.median(input)); 
```
This test has elements that are different lengths and eliminate any false positives if that median ordering was done by length of string.
It helps ensure the tester that the median is found from the alphabetical order.
