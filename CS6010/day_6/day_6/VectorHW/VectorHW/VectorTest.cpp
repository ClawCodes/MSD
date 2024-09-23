/*
  Author: Daniel Kopta and ??
  July 2017
  
  CS 6010 Fall 2019
  Vector util library tests

  Compile this file together with your VectorUtil library with the following command:
  clang++ -std=c++11 VectorTest.cpp VectorUtil.cpp

  Most of the provided tests will fail until you have provided correct 
  implementations for the VectorUtil library functions.

  You will need to provide more thorough tests.
*/

#include <iostream>
#include <string>
#include <vector>

// Include the VectorUtil library
#include "VectorUtil.h"

/*
 * Helper function for failing a test.
 * Prints a message and exits the program.
 */
void ErrorExit( std::string message )
{
  std::cerr << "Failed test: " << message << std::endl;
  exit(1); // Causes the entire program to exit.
}


int main()
{
  
  // Set up some input vectors for testing purposes.

  // We can use list initialization syntax:
  vector<int> v1 = {3, 1, 0, -1, 5};

  // Or we can repeatedly push_back items
  vector<int> v2;
  v2.push_back(1);
  v2.push_back(2);
  v2.push_back(3);

  // When testing, be sure to check boundary conditions, such as an empty vector
  vector<int> empty;
  
  
  /* 
   * Contains tests 
   */

  // v1 doesn't contain 4, so this should return false
  if( Contains(v1, 4) ) {
    ErrorExit( "Contains() - test 1" );
  }

  // v1 does contain -1, so this should return true
  if(!Contains(v1, -1)) {
    ErrorExit("Contains() - test 2");
  }

  /* 
   * The vector 'empty' doesn't contain anything, so this should return false
   * The specific value we're looking for here (99) is not important in this test. 
   * This test is designed to find any general errors caused by the array being empty. 
   * That type of error is unlikely to depend on the value we are looking for.
  */
  if( Contains(empty, 99) ) {
    ErrorExit("Contains() - empty");
  }
    
  /*
   * FindMin test cases
   */
    
  // Find min which is negative
    std::vector<int> vecMinNegative = {1, -1, 2};
    if (!(FindMin(vecMinNegative) == -1)){
        ErrorExit("FindMin() - test 1");
    }
  // Find min which is positive
    std::vector<int> vecMinPositive = {2, 1, 3};
    if (!(FindMin(vecMinPositive) == 1)){
        ErrorExit("FindMin() - test 2");
    }
  // Find min which is 0
    std::vector<int> vecMinZero = {1, 3, 0};
    if (!(FindMin(vecMinZero) == 0)){
        ErrorExit("FindMin() - test 3");
    }
  // Find min when the first element is larger than the true min
    std::vector<int> vecMinNotInZerothIdx = {3, 1, 0};
    if (!(FindMin(vecMinNotInZerothIdx) == 0)){
        ErrorExit("FindMin() - test 4");
    }
  // Find min when the first element is the true min
    std::vector<int> vecMinAtZerothIdx = {0, 1, 3};
    if (!(FindMin(vecMinAtZerothIdx) == 0)){
        ErrorExit("FindMin() - test 5");
    }
  // Find min when all elements are the same
    std::vector<int> vecAllZero = {0, 0, 0};
    if (!(FindMin(vecAllZero) == 0)){
        ErrorExit("FindMin() - test 6");
    }
  // Find min with array of one element
    std::vector<int> vecOneElement = {0};
    if (!(FindMin(vecOneElement) == 0)){
        ErrorExit("FindMin() - test 7");
    }
    
    /*
     * FindMax test cases
     */
    
    // Find max which is negative
      std::vector<int> vecMaxNegative = {-4, -1, -3};
      if (!(FindMax(vecMaxNegative) == -1)){
          ErrorExit("FindMax() - test 1");
      }
    // Find max which is positive
      std::vector<int> vecMaxPositive = {2, 1, 3};
      if (!(FindMax(vecMaxPositive) == 3)){
          ErrorExit("FindMax() - test 2");
      }
    // Find max which is 0
      std::vector<int> vecMaxZero = {-1, -3, 0};
      if (!(FindMax(vecMaxZero) == 0)){
          ErrorExit("FindMax() - test 3");
      }
    // Find max when the first element is smaller than the true max
      std::vector<int> vecMaxNotInZerothIdx = {3, 1, 5};
      if (!(FindMax(vecMaxNotInZerothIdx) == 5)){
          ErrorExit("FindMax() - test 4");
      }
    // Find max when the first element is the true max
      std::vector<int> vecMaxAtZerothIdx = {5, 1, 3};
      if (!(FindMax(vecMaxAtZerothIdx) == 5)){
          ErrorExit("FindMax() - test 5");
      }
    // Find max when all elements are the same
      std::vector<int> vecMaxAllZero = {0, 0, 0};
      if (!(FindMax(vecMaxAllZero) == 0)){
          ErrorExit("FindMax() - test 6");
      }
    // Find max with array of one element
      std::vector<int> vecMaxOneElement = {0};
      if (!(FindMax(vecMaxOneElement) == 0)){
          ErrorExit("FindMax() - test 7");
      }
    
    /*
     * Average test cases
     */
    
    // Find average from array of positive integers
    std::vector<int> vecAllPositive = {1, 2, 3};
    if (!(Average(vecAllPositive) == 2)){
        ErrorExit("Average() - test 1");
    }
    // Find average from array of negative integers
    std::vector<int> vecAllNegative = {-1, -2, -3};
    if (!(Average(vecAllNegative) == -2)){
        ErrorExit("Average() - test 2");
    }
    
    // Find average from array of the same integer
    std::vector<int> vecAllTheSame = {2, 2, 2};
    if (!(Average(vecAllTheSame) == 2)){
        ErrorExit("Average() - test 3");
    }
    
    // Find average of array with one element
    std::vector<int> vecAvgOneElement = {2};
    if (!(Average(vecAvgOneElement) == 2)){
        ErrorExit("Average() - test 4");
    }
    
    /*
     * IsSorted test cases
     */
    
    // is sorted without duplicates
    std::vector<int> vecSortedNoDups = {1, 2, 3, 4};
    if (!(IsSorted(vecSortedNoDups))){
        ErrorExit("IsSorted() - test 1");
    }
    // is sorted with duplicates
    std::vector<int> vecSortedWithDups = {1, 2, 2, 3, 4, 4};
    if (!(IsSorted(vecSortedWithDups))){
        ErrorExit("IsSorted() - test 2");
    }
    // is sorted with negative and positive ints
    std::vector<int> vecSortedWithNegAndPos = {-10, -2, 2, 3, 4};
    if (!(IsSorted(vecSortedWithNegAndPos))){
        ErrorExit("IsSorted() - test 3");
    }
    // is not sorted with violation occuring at first element
    std::vector<int> vecNotSortedFirstElem = {10, 1, 2, 3};
    if (IsSorted(vecNotSortedFirstElem)){
        ErrorExit("IsSorted() - test 4");
    }
    // is not sorted with violation occuring at last element
    std::vector<int> vecNotSortedLastElem = {-1, 1, 2, 2, -10};
    if (IsSorted(vecNotSortedLastElem)){
        ErrorExit("IsSorted() - test 5");
    }
    
  // Since any failed test exits the program, if we made it this far, we passed all tests.
  std::cout << "All tests passed!\n";

}
