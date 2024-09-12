//
//  main.cpp
//  Day_16_VectorBuilding
//
//

#include <iostream>
#include <cassert>
#include "vector.hpp"

template <typename T>
void testNumericTypeVector(){
    auto vector = myVector<T>(10);
    assert(vector.getCapacity() == 10);
    assert(vector.getSize() == 0);
    
    vector.pushBack(7);
    assert(vector.get(0) == 7);
    assert(vector.getSize() == 1);
    
    vector.pushBack(13);
    assert(vector.get(1) == 13);
    assert(vector.getSize() == 2);
    
    vector.popBack();
    assert(vector.get(0) == 7);
    assert(vector.getSize() == 1);
    
    vector.set(0, 8);
    assert(vector.get(0) == 8);
    assert(vector.getSize() == 1);
    
    vector.set(1, 24);
    assert(vector.get(1) == 24);
    assert(vector.getSize() == 2);

    assert(vector.get(1) == 24);
    
    vector.~myVector();
    assert(vector.getSize() == 0);
    
    // Test copy operator
    myVector vector2 = vector;
    assert(vector2.getSize() == vector.getSize());
    assert(vector2.getCapacity() == vector.getCapacity());
    assert(vector2.getArray() != vector.getArray());
}

int main(int argc, const char * argv[]) {
    
    testNumericTypeVector<int>();
    testNumericTypeVector<long>();
    testNumericTypeVector<double>();
    testNumericTypeVector<float>();
    
    
    return 0;
}
