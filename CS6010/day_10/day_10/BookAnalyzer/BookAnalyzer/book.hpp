//
//  book.hpp
//  BookAnalyzer
//
//  Created by Christopher Lawton on 9/2/24.
//

#ifndef book_hpp
#define book_hpp

#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include "utils.hpp"

namespace fs = std::filesystem;

struct textMatch{
    std::string match;
    int startIdx;
    int endIdx;
};

struct Book{
    std::string tile;
    std::string author;
    std::vector<std::string> text;
    
    void findWordOccurance(std::string searchWord){
        std::string previous = "";
        std::vector<std::string> matchStatements;
        for (int i = 0; i<text.size(); i++){
            if (text[i] == searchWord){
                std::string end;
                // Handle when i is the last element in the array
                if (i == text.size() - 1){
                    end = "";
                } else {
                    // Retrieve the following word to add it to the context of the match
                    end = text[i + 1];
                }
                int idxPercent = double(i) / text.size() * 100;
                
                std::string statement = std::to_string(idxPercent) + "%: \"" + previous + ' ' + text[i] + ' ' + end + '"';
                matchStatements.push_back(statement);
            }
            previous = text[i];
        }
        std::string suffix;
        if (matchStatements.empty()){
            suffix = " 0 times";
        } else{
            suffix = std::to_string(matchStatements.size()) + " times:";
        }
        std::cout << "The word \"" + searchWord + "\" appears " + suffix << std::endl;
        for (const std::string &statement : matchStatements){
            std::cout << "\tat " << statement << std::endl;
        }
        std::cout << std::endl;
    }
    std::string shortestWord(){
        std::string shortest = text[0];
        for (const std::string &word : text){
            if (word.size() < shortest.size())
                shortest = word;
        }
        return shortest;
    }
    
    std::string longestWord(){
        std::string longest = text[0];
        for (const std::string &word : text){
            if (word.size() > longest.size())
                longest = word;
        }

        return longest;
    }
    
    size_t wordCount(){
        return text.size();
    }
    
    int characterCount(){
        return getCharCount(text);
    }
};

Book createBook(fs::path &fileName);

std::vector<std::string> extractHeaderInfo(std::string book);

std::string extractLine(std::string startWord, std::string endWord, std::vector<std::string> &text, int allowedRange = 100);

int findBookStartIdx(std::vector<std::string> book);

int findBookEndIdx(std::vector<std::string> book);

#endif /* book_hpp */
