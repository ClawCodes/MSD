//
//  main.cpp
//  average
//
//  Created by Christopher Lawton on 8/20/24.
// Progrmmer partner: Alexia Pappas

#include <iostream>

int main(int argc, const char * argv[]) {
    float score1, score2, score3, score4, score5;
    
    std::cout << "Enter 5 assignement scores:";
    std::cin >> score1 >> score2 >> score3 >> score4 >> score5;
    
    std::cout << score1 << "\n";
    std::cout << score2 << "\n";
    std::cout << score3 << "\n";
    std::cout << score4 << "\n";
    std::cout << score5 << "\n";
    
    std::cout << "Average: " << (score1 + score2 + score3 + score4 + score5) / 5 << "\n";
    
    return 0;
}
