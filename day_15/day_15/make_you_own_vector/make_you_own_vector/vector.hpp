//
//  vector.hpp
//  Day_16_VectorBuilding
//
//  Created by Alexia Pappas on 9/10/24.
//

#ifndef vector_hpp
#define vector_hpp

#include <stdio.h>

#endif /* vector_hpp */

struct myVector{
    int size;
    int capacity;
    int* arrayStart;
};

myVector makeVector(int initialCapacity);

void freeVector(myVector &newVector);

void growvector(myVector &vector);

void pushBack(myVector &vector, int value);

void popBack(myVector &vector);

int get(myVector &vector, int index);

void set(myVector &vector, int index, int newValue);
