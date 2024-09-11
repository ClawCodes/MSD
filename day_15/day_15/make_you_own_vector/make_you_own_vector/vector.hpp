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
    
    void growvector();
    
    void pushBack(int value);
    
    void popBack();
    
    int get(int index);
    
    void set(int index, int newValue);
    
    int getSize();
    
    int getCapacity();
    
};

#endif /* vector_hpp */
