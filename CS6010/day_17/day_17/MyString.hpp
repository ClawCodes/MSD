//
//  MyString.hpp
//  day_17
//
//  Created by Christopher Lawton on 9/11/24.
//

#ifndef MyString_hpp
#define MyString_hpp

// Class implementing copy patterns
class MyString{
    
public:
    // Constructor
    MyString(); // empty string (Array of size 1 which contains the null terminator character ('\0')
    MyString(const char* str);
    
    // Rule of 3
    MyString(const MyString& rhs);
    MyString& operator=(const MyString rhs);
    ~MyString(){};
    
    // +, +=
    MyString& opeerator+=(const MyString& rhs);
    // []
    // read only version
    char opeerator[](int index) const {return data[index]};
    // for str[i] = 'c';
    char& opeerator[](int index) {return data[index]};
    
    int size() const;
private:
    // null-terminated string (0-terminated)
    // size is implicit, ends where I find '\0' == 0
    char* data;
    
};

MyString operator+(const MyString& lhs, const MyString& rhs);

#endif /* MyString_hpp */
