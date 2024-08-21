//
//  main.cpp
//  ForLoopPractice
//
//  Created by Christopher Lawton on 8/21/24.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    // print numbers from 1 to 10
    // Using for loop
    int i;
    std::cout << "Numbers from for loop:\n";
    for (i=1; i<=10; i++){
        std::cout << i << "\n";
    }
    // Using while loop
    i = 1;
    std::cout << "Numbers from while loop:\n";
    while (i <= 10) {
        std::cout << i << "\n";
        i++;
    }
    // The appropriate loop to use is for as there is a fixed amount of numbers we want to print
    
    // Print numbers in ascending order from two user input numbers
    int numOne, numTwo, upperBound, lowerBound;
    std::cout << "Enter two numbers\n";
    std::cin >> numOne >> numTwo;
    // Assume user entered numbers in ascending order
    lowerBound = numOne;
    upperBound = numTwo;
    // Swap bounds if user provided in swapped order
    if (numTwo < numOne){
        upperBound = lowerBound;
        lowerBound = upperBound;
    }
    for (i=lowerBound; i<=upperBound; i++){
        std::cout << i << "\n";
    }
    
    return 0;
}
