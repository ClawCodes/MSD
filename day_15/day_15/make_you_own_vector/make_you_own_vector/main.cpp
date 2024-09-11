//
//  main.cpp
//  Day_16_VectorBuilding
//
//

#include <iostream>
#include <cassert>
#include "vector.hpp"



int main(int argc, const char * argv[]) {
    
    
    auto vector = myVector(10);
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
    
    return 0;
}
