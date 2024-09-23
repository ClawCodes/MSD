//
//  main.cpp
//  day_4
//
//  Created by Christopher Lawton on 8/22/24.
//
//  Practice from lecture

#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    std::string password;
    bool isValid = false;
    
    while(!isValid){
        
        std::cout << "Please enter your password\n";
        std::cin >> password;
        
        if (password.length() < 8){
            std::cout << "Please enter a password of 8 or more characters" << std::endl;
        } else if (password.find('$') == std::string::npos){
            std::cout << "Please add $ to your password" << std::endl;
        } else if (!(password[0] >= 'A' && password[0] <= 'Z')){
            std::cout << "Please make sure that your first letter is a capital " << std::endl;
        } else {
            isValid = true;
            std::cout << "Congrats you picked a strong password" << std::endl;
        }
        
        
    }
    
    return 0;
}
