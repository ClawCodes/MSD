//
//  vector.hpp
//  Day_16_VectorBuilding
//
//

#ifndef vector_hpp
#define vector_hpp

#include <stdio.h>
#include <iostream>

template<typename T>
class myVector{
    
    int size;
    int capacity;
    T* arrayStart;

public:
    myVector(int initialCapacity){
        arrayStart = new T[initialCapacity];
        capacity = initialCapacity;
        size = 0;
    }
    
    ~myVector(){
        delete arrayStart;
        arrayStart = nullptr;
        size = 0;
    }
    
    myVector(const myVector& vector){
        arrayStart = new T[capacity];
        capacity = vector.capacity;
        size = vector.size;
        
        for(int i = 0; i < vector.size; i++){
            arrayStart[i] = vector.arrayStart[i];
        }
    };
    
    myVector& operator=(myVector rhs){
      // rhs is already a copy from the copy constructor call
        int* tmp= rhs.arrayStart;
        arrayStart = rhs.arrayStart;
        size = rhs.size;
        capacity = rhs.capacity;
        rhs.arrayStart = tmp;
        
        return *this;
    };
    
    int operator[](int index) const {return arrayStart[index];};
    
    int& operator[](int index) {return arrayStart[index];};

    void growvector(){
        int newCapacity = capacity * 2;
        T* newVector = new T[newCapacity];
        for (int i = 0; i < size; i++){
            T* addressToSet = newVector + i;
            *addressToSet = arrayStart[i];
        }
        this->~myVector();
        arrayStart = newVector;
        capacity = newCapacity;
    }
    
    void pushBack(T value){
        if (size >= capacity){
            growvector();
        }
        set(size, value);
    }
    
    void popBack(){
        size--;
    }
    
    T get(int index){
        return *(arrayStart + index);
    }
    
    void set(int index, T newValue){
        if (index <= size){
            *(arrayStart + index) = newValue;
            if (index == size)
                size = size + 1;
        } else
            std::cout << "You cannot set a value beyond index " <<  size + 1 << std::endl;
    }
    
    int getSize(){
        return size;
    }

    int getCapacity(){
        return capacity;
    }

    T* getArray(){
        return arrayStart;
    };
    
};

#endif /* vector_hpp */
