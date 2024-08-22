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
    
    // Print odd numbers
    // Using if statement
    for (i=1; i<=20; i++){
        if (i % 2 != 0){
            std::cout << i << "\n";
        }
    }
    // Not using if statement
    for (i=1; i<=20; i+=2){
        std::cout << i << "\n";
    }
    // Not using an if statement is better as it reduces the amount of iterations and the additiona if computation required
    
    i = 0;
    int userInput = 0;
    while (userInput >= 0){
        std::cout << "Enter a positive number to sum\n";
        std::cin >> userInput;
        if (userInput >= 0){
            i += userInput;
        }
    }
    std::cout << "The sum of your entered numbers is: " << i << "\n";
    
    for (i = 1; i <= 5; i++){
        std::cout << "\n" << i << "x*: ";
        for (int j = 1; j <= 5; j++){
            std::cout << i * j << ' ';
        }
    }
    return 0;
}
