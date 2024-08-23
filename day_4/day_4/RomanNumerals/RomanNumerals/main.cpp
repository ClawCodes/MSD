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
    string userInput;
    int decimalInteger;
    
    cout << "Enter a decimal number:";
    cin >> userInput;
    
    decimalInteger = stoi(userInput);
    
    if (decimalInteger <= 0){
        cout << "Please enter a number which is greater than 0.";
        return 1;
    }
    
    while(decimalInteger > 0){
        
        if (decimalInteger >= 1000){
            cout << 'M';
            decimalInteger = decimalInteger - 1000;
        } else if (decimalInteger >= 900){
            cout << "CM";
            decimalInteger = decimalInteger - 900;
        } else if (decimalInteger >= 500){
            cout << 'D';
            decimalInteger = decimalInteger - 100;
        } else if (decimalInteger >= 400){
            cout << "CD";
            decimalInteger = decimalInteger - 400;
        } else if (decimalInteger >= 100){
            cout << 'C';
            decimalInteger = decimalInteger - 100;
        } else if (decimalInteger >= 90){
            cout << "XC";
            decimalInteger = decimalInteger - 90;
        } else if (decimalInteger >= 50){
            cout << 'L';
            decimalInteger = decimalInteger - 10;
        } else if (decimalInteger >= 40){
            cout << "XL";
            decimalInteger = decimalInteger - 40;
        } else if (decimalInteger >= 10){
            cout << 'X';
            decimalInteger = decimalInteger - 10;
        } else if (decimalInteger >= 9){
            cout << "IX";
            decimalInteger = decimalInteger - 9;
        } else if (decimalInteger >= 5){
            cout << 'V';
            decimalInteger = decimalInteger - 5;
        } else if (decimalInteger >= 4){
            cout << "IV";
            decimalInteger = decimalInteger - 4;
        } else {
            cout << 'I';
            decimalInteger = decimalInteger - 1;
        }
    }
    cout << endl;
}
