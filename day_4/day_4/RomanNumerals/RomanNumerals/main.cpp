//
//  main.cpp
//  RomanNumerals
//
//  Created by Christopher Lawton on 8/22/24.
//

#include <iostream>
using namespace std;
#include <string>

// 1000 -> one iteration
// 1200 -> two iterations

int main(int argc, const char * argv[]) {
    string decimalIntStr;
    int decimalInteger;
    
    cout << "Enter a decimal number:";
    cin >> decimalIntStr;
    
    decimalInteger = stoi(decimalIntStr);
    
    while(decimalInteger > 0){
        int multiplier = 1;
        int subtractInt = 0;
        
        if (decimalInteger >= 1000){
            multiplier = decimalInteger / 1000;
            cout << string(multiplier, 'M');
            subtractInt = multiplier * 1000;
        } else if (decimalInteger >= 900){
            cout << "CM";
            subtractInt = 900;
        } else if (decimalInteger >= 500){
            multiplier = decimalInteger / 100;
            cout << string(multiplier, 'D');
            subtractInt = multiplier * 100;
        } else if (decimalInteger >= 400){
            cout << "CD";
            subtractInt = 400;
        } else if (decimalInteger >= 100){
            multiplier = decimalInteger / 100;
            cout << string(multiplier, 'C');
            subtractInt = multiplier * 100;
        } else if (decimalInteger >= 90){
            cout << "XC";
            subtractInt = 90;
        } else if (decimalInteger >= 50){
            multiplier = decimalInteger / 10;
            cout << string(multiplier, 'L');
            subtractInt = multiplier * 10;
        } else if (decimalInteger >= 40){
            cout << "XL";
            subtractInt = 40;
        } else if (decimalInteger >= 10){
            multiplier = decimalInteger / 10;
            cout << string(multiplier, 'X');
            subtractInt = multiplier * 10;
        } else if (decimalInteger >= 9){
            cout << "IX";
            subtractInt = 9;
        } else if (decimalInteger >= 5){
            cout << 'V';
            cout << string(decimalInteger - 5, 'I');
            subtractInt = decimalInteger;
        } else if (decimalInteger >= 4){
            cout << "IV";
            subtractInt = 4;
        } else {
            cout << string(decimalInteger, 'I');
            subtractInt = decimalInteger;
        }
        
        decimalInteger = decimalInteger - subtractInt;
    }
    cout << endl;
}
