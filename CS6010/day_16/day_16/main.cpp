//
//  main.cpp
//  day_16
//
//  Created by Christopher Lawton on 9/10/24.
//
// Scratch file for in class examples

#include <iostream>

struct S{
    int calculate(); // The struct will be in the header file. To keep calculate in cpp files, declare with <Struct name>::<method name>(){}
    int x;
};

int S::calculate(){
    x++; // To reference you could use (*this).x but this is not used often. Instead you could use this->x. However most of the time people do not write the arrow.
    return x;
}

// Store int on the heap
class HeapInt{
public:
    int getValue(){return *intPtr;};
    HeapInt(); // Default constructor
    HeapInt(int val); // Constructor with passed in param
    ~HeapInt(); // Destructor
private:
    int* intPtr;
};

// This default constructor creates HeapInt with intPtr == 0 on the heap
HeapInt::HeapInt(){
    intPtr = new int;
    *intPtr = 0;
}

HeapInt::HeapInt(int val){
    intPtr = new int(val);
}

HeapInt::~HeapInt(){
    delete intPtr;
    // You don't need to set intPtr to nullPtr as intPtr can never be accessed again once the class is gone
}

int main(int argc, const char * argv[]) {
    auto hi = HeapInt(1); // Calls non-defualt constructor
    
    return 0;
}
