//
//  main.cpp
//  day_18
//
//  Created by Christopher Lawton on 9/12/24.
//
// Scratch project for day 18 - Templates

#include <iostream>


class MyVector{
    int* arr; // This enforces only using ints
    
public:
    void pushBack(int x); // This enforces only using ints
};

// How can we change MyVecotr to accept any types and work like std::vector?
// -> use a template

// Templates are a class or function that is essentially a "fill in the blank"


// This is no longer a class, it is a class template
template <typename T>
class FlexibleVector{
    T* arr;
    void push_back(T val);
};

// std::Struct pair
// The types are filled in positionally - instantiate as Pair<int, double> MyPair = (1, 2.0);
template<typename T, typename U>
struct Pair{
    T first;
    U second;
};

// Struct array in std::array is of fixed size
// passing in size allows use to create a fixed size array
template <typename T, int size>
struct array{
    T[size] data;
};

// Function template
template<typename T>
T abs(T x){
    if (x < 0) return -x;
    return x;
};


// TEMPLATE ANNOYANCES

// 1. Errors are usually detected at point of use
// 2. All template stuff lives in header files
// 3. Declaring methods with a template needs to use <class name><T>::<method name> -> same as normal methods, but you need to specify T


// More operator overloads
// 1. std::cout << "hello world" -> this is an overload of <<. If you want your type to be cout-ed, then you can overload the left shift operator

struct MyType{
    char a;
    char b;
};

std::ostream& operator <<(std::ostream& out, MyType x){
    out << x.a << ' ' << x.b;
    return out;
};

// Note: since cin mutates the value x, you need to pass by reference
std::istream& operator >>(std::istream& in, MyType& x){
    in >> x.a >> x.b;
    return in;
}


int main(int argc, const char * argv[]) {
    
    
    return 0;
}
