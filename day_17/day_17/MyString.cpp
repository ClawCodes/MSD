//
//  MyString.cpp
//  day_17
//
//  Created by Christopher Lawton on 9/11/24.
//

#include "MyString.hpp"
#include <cstring>


// Constructor
MyString::MyString(){
    data = new char[1];
    data[0] = '0\';
}; // empty string (Array of size 1 which contains the null terminator character ('\0')

MyString::MyString(const char* str){
    int strSize = strlen(str) + 1;
    data = new char[strSize];
    for (int = 0; i < strSize; i++){
        data[i] = str[i];
    }
    
};

// Rule of 3
// Copy constructor
MyString::MyString(const MyString& rhs){
    int rhsSize = rhs.size() + 1; // This counts the zero byte at the end with +1 as strlen does not count the terminator byte ('/0')
    data = new char[rhsSize];
    
    for(int i = 0; i < rhsSize; i++){
        data[i] = rhs.data[i];
    }
};

MyString::MyString& operator=(const MyString rhs){
    // rhs is it's own copy becuase it's passed by value
    char* tmp = data;
    data = rhs.data;
    rhs.data = tmp;
    
    return *this;
    // rhs destructor will delete[] my old data
};

// desctructor
MyString::~MyString(){
    delete [] data;
};

// +, +=
MyString operator+=(const MyString& lhs, const MyString& rhs){
    // Make a new array long enough for this + rhs
    int lSize = size();
    int rSize = rhs.size();
    char* newData = new char[lSize + rSize + 1];
    
    // copy lhs and rhs values in
    for(int i = 0; i < lSize; i++){
        newData[i] = data[i];
    }
    
    for(int i = 0; i < lSize; i++){
        newData[lSize + i] = data[i];
    }
    // add a '\0' byte
    newData[lSize + rSize] = '\0';
    
    // delted old array
    delete [] data;
    data = newData;
    
    return *this;
};
int MyString::size() const{
    return (int)strlen(data);
};


MyString operator+(const MyString& lhs, const MyString)

