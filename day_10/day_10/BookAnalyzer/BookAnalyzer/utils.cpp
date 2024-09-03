//
//  utils.cpp
//  BookAnalyzer
//
//  Created by Christopher Lawton on 9/1/24.
//

#include <string>
#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <sstream>
#include "utils.hpp"

namespace fs = std::filesystem;

// All whitespace characters
const std::string whiteSpace = " \t\n\r\f\v";

std::string strip(std::string rawString){
    size_t firstPos = rawString.find_first_not_of(whiteSpace);
    
    // No matches found (i.e. rawString is empty or entirely whitespace)
    if (firstPos == std::string::npos){
        return "";
    }
    
    size_t lastPos = rawString.find_last_not_of(whiteSpace);
    return rawString.substr(firstPos, lastPos - firstPos + 1);
}

std::string getFileName(fs::path filePath){
    std::string fileName = filePath.string();
    auto nameStart = fileName.find_last_of("/\\") + 1;
    auto nameEnd = fileName.find_last_of(".");
    
    nameEnd = nameEnd - nameStart;
    fileName = fileName.substr(nameStart, nameEnd);
    
    return fileName;
}


std::vector<std::string> getDirFileNames(std::string path){
    fs::path parentDir = fs::path(__FILE__).parent_path();
    std::vector<std::string> allFiles;
    for (const auto & file : fs::directory_iterator(parentDir / path))
        allFiles.push_back(getFileName(file));
    return allFiles;
}

bool containsString (std::string target, std::vector<std::string> collection){
    return std::find(collection.begin(), collection.end(), target) != collection.end();
}

std::vector<std::string> readFile(std::string fileName){
    std::ifstream inFile(fileName);
    
    if (!inFile.is_open()){
        std::cout << "File " << fileName << " not found." << std::endl;
        exit(1);
    }
    
    
    std::string currentWord;
    std::vector<std::string> allWords;
    while(inFile >> currentWord){
        allWords.push_back(currentWord);
    }
    
    return allWords;
}

std::string joinText(std::vector<std::string> inputText, char delim){
    std::string outString;
    for (const std::string &word : inputText){
        outString += word + delim;
    }
    
    // remove trailing delimiter
    if (!outString.empty()){
        outString.pop_back();
    }
    
    return outString;
}

std::vector<std::string> sliceWordVec(std::vector<std::string> &input, int startIdx, int endIdx){
    auto start = input.begin() + startIdx;
    auto end = input.begin() + endIdx;
    
    std::vector<std::string> slicedVec(endIdx - startIdx);
    
    copy(start, end, slicedVec.begin());
    
    return slicedVec;
}

int getCharCount(std::vector<std::string> wordVec){
    int count = 0;
    for (auto &word : wordVec)
        count += word.size();
    return count;
}
