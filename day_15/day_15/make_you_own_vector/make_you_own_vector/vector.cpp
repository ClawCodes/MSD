//
//  vector.cpp
//  Day_16_VectorBuilding
//
//

#include "vector.hpp"
#include <iostream>


myVector makeVector(int initialCapacity){
    int* arrayStart = new int[initialCapacity];
    auto newVector = myVector{0, initialCapacity, arrayStart};
    return newVector;
}

void freeVector(myVector &newVector){
    delete newVector.arrayStart;
    newVector.arrayStart = nullptr;
}

void growvector(myVector &vector){
    int newCapacity = vector.capacity * 2;
    int* newVector = new int[newCapacity];
    for (int i = 0; i < vector.size; i++){
        int* addressToSet = newVector + i;
        *addressToSet = vector.arrayStart[i];
    }
    freeVector(vector);
    vector.arrayStart = newVector;
    vector.capacity = newCapacity;
}

void pushBack(myVector &vector, int value){
    if (vector.size >= vector.capacity){
        growvector(vector);
    }
    set(vector, vector.size, value);
}

void popBack(myVector &vector){
    vector.size--;
}

int get(myVector &vector, int index){
    return *(vector.arrayStart + index);
}

void set(myVector &vector, int index, int newValue){
    if (index <= vector.size){
        *(vector.arrayStart + index) = newValue;
        vector.size = vector.size + 1;
    } else
        std::cout << "You cannot set a value beyond index " <<  vector.size + 1 << std::endl;
}
