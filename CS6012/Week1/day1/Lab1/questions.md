# Lab 1 Questions

1. Where is the assertEquals method defined? - `assertEquals` is defined in the JUnit `org.junit.jupiter.api.Assertions` package.
   2. What does it do? - `assertEquals` asserts that two values of the same type are equal
   3. Can we use assertEquals for more than just int types? (Hint: Consult the API for JUnit) - Yes, there are several overloads for different types.

2. Briefly describe what each of the three tests is checking.
   3. `emptyArray` is checking that an empty array hits the first condition of the `findSmallestDiff` method which ensures a diff can be found in the array. This asserts that -1 will be returned.

3. Why is our unimplemented findSmallestDiff method passing one of the tests?
   4. The unimplemented test is returning `0` which happens to be what the `allArrayElementsEqual` expects the output to be

4. Why are we failing the third test? How should our method be modified to correct this?
   5. We are failing the third test because we are not looking at the absolute value of the difference, so the smallest diff we find is -8 due to the comparison being <previous element> - <following element>. When we reach -8, the previous element is -8 and the following element is 0.

5. What is the appropriate call to assertEquals for your new test (test 4)?
   6. My new test is asserting the smallest diff is found between the starting two elements in the array. Since the starting element is `0` and the following element is `1`, then I expect the output to be `1` resulting in the testing body to be `assertEquals(1, DiffUtil.findSmallestDiff(arr4)`.

6. Provide one more test (test 5) for the findSmallestDiff method. Briefly describe your test and write the call to assertEquals here. 
   7. My new test is asserting the smallest diff is found between the ending two elements in the array. Since the ending element is `1` and the second to last element is `1`, then I expect the output to be `1` resulting in the testing body to be `assertEquals(1, DiffUtil.findSmallestDiff(arr4)`.

7. Briefly describe your unit tests for Assignment 1.
   8. 