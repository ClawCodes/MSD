//
//  vector.hpp
//  Day_16_VectorBuilding
//
//

#ifndef vector_hpp
#define vector_hpp

#include <stdio.h>

class myVector{
    
    int size;
    int capacity;
    int* arrayStart;

public:
    myVector(int initialCapacity);
    
    ~myVector();
    
    myVector(const myVector& vector);
    
    myVector& operator=(myVector rhs);
    
    int operator[](int index) const;
    
    int& operator[](int index);
    
    void growvector();
    
    void pushBack(int value);
    
    void popBack();
    
    int get(int index);
    
    void set(int index, int newValue);
    
    int getSize();
    
    int getCapacity();
    
    int* getArray();
    
};

#endif /* vector_hpp */
