//
//  main.cpp
//  day_12
//
//  Created by Christopher Lawton on 9/4/24.
//

#include <iostream>
#include <cstdint>
#include <cassert>
#include <iomanip>
#include <cmath>
#include <fstream>
#include <filesystem>

namespace fs = std::filesystem;

void intRepresentations(){
    // How many bytes are in any data type?
    char xChar;
    std::cout << sizeof(xChar) << std::endl;
    int xInt;
    std::cout << sizeof(xInt) << std::endl;
    
    // Sizes of csdt unsigned types
    // one byte
    uint8_t uint8t;
    std::cout << sizeof(uint8t) << std::endl;
    // two bytes
    uint16_t uint16t;
    std::cout << sizeof(uint16t) << std::endl;

    // Unsigned int 8 min max
    uint8_t u8tMax = 0xFF;
    std::cout << +u8tMax << std::endl;
    uint8_t u8tMin = 0x00;
    std::cout << +u8tMin << std::endl;
    
    // Unsigned int 16 min max
    uint16_t u16tMax = 0xFFFF;
    std::cout << +u16tMax << std::endl;
    uint16_t u16tMin = 0x0000;
    std::cout << +u16tMin << std::endl;
    
    // Unsigned int 64 min max
    uint64_t u64tMax = 0xFFFFFFFFFFFFFFFF;
    std::cout << +u64tMax << std::endl;
    uint64_t u64tMin = 0x0000000000000000;
    std::cout << +u64tMin << std::endl;
    
    // Unsigned int 8 min max
    int8_t s8tMin = 0x80;
    std::cout << +s8tMin << std::endl;
    int8_t s8tMax = 0x7F;
    std::cout << +s8tMax << std::endl;
    
    // Unsigned int 16 min max
    int16_t s16tMin = 0x8000;
    std::cout << +s16tMin << std::endl;
    int16_t s16tMax = 0x7FFF;
    std::cout << +s16tMax << std::endl;
    
    // Unsigned int 16 min max
    int64_t s64tMin = 0x8000000000000000;
    std::cout << +s64tMin << std::endl;
    int64_t s64tMax = 0x7FFFFFFFFFFFFFFF;
    std::cout << +s64tMax << std::endl;
    
    // Add/subtract 1 to the max-values
    std::cout << "64 int max +1: " << +s64tMax + 1 << std::endl;
    std::cout << "64 int max -1: " << +s64tMax - 1 << std::endl;
    std::cout << "64 int min +1: " << +s64tMin + 1 << std::endl;
    std::cout << "64 int min -1: " << +s64tMin - 1 << std::endl;
}

bool approxEquals(double a, double b, double tolerance){
    double diff = std::abs(a - b);

    return diff < tolerance;
}

void floatRepresentations(){
    double doubleTest = .1 + .2;
    std::cout << "double: " << doubleTest << std::endl;
    // Fails
    // assert(step1 == 0.3);
    float floatTest = .1 + .2;
    std::cout << "Float: " << floatTest << std::endl;
    
    bool comparison = approxEquals(doubleTest, 0.3, 0.000000000001);
    std::cout << comparison << std::endl;
}

bool isAscii(char c){
    return (c > 0 && c <= 127);
}

void compareToASCII(std::string fileName){
    char c;
    
    int asciiCount = 0;
    int nonAsciiCount = 0;
    
    std::ifstream inFile(fileName);
    
    while (inFile >> c){
        // Compare characters
        if (isAscii(c)){
            asciiCount++;
        } else {
            nonAsciiCount++;
        }
        std::cout << c << std::endl;
    }
    
    std::cout << "Count of ASCII chars: " << asciiCount << std::endl;
    std::cout << "Count of NON-ASCII chars: " << nonAsciiCount << std::endl;
}


int main(int argc, const char * argv[]) {

    intRepresentations();
    
    std::cout << std::setprecision(18); //print numbers to 18 digits of accuracy
    floatRepresentations();
    
    fs::path filePath = fs::path(__FILE__).parent_path() / "number_rep_part_3.txt";
    compareToASCII(filePath);
    
    // We are seeing \\d\d\d print as by default we are using the base ASCII encoding table and not the extended.
    // If we used the extended ASCII, then we would be able to print the extended ASCII characters to screen
    
    return 0;
}
