//
//  main.cpp
//  day_17
//
//  Created by Christopher Lawton on 9/11/24.
//
// Scratch code for opertor overloading

#include <iostream>
#include <cassert>
#include "MyString.hpp"


struct SpeciaInt{
    int x;
    // operator + is a binary operator (needs two arguments)
    SpeciaInt operator +(const SpeciaInt &rhs) const {
        return SpeciaInt{this->x + rhs.x}; // Within a class you can access private members of the same class (this is why you can access rhs.x)
    }
    
    // This is a standard pattern -> return a reference to "this" for operation= operators
    SpeciaInt& operator +=(const SpeciaInt &rhs){
        *this = *this + rhs;
        return *this;
    }
};

//operator[](int index){
//    myVec[i] -> myVec.operator[](1);
//}
// These need different methods to implement both
//x = myVec[i];
//myVec[i] = x; <- this returns a reference


//  x = myVec[i];
//int operator[](int index) const;
//
// myVec[i] = x;
//int& operator[](int index){
//    return myPtrs[index];
//}


// COPY CONSTRUCTOR
// MyVector (const MyVector& rhs){} // You must pass by reference as passing by copy with create infinite recursion by calling the constructor over and over
// A copy constructor ONLY runs when you are creating a new object
// MyVec v2; -> calls default constructor
// v2 = v1; -> calls = operator
// MyVec v2 = v1; -> calls copy constructor
// To get the same behavior you would need to implement a new = operator
        //
        // COPY AND SWAP IDIOM
        //MyVector& operator =(MyVec rhs){ // Pass by value calls the copy constructor
        //    swap(size, rhs.size);
        //    swap(capacity, rhs.capacity);
        //    swap(ptr, rhs.ptr);
        //    ~rhs;
        //    return *this;
        //}

// RULE OF 3
// If you have a destructor, copy constructor, or operator =, then you need all three

// Rule of 0
// Use types as your members that can handle copy/destruct/etc. for you
// i.e. use types which clean up after themselves


// Example class implementing copy patterns



int main(int argc, const char * argv[]) {
 
//    auto one = SpeciaInt{1};
//    auto two = SpeciaInt{2};
//    
//    auto three = one + two;
//    
//    assert(three.x == 3);
//    
//    one += two;
//    
//    assert(one.x == 3);
    
    // Default construct a string
    MyString ms1;
    assert(ms1.size() == 0);
    
    MyString ms2 = "hello"; // Calls my char* constructor
    int size = ms2.size();
    for (int i = 0; i < size; i++){
        std::cout << ms2[i];
    }
    std::cout << std::endl;
    
    // rule of 3
    {
        MyString ms3 = ms2;
        ms3[0] = 'j';
        int size = ms3.size();
        for (int i = 0; i < size; i++){
            std::cout << ms3[i];
        }
        
    }
    
    for (int i = 0; i < size; i++){
        std::cout << ms2[i];
    }
    
    
    
    
    return 0;
}
