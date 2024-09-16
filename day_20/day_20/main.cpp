//
//  main.cpp
//  day_20
//
//  Created by Christopher Lawton on 9/13/24.
//

#include <iostream>

// Create a non-copyable object
struct NotCopy{
    // include delete
    NotCopy(const NotCopy&) = delete;
    NotCopy& operator = (const NotCopy&) = delete;
//    NotCopy(NotCopy&& other); // This is a move constructor
//    NotCopy& operator=(NotCopy&& rhs);
};

// What would the move constructor for your vector look like?
MyVector(MyVector&& other){
    size = other.size;
    capacity = other.capactiy;
    ptr = other.ptr;
    // Do the following since other's destructor will run and would delete the pointer you just stole from other.
    other.ptr = nullptr;
    other.size = 0;
    other.capacity = 0;
}

// For each loop
for (auto x : someVector){
    cout << x;
}
// -> The compiler transforms above into something like:
auto begin = someVector.begin();
auto end = someVector.end();
for (auto it = begin; it != end; it++){
    auto x = *it;
    cout << x;
}

// Iterators
// begin and end are iterators which behave like pointers
// To create an iterator you need to include begin and end methods
int* begin(){
    return data; // Where data is the first pointer in the vector
}

int* end(){
    return data + size() + 1; // End points to one beyond the last value (end character)
}

// Defining the above methods will allow you to use your defined struct/classes with a for each loop

int main(int argc, const char * argv[]) {
    
    
    return 0;
}
