//
//  book.cpp
//  BookAnalyzer
//
//  Created by Christopher Lawton on 9/2/24.
//

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include "book.hpp"
#include "utils.hpp"

namespace fs = std::filesystem;

textMatch extractLine(std::string startWord, std::string endWord, std::vector<std::string> &text, int allowedRange){
    auto itStart = find(text.begin(), text.end(), startWord);
    
    if (itStart == text.end()){
        return textMatch{};
    }
    
    auto itEnd = find(text.begin(), text.end(), endWord);
    
    if (itEnd == text.end()){
        return textMatch{};
    }
    
    int startIdx = int(itStart - text.begin() + 1);
    int endIdx = int(itEnd - text.begin());
    
    if (allowedRange && endIdx - startIdx > allowedRange){
        return textMatch{};
    }
    
    auto joinedText = joinText(sliceWordVec(text, startIdx, endIdx));
    
    
    return textMatch{joinedText, startIdx, endIdx};
}

Book createBook(fs::path &fileName){
    std::vector<std::string> book = readFile(fileName);
    
    // Split book
    
    
    return Book{};
}
