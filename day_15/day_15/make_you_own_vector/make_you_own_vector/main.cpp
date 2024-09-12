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
    
    // Test with different numeric types
    testNumericTypeVector<int>();
    testNumericTypeVector<long>();
    testNumericTypeVector<double>();
    testNumericTypeVector<float>();
    
    // Test comparison operators
    int capacity = 10;
    auto vector1 = myVector<int>(capacity);
    auto vector2 = myVector<int>(capacity);
    auto vector3 = myVector<int>(capacity);
    
    // make the equivalent and non equivalent arrays
    for (int i = 0; i < 10; i++){
        vector1[i] = i;
        vector2[i] = i;
        vector3[i] = i + 1;
    }
    
    assert(vector1 == vector2);
    assert(!(vector1 != vector2));
    assert(vector1 < vector3);
    assert(vector3 > vector1);
    assert(vector1 <= vector3);
    assert(vector3 >= vector1);
    
    return 0;
}
