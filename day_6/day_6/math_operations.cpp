//
//  math_operations.cpp
//  day_6
//
//  Created by Christopher Lawton on 8/26/24.
//

#include "math_operations.hpp" // means int add(int a, int b); is included in this file
#include <cassert>

int add(int a, int b){
    assert(a >= 0 && b >= 0);
    int sum = a + b;
    assert(sum >= 0);
    return sum;
}
