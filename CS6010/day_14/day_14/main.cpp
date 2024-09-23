//
//  main.cpp
//  day_14
//
//  Created by Christopher Lawton on 9/9/24.
//
// Scratch file for in class examples

#include <iostream>
#include <cstdlib>

int sum(int* arr, int size){
    int ret = 0;
    // This will allow us to go out of bounds, but the program will exectue and won't throw and error
//    for (int i = 0; i < size + 1000; i++){
//        std::cout << i - size << std::endl;
//        ret += arr[i];
//    }
    
    // Staying within bounds
    for (int i = 0; i < size; i++){
        ret += arr[i];
    }
    return ret;
}


int main(int argc, const char * argv[]) {

    int size = rand() % 20;
    int* arr = new int[size];
    for(int i = 0; i < size; i++){
        arr[i] = i;
    }
    
    std::cout << sum(arr, size) << std::endl;
    
    delete [] arr;

    return 0;
}

