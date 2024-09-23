//
//  main.cpp
//  day_5
//
//  Created by Christopher Lawton on 8/23/24.
//
//  Scratch script for in class examples

#include <iostream>

int testVariadic(int args){
    std::cout << args;
    return 0;
}

int calculatePower(int base, int exp){
    int result = 1;
    for (int i=0; i<exp; i++){
        result = result*base;
    }
    return result;
}

void sayHi(){
    std::cout << "Hello" << std::endl;
}

int main(int argc, const char * argv[]) {
    int x=2, ex1=2, y=3, ex2=2, z=4, ex3=2, result=0;
    
    sayHi();
    result = calculatePower(x, ex1) + calculatePower(y, ex2) + calculatePower(z, ex3);
    
    std::cout << result << std::endl;
    
//    testVariadic(1, 2 , 3);
    
    return 0;
}
