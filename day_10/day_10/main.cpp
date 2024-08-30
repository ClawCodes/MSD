//
//  main.cpp
//  day_10
//
//  Created by Christopher Lawton on 8/30/24.
//
// Scratch file for in class examples
//
// Pointers


// NOTES

// By reference acts similar to pointers but is not the same
// C type arrays work with the pointer to where the collection starts and the size of the collection. Size provides you how far to traverse in memory.
// Pointer - data type that allows you to store addresses
//  Dereferencing a pointer can be done with "*<pointer variable name>" (e.g. std::count << *myPointer)


#include <iostream>


void incrementArray(int numbers [], int size){
    for (int i=0; i <size; i++){
        numbers[i] += 1;
    }
}

int * createNumbers(){
    int numbers[5];
    numbers[0]=2;
    
    return numbers;
}

int main(int argc, const char * argv[]) {
    
    // If you use C-style arrays, then it implicilty uses pointers (it is not pass by value)
    int numbers[5] = {1,2,3,4,5};
//    incrementArray( numbers, 5 );
//    
//    for (int i = 0; i < 5; i++){
//        std::cout << numbers[i] << std::endl;
//    }
    
    int *numP;
    numP = numbers;
//    int * = &numbers;
//    numP = createNumbers();
    
    std::cout << numP << std::endl;
    std::cout << *numP << std::endl; // Wut??
    
//    for (int i = 0; i < 5; i++){
//        
//        std::count << numP[i] << std::endl;
//    }
//    
    return 0;
}
