//
//  main.cpp
//  IfStatementPractice
//
//  Created by Christopher Lawton on 8/21/24.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    std::cout << "Please enter your age\n";
    int age;
    std::cin >> age;
    
    if ( age >= 18){
        std::cout << "You are old enough to vote!\n";
    }
    if ( age >= 30){
        std::cout << "You are old enough to run for senate\n";
    }
    if ( age > 80){
        std::cout << "You are part of the greatest generation\n";
    }
    else if ( age > 60 && age <= 80){
        std::cout << "You are a baby boomer\n";
    }
    else if ( age > 40 && age <= 60){
        std::cout << "You are part of generation X\n";
    }
    else if (age > 20 && age <= 40){
        std::cout << "You are a millenial\n";
    }
    else {
        std::cout << "You are an iKid\n";
    }
    return 0;
}
