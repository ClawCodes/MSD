# Lab 1 Questions

1. Where is the assertEquals method defined? - `assertEquals` is defined in the JUnit `org.junit.jupiter.api.Assertions` package.
   * What does it do? - `assertEquals` asserts that two values of the same type are equal
   * Can we use assertEquals for more than just int types? (Hint: Consult the API for JUnit) - Yes, there are several overloads for different types.

2. Briefly describe what each of the three tests is checking.
   * `emptyArray` is checking that an empty array hits the first condition of the `findSmallestDiff` method which ensures a diff can be found in the array. This asserts that -1 will be returned.
   * `allArrayElementsEqual` is checking that when all elements of the array are equal, then there will be no non-zero diff
   * `smallRandomArrayElements` is checking that the smallest diff can be found amongst and unsorted random number array. Effectively finding the smallest diff across any elements in an array. In this case between 4 and 0.

3. Why is our unimplemented findSmallestDiff method passing one of the tests?
   * The unimplemented test is returning `0` which happens to be what the `allArrayElementsEqual` expects the output to be

4. Why are we failing the third test? How should our method be modified to correct this?
   * We are failing the third test because we are not looking at the absolute value of the difference, so the smallest diff we find is -8 due to the comparison being <previous element> - <following element>. When we reach -8, the previous element is -8 and the following element is 0.

5. What is the appropriate call to assertEquals for your new test (test 4)?
   * My new test is asserting the smallest diff is found between the starting two elements in the array. Since the starting element is `0` and the following element is `1`, then I expect the output to be `1` resulting in the testing body to be `assertEquals(1, DiffUtil.findSmallestDiff(arr4)`.

6. Provide one more test (test 5) for the findSmallestDiff method. Briefly describe your test and write the call to assertEquals here. 
   * My new test is asserting the smallest diff is found between the ending two elements in the array. Since the ending element is `1` and the second to last element is `1`, then I expect the output to be `1` resulting in the testing body to be `assertEquals(1, DiffUtil.findSmallestDiff(arr4)`.

7. Briefly describe your unit tests for Assignment 1.
   * `testGetPixel` - tests getting pixel values at various locations. More specifically, the corner pixels from images with different dimensions.
   * `testEquals` - Asserts images are equivalent
   * `equalsWithDifferentTypes` - Asserts images are not equivalent to non-GrayscaleImage types
   * `equalsWithDifferentDimensions` - Asserts images that have different dimensions are not equal
   * `equalsWithInequivalentPixel` - Asserts images that share the same dimension, but have different pixel values are not equivalent
   * `averageBrightnessFromArguments` - Asserts the average brightness is calculated correctly from images of different dimensions and values
   * `normalizedFromExpectedBrightness` - Asserts images with different dimensions and values are scaled correctly. Covers the edge case when the average brightness is 0.
   * `mirroredWithVaryingDimensions` - Asserts images with varying dimensions are correctly mirrored
   * `croppedWithVaryingDimensions` - Asserts:
     * images with varying dimensions can be cropped
       * Handles cases where either height, width, or both dimension(s) is 1
     * Images can be cropped on each corner
   * `croppedThrowsIllegalArgumentException` - Asserts an exception is throw when any of the parameter values would require a larger image that what instance is under test
   * `squarified` - Asserts images with width > height, height == width, and height > width can be converted to a square image
   * `testGetPixelThrowsOnIndexOutOfBounds` - Asserts that an exception is thrown when a requested pixel does not exist in the image