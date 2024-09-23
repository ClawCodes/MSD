//
//  main.cpp
//  Palindromes
//
//  Created by Christopher Lawton on 8/22/24.
//  Partner: Alexia Pappas

#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    std::string userInput;
    std::cout << "Please enter a word to determine if it is a palindrome";
    std::cin >> userInput;
    
    std::string result;
    
    for (int i=0; i<userInput.size(); i++){
        result = userInput[i] + result;
    }
    
    if (userInput == result){
        std::cout << userInput << " is a palindrome" << std::endl;
    } else {
        std::cout << userInput << " is NOT a palindrome" << std::endl;
    }
    
    return 0;
}
