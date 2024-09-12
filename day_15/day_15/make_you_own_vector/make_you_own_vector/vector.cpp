//
//  vector.cpp
//  Day_16_VectorBuilding
//
//

#include "vector.hpp"
#include <iostream>


myVector::myVector(int initialCapacity){
    arrayStart = new int[initialCapacity];
    capacity = initialCapacity;
    size = 0;
}

// Destructor
myVector::~myVector(){
    delete arrayStart;
    arrayStart = nullptr;
    size = 0;
}

// Copy constructor
myVector::myVector(const myVector& vector){
    arrayStart = new int[capacity];
    capacity = vector.capacity;
    size = vector.size;
    
    for(int i = 0; i < vector.size; i++){
        arrayStart[i] = vector.arrayStart[i];
    }
};

myVector& myVector::operator=(myVector rhs){
  // rhs is already a copy from the copy constructor call
    int* tmp= rhs.arrayStart;
    arrayStart = rhs.arrayStart;
    size = rhs.size;
    capacity = rhs.capacity;
    rhs.arrayStart = tmp;
    
    return *this;
};

int myVector::operator[](int index) const {return arrayStart[index];};

int& myVector::operator[](int index) {return arrayStart[index];};

void myVector::growvector(){
    int newCapacity = capacity * 2;
    int* newVector = new int[newCapacity];
    for (int i = 0; i < size; i++){
        int* addressToSet = newVector + i;
        *addressToSet = arrayStart[i];
    }
    this->~myVector();
    arrayStart = newVector;
    capacity = newCapacity;
}

void myVector::pushBack(int value){
    if (size >= capacity){
        growvector();
    }
    set(size, value);
}

void myVector::popBack(){
    size--;
}

int myVector::get(int index){
    return *(arrayStart + index);
}

void myVector::set(int index, int newValue){
    if (index <= size){
        *(arrayStart + index) = newValue;
        if (index == size)
            size = size + 1;
    } else
        std::cout << "You cannot set a value beyond index " <<  size + 1 << std::endl;
}

int myVector::getSize(){
    return size;
}

int myVector::getCapacity(){
    return capacity;
}

int* myVector::getArray(){
    return arrayStart;
};
