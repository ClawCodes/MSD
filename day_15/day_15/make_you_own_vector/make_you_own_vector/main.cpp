//
//  main.cpp
//  Day_16_VectorBuilding
//
//

#include <iostream>
#include <cassert>
#include "vector.hpp"



int main(int argc, const char * argv[]) {
    
    myVector vector = makeVector(10);
    assert(vector.capacity == 10);
    assert(vector.size == 0);
    
    pushBack(vector, 7);
    assert(*vector.arrayStart == 7);
    assert(vector.size == 1);
    
    pushBack(vector, 13);
    assert(*(vector.arrayStart + 1) == 13);
    assert(vector.size == 2);
    
    popBack(vector);
    assert(*vector.arrayStart == 7);
    assert(vector.size == 1);
    
    set(vector, 0, 8);
    assert(*vector.arrayStart == 8);
    assert(vector.size == 2);
    
    set(vector, 1, 24);
    assert(*(vector.arrayStart + 1) == 24);
    assert(vector.size == 3);

    assert(get(vector, 1) == 24);
    
    freeVector(vector);
    assert(vector.arrayStart == nullptr);
    
    
    
    
    return 0;
}
