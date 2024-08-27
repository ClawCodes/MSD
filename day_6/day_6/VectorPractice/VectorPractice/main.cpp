//
//  main.cpp
//  VectorPractice
//
//  Created by Christopher Lawton on 8/26/24.
//

#include <iostream>
#include <vector>
#include <cassert>
#include <string>

int sum(std::vector<int> inputVector){
    int sum = 0;
    for (int i = 0; i < inputVector.size(); i++){
        sum += inputVector[i];
    }
    return sum;
}

std::vector<char> stringToVec(std::string inputString){
    std::vector<char> stringVector;
    for (int i = 0; i < inputString.size(); i++){
        stringVector.push_back(inputString[i]);
    }
    return stringVector;
}

std::vector<int> reverse(std::vector<int> inputVector){
    std::vector<int> reversedVector;
    for (int i = 0; i < inputVector.size(); i++){
        reversedVector.insert(reversedVector.begin(), inputVector[i]);
    }
    return reversedVector;
}

int main(int argc, const char * argv[]) {
    std::vector<int> sampleVector = {1, 2, 3, 4, 5};
    
    // Sum the integers in the vector defined above
    int vectorSum = sum(sampleVector);
    
    // Assert the sample vector sum is the expected value of 15
    assert(vectorSum == 15);
    std::cout << "The sample vector sum is: " << vectorSum << std::endl;
    
    // Convert a string to a vector of characters
    std::string sampleString = "Test";
    std::vector<char> convertedVector = stringToVec(sampleString);
    
    // Assert that the generated vector is the same length of the input string and the character order is the same
    assert(convertedVector.size() == sampleString.size());
    assert(convertedVector[0] == 'T' && convertedVector[1] == 'e' && convertedVector[2] == 's' && convertedVector[3] == 't');
    
    std::cout << "We converted the sample string " << "'" << sampleString << "'" << " to the vector: {";
    for (int i = 0; i < convertedVector.size(); i++){
        std::cout << "'" << convertedVector[i] << "'";
        if (i < convertedVector.size() - 1){
            std::cout << ", ";
        }
    }
    std::cout << "}" << std::endl;
    
    std::vector<int> reversedVector = reverse(sampleVector);
    
    assert(reversedVector.size() == sampleVector.size());
    assert(reversedVector[0] == 5 && reversedVector[1] == 4 && reversedVector[2] == 3 && reversedVector[3] == 2 && reversedVector[4] == 1);
    
    std::cout << "We reversed the sample vector: " << "{";
    for (int i = 0; i < sampleVector.size(); i++){
        std::cout << sampleVector[i];
        if (i < sampleVector.size() - 1){
            std::cout << ", ";
        }
    }
    std::cout << "}" << " to " << "{";
    for (int i = 0; i < reversedVector.size(); i++){
        std::cout << reversedVector[i];
        if (i < reversedVector.size() - 1){
            std::cout << ", ";
        }
    }
    std::cout << "}" << std::endl;
    
    return 0;
}
