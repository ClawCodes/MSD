//
//  main.cpp
//  TripleTemplate
//
//  Created by Christopher Lawton on 9/12/24.
//

#include <iostream>
#include <string>
#include <cassert>
#include <vector>
#include "TripleTemplate.h"

int main(int argc, const char * argv[]) {
    Triple<int> tripleInt = {1, 2, 3};
    assert(tripleInt.add() == 6);
    
    Triple<std::string> tripleString = {"Hello", "World", "!!"};
    assert(tripleString.add() == "HelloWorld!!");
    
    Triple<std::vector<int>> tripleVector = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    tripleVector.add(); // This causes the compiler to fail as there is no valid + operand for std::array
    
    return 0;
}
