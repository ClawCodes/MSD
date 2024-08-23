//
//  main.cpp
//  RomanNumerals
//
//  Created by Christopher Lawton on 8/22/24.
//

#include <iostream>
using namespace std;
#include <string>


int main(int argc, const char * argv[]) {
    int userInput;
    
    // Capture user input number to convert
    cout << "Enter a decimal number:";
    cin >> userInput;
        
    // Ensure user input can be converted as roman numerals begin at 1
    if (userInput <= 0){
        cout << "Please enter a number which is greater than 0." << endl;
        return 1;
    }
    
    // Emit roman numerals incrementally
    while(userInput > 0){
        
        if (userInput >= 1000){
            cout << 'M';
            userInput = userInput - 1000;
        } else if (userInput >= 900){
            cout << "CM";
            userInput = userInput - 900;
        } else if (userInput >= 500){
            cout << 'D';
            userInput = userInput - 100;
        } else if (userInput >= 400){
            cout << "CD";
            userInput = userInput - 400;
        } else if (userInput >= 100){
            cout << 'C';
            userInput = userInput - 100;
        } else if (userInput >= 90){
            cout << "XC";
            userInput = userInput - 90;
        } else if (userInput >= 50){
            cout << 'L';
            userInput = userInput - 10;
        } else if (userInput >= 40){
            cout << "XL";
            userInput = userInput - 40;
        } else if (userInput >= 10){
            cout << 'X';
            userInput = userInput - 10;
        } else if (userInput >= 9){
            cout << "IX";
            userInput = userInput - 9;
        } else if (userInput >= 5){
            cout << 'V';
            userInput = userInput - 5;
        } else if (userInput >= 4){
            cout << "IV";
            userInput = userInput - 4;
        } else {
            cout << 'I';
            userInput = userInput - 1;
        }
    }
    cout << endl;
}
