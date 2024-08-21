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
    
    // Determin vote and senate eligibility
    if ( age >= 18){
        std::cout << "You are old enough to vote!\n";
    }
    if ( age >= 30){
        std::cout << "You are old enough to run for senate\n";
    }
    
    // determine generation
    if ( age > 80){
        std::cout << "You are part of the greatest generation\n";
    }else if ( age > 60 && age <= 80){
        std::cout << "You are a baby boomer\n";
    }else if ( age > 40 && age <= 60){
        std::cout << "You are part of generation X\n";
    }else if (age > 20 && age <= 40){
        std::cout << "You are a millenial\n";
    }else {
        std::cout << "You are an iKid\n";
    }
    
    // Determine if the user will get sleep
    char weekdayResponse, holidayResponse, childrenResponse;
    bool isWeekday, isHoliday, hasChildren;
    
    std::cout << "Is it a weekday?\n";
    std::cin >> weekdayResponse;
    isWeekday = (weekdayResponse == 'Y');
    
    std::cout << "Is it a holiday?\n";
    std::cin >> holidayResponse;
    isHoliday = (holidayResponse == 'Y');
    
    std::cout << "Do you have young children?\n";
    std::cin >> childrenResponse;
    hasChildren = (childrenResponse == 'Y');
    
    if (hasChildren || isWeekday){
        std::cout << "You will not get sleep!\n";
    } else {
        std::cout << "You will get sleep!\n";
    }
    
    return 0;
}
