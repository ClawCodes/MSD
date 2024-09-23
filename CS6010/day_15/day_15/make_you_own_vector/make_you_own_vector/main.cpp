//
//  main.cpp
//  Day_16_VectorBuilding
//
//

#include <iostream>
#include <cassert>
#include "vector.hpp"
#include <numeric>

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


void testComparisonOperators(){
    int capacity = 10;
    auto vector1 = myVector<int>(capacity);
    auto vector2 = myVector<int>(capacity);
    auto vector3 = myVector<int>(capacity);
    
    // make the equivalent and non equivalent arrays
    for (int i = 0; i < 10; i++){
        vector1[i] = i;
        vector2[i] = i; // equivalent to vector1
        vector3[i] = i + 1; // make vector3 larger than vector1 and vector2
    }
    
    assert(vector1 == vector2);
    assert(!(vector1 != vector2));
    assert(vector1 < vector3);
    assert(vector3 > vector1);
    assert(vector1 <= vector3);
    assert(vector3 >= vector1);
}


void testSort(){
    int capacity = 10;
    auto vector = myVector<int>(capacity);
    vector.pushBack(1);
    vector.pushBack(0);
    vector.pushBack(3);
    vector.pushBack(4);
    vector.pushBack(2);

    // Complete sort
    std::sort(vector.begin(),vector.end());
    assert(vector[0] == 0);
    assert(vector[1] == 1);
    assert(vector[2] == 2);
    assert(vector[3] == 3);
    assert(vector[4] == 4);
    
    //partial sort
    auto vector2 = myVector<int>(capacity);
    vector2.pushBack(1);
    vector2.pushBack(0);
    vector2.pushBack(3);
    vector2.pushBack(4);
    vector2.pushBack(2);
    
    std::sort(vector2.begin() + 2, vector2.end());
    
    assert(vector2[0] == 1);
    assert(vector2[1] == 0);
    assert(vector2[2] == 2);
    assert(vector2[3] == 3);
    assert(vector2[4] == 4);
}

void testMinElement(){
    int capacity = 10;
    // Total min
    auto vector = myVector<int>(capacity);
    vector.pushBack(1);
    vector.pushBack(0);
    vector.pushBack(3);
    vector.pushBack(4);
    vector.pushBack(2);
    
    int* minValue = std::min_element(vector.begin(), vector.end());
    assert(*minValue == 0);
    
    // Partial min
    auto vector2 = myVector<int>(capacity);
    vector2.pushBack(1);
    vector2.pushBack(0);
    vector2.pushBack(3);
    vector2.pushBack(4);
    vector2.pushBack(2);
    
    int* partialMinValue = std::min_element(vector2.begin(), vector2.begin() + 1);
    assert(*partialMinValue == 1);
}

void testAccumulate(){
    auto vector = myVector<int>(5);
    vector.pushBack(1);
    vector.pushBack(0);
    vector.pushBack(3);
    vector.pushBack(4);
    vector.pushBack(2);
    
    auto accumulation = std::accumulate(vector.begin(), vector.end(), 0);
    
    assert(accumulation == 10);
}

void testCountIf(){
    auto vector = myVector<int>(5);
    vector.pushBack(1);
    vector.pushBack(0);
    vector.pushBack(3);
    vector.pushBack(4);
    vector.pushBack(2);
    
    auto count = std::count_if(vector.begin(), vector.end(), [](int x){return x % 2 == 0;});
    assert(count == 3);
}



int main(int argc, const char * argv[]) {
    
    // Test with different numeric types
    testNumericTypeVector<int>();
    testNumericTypeVector<long>();
    testNumericTypeVector<double>();
    testNumericTypeVector<float>();
    
    testComparisonOperators();
    
    // Test compatibility with algorithms
    auto vector = myVector<int>(5);
    vector.pushBack(1);
    vector.pushBack(0);
    vector.pushBack(3);
    vector.pushBack(4);
    vector.pushBack(2);
    std::for_each(vector.begin(), vector.end(), [](int x){std::cout<<x<<std::endl;});
    
    testSort();
    testMinElement();
    testAccumulate();
    testCountIf();
    
    return 0;
}
