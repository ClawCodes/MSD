//
//  main.cpp
//  MapAndSet
//
//  Created by Christopher Lawton on 9/18/24.
//

#include <iostream>
#include <fstream>
#include <set>
#include <map>

int main(int argc, const char * argv[]) {
    
    if(argc < 2){
        std::cout << "specify a file" << std::endl;
        return 1;
    }

    // Step 1: Count unique words in a file
    // Step 2: Get occurance of words
    std::map<std::string, int> counter;
    std::ifstream file(argv[1]);
    
    std::string word;
    std::set<std::string> words;
    while(file >> word){
        words.insert(word);
//        counter[word]++; // Short hand
        if(counter.find(word) == counter.end()){
            counter[word] = 1;
        } else{
            counter[word]++;
        }
    }

    std::cout << words.size() << std::endl;
    
    for(const auto& pair : counter){
        std::cout << pair.first << ' ' << pair.second << std::endl;
    }
    
    
    return 0;
}
