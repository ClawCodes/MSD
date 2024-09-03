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

std::string extractLine(std::string startWord, std::string endWord, std::vector<std::string> &text, int allowedRange){
    auto itStart = find(text.begin(), text.end(), startWord);
    
    std::string result = "";
    if (itStart == text.end()){
        return result;
    }
    
    auto itEnd = find(text.begin(), text.end(), endWord);
    
    if (itEnd == text.end()){
        return result;
    }
    
    int startIdx = int(itStart - text.begin() + 1);
    int endIdx = int(itEnd - text.begin());
    
    if (allowedRange && endIdx - startIdx > allowedRange){
        return result;
    }
    
    result = joinText(sliceWordVec(text, startIdx, endIdx));
    
    return result;
}

int findBookStartIdx(std::vector<std::string> book){
    const std::string headerSeq = "***";
    std::string previous;
    bool atStartHeader = false;
    for (int i = 0; i < book.size(); i++){
        if (atStartHeader && book[i] == headerSeq){
            return i + 1;
        }
        if (book[i] == "START" && previous == headerSeq){
            atStartHeader = true;
        }
        previous = book[i];
    }
    return -1;
}


int findBookEndIdx(std::vector<std::string> book){
    std::string previous;
    for (int i = 0; i < book.size(); i++){
        if (book[i] == "END" && previous == "***"){
            return i;
        }
        previous = book[i];
    }
    return -1;
}


Book createBook(fs::path &fileName){
    std::vector<std::string> book = readFile(fileName);
    
    std::string title = extractLine("Title:", "Author:", book);
    if (title.empty())
        title = "unknown";
    
    std::string author = extractLine("Author:", "Release", book);
    if (author.empty())
        author = "unknown";
    
    int bookStart = findBookStartIdx(book);

    int bookEnd = findBookEndIdx(book);
    
    std::vector<std::string> strippedBook = sliceWordVec(book, bookStart, bookEnd);
    
    return Book{
        title,
        author,
        strippedBook,
        book
    };
}
