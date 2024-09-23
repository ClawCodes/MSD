//
//  main.cpp
//  pointer_lab
//
//  Created by Christopher Lawton on 9/9/24.
//

#include <iostream>
#include <cassert>

double* copyArr(double* arr, int size){
    double* copy = new double[size];
    
    for (int i = 0; i < size; i++){
        copy[i] = arr[i];
    }
    
    return copy;
}

int main(int argc, const char * argv[]) {
    int size = 5;
    double* arr = new double[size];
    
    // Insert dummy values
    for (int i = 0; i < size; i++){
        arr[i] = i;
    }
    
    double* copiedArr = copyArr(arr, size);

    for (int i = 0; i < size; i++){
        assert(arr[i] == copiedArr[i]);
    }
    
    delete[] arr;
    arr = nullptr;
    delete copiedArr;
    arr = nullptr;
    return 0;
}
