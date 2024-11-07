1. Which two binarySearch tests are failing?
   * The second and thrid tests (`Part1.binarySearch(arr1, 0) == true)` and `(Part1.binarySearch(arr2, 20) == false)`)

2. What are the values of arr and goal passed in to binary search?
   * arr1 values are {1, 2, 3, 4, 5} with goal of 2 for first test and 0 for the second test. 
   * arr 2 values are {5, 10, 15, 20, 25} with goal of 20 for first test and 18 for second test

3. For each of the two cases of binarySearch that are failing, which return statements are they executing in error (is it returning true when it should be returning false, or the other way around)?
   * Both are exiting the binarySearch on the wrong return. So, both tests in the "Correct" state should exit the function with the opposite return statement.

4. What is the bug in binarySearch? How do you fix it?
   * The bug is that in the comparisons of goal to mid, we are comparing a value (goal) to an index (mid). To fix this, both references need to compare the value at the index of mid (arr[mid]) to the goal.

5. Which loops appear to be inifinite?
   * loop #2 and loop #4

6. For the loops that you found to be infinite, briefly explain why they are never progressing.
   * Loop #2: b will always remain 0 since be starts with 0 and  b *= a will always evaluate to 0. Then the while condition (b < 10) will always be true.
   * Loop #4: In this loop f will be continuously decremented by 1 and then incremented by 1 every other loop. This is due to the c *= -1 statement either resulting in c == -1 when c == 1 or c == 1 when c == -1. The loop never exits as it starts at 5 and oscillates between 5 and 6, thus always making f > 0 evaluate to true.

7. What does the call to password.split(" ") appear to do? Hint: examine the input value of "password", then examine the items in the "tokens" array returned by the call.
   * It splits the password string into the individual characters that are delimited by a space

8. What is the correct password?
   * 