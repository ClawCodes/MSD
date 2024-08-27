//
//  main.cpp
//  day_6
//
//  Created by Christopher Lawton on 8/26/24.
//
// Scratch code for lecture

#include <iostream>
#include "math_operations.hpp"
#include <vector>

int main(int argc, const char * argv[]) {

    std::cout << add(2,3) << std::endl;
    
    // Vectors
    std::vector<int> numbers;
    numbers.push_back(1);
    numbers.push_back(2);
    
    for (int i = 0; i < numbers.size(); i++){
        std::cout << numbers[i] << std::endl;
    }
    
    return 0;
}
