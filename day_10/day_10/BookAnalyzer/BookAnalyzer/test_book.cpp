//
//  test_book.cpp
//  BookAnalyzer
//
//  Created by Christopher Lawton on 9/2/24.
//

#include <fstream>
#include <cassert>
#include "test_book.hpp"
#include "utils.hpp"
#include "book.hpp"

namespace fs = std::filesystem;

const fs::path parentDir = fs::path(__FILE__).parent_path();

void testExtractLine(){
    // Arrange
    std::string start;
    std::string end;
    std::vector<std::string> input;
    std::string expectedMatch;
    std::string actual;
    
    // Test extraction with simple case
    start = "Title:";
    end = "another";
    input = {"test", "line", "Title:", "text", "to", "extract", "another", "line."};
    expectedMatch = "text to extract";
    
    actual = extractLine(start, end, input);
    
    assert(actual == expectedMatch);
    
    // Test extract no match to start word
    start = "Title:";
    end = "match";
    input = {"test", "nothin", "to", "match"};
    actual = extractLine(start, end, input);
    
    assert(actual.empty());
    
    // Test extract no match end word
    start = "Title:";
    end = "NOT A MATCH";
    input = {"Title:", "test", "nothin", "to", "match"};
    actual = extractLine(start, end, input);
    
    assert(actual.empty());
    
    // Test extract no match exceeds allowed range
    start = "Title:";
    end = "End";
    input = {"Title:"};
    for (int i=0; i<150; i++){
        input.push_back("word");
    }
    input.push_back("End");
    
    actual = extractLine(start, end, input);
    
    assert(actual.empty());
}


void testFindBookStartIdx(){
    std::vector<std::string> testBook = readFile(parentDir / "test_books/book3.txt");
    int expected = 31;
    int actual = findBookStartIdx(testBook);
    assert(actual == expected);
}

void testFindBookEndIdx(){
    std::vector<std::string> testBook = readFile(parentDir / "test_books/book3.txt");
    int expected = 37;
    int actual = findBookEndIdx(testBook);
    assert(actual == expected);
}
