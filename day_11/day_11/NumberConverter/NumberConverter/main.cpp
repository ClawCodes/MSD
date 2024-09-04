//
//  main.cpp
//  NumberConverter
//
//  Created by Christopher Lawton on 9/3/24.
//

#include <iostream>
#include <string>
#include <cassert>
#include <vector>


int charToInt(char c){
    if (isalpha(c)){
        return tolower(c) - 'a' + 10;
    } else {
        return c - '0';
    }
}

char intToChar(int num){
    if (num>=10){
        return '7' + num;
    } else {
        return '0' + num;
    }
}

int stringToInt(std::string numberStr, int base){
    bool isNegative = numberStr[0] == '-';
    if (isNegative){
        numberStr = numberStr.substr(1, numberStr.size() - 1);
    }
    int result = 0;
    for (int i = 0; i < numberStr.size(); i++){
        int number = charToInt(numberStr[i]);
        result += number * (pow(base, numberStr.size() - i - 1));
    }
    return isNegative ? result * -1 : result;
}

std::string numToString(int num, int base){
    bool isNegative = num < 0;
    if (isNegative){
        num = num * -1;
    }
    std::string result;
    while (num > 0){
        int remainder = num % base;
        char converted = intToChar(remainder);
        result.insert(result.begin(), converted);
        num = num / base;
    }
    return isNegative ? '-' + result : result;
}

std::string intToString(int num){
    return numToString(num, 10);
}

std::string intToBinary(int num){
    return numToString(num, 2);
}

std::string intToHex(int num){
    return numToString(num, 16);
}

void testStringtoInt(){
    assert(stringToInt("99", 10) == 99);
    assert(stringToInt("10", 2) == 2);
    assert(stringToInt("32", 16) == 50);
    assert(stringToInt("FF", 16) == 255);
    assert(stringToInt("-FF", 16) == -255);
    assert(stringToInt("-10", 2) == -2);
}

void testIntToString(){
    assert(intToString(10) == "10");
    assert(intToString(123) == "123");
    assert(intToString(-123) == "-123");
}

void testToBinary(){
    assert(intToBinary(12) == "1100");
    assert(intToBinary(57) == "111001");
    assert(intToBinary(-57) == "-111001");
}

void testToHex(){
    assert(intToHex(12) == "C");
    assert(intToHex(100) == "64");
    assert(intToHex(254) == "FE");
    assert(intToHex(-254) == "-FE");
}

int main(int argc, const char * argv[]) {
    testStringtoInt();
    testIntToString();
    testToBinary();
    testToHex();
    
    return 0;
}
