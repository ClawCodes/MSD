//
//  test_utils.cpp
//  BookAnalyzer
//
//  Created by Christopher Lawton on 9/2/24.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <cassert>
#include "utils.hpp"

namespace fs = std::filesystem;

void testStrip(){
    // Arrange
    std::string inputString;
    std::string expected;
    std::string actual;
    
    // Beginning whitespace removed
    inputString = " \ntest";
    expected = "test";
    actual = strip(inputString);
    assert(actual == expected);
    
    // End whitespace removed
    inputString = "test2\r\n";
    expected = "test2";
    actual = strip(inputString);
    assert(actual == expected);
    
    // Start and end whitespace removed
    inputString = " test3\r\n";
    expected = "test3";
    actual = strip(inputString);
    assert(actual == expected);
    
    // Empty string
    inputString = "";
    expected = "";
    actual = strip(inputString);
    assert(actual == expected);
    
    // Middle white space remains
    inputString = " This is a test   \r\n";
    expected = "This is a test";
    actual = strip(inputString);
    assert(actual == expected);
    
    // Strip string with no whitespace
    inputString = "test";
    expected = "test";
    actual = strip(inputString);
    assert(actual == expected);
    
    // Only whitespace
    inputString = "\r \n\n  ";
    expected = "";
    actual = strip(inputString);
    assert(actual == expected);
}

void testGetFileName(){
    fs::path testPath = fs::path("./some_dir/my_file.txt");
    std::string const expected = "my_file";
    std::string actual = getFileName(testPath);
    assert(actual == expected);
}

void testGetFileNames(){
    std::vector<std::string> bookNames = getDirFileNames("books");
    std::vector<std::string> expectedFileNames = {
        "ThePictureOfDorianGray",
        "TheChessmenOfMars",
        "DonQuijote(Spanish)"
    };
    assert(bookNames.size() == expectedFileNames.size());
    assert(bookNames[0] == expectedFileNames[0]);
    assert(bookNames[1] == expectedFileNames[1]);
    assert(bookNames[2] == expectedFileNames[2]);
}

void testContainsString(){
    std::vector<std::string> collection = {
        "one", "two", "three"
    };
    std::string const target = "two";
    
    assert(containsString(target, collection));
    
}

void testReadFile(){
    fs::path fileName;
    std::vector<std::string> result;
    // test with test book1
    fileName = fs::path(__FILE__).parent_path() / "test_books/book1.txt";
    result = readFile(fileName);
    
    assert(result.size() == 37); // there are 37 words in the test book1
    assert(result[0] == "before");
    assert(result[36] == "Abela");
    
    // test with test book2
    fileName = fs::path(__FILE__).parent_path() / "test_books/book2.txt";
    result = readFile(fileName);
    
    assert(result.size() == 9); // there are 9 words in the test book2
    assert(result[0] == "Title:");
    assert(result[8] == "Wilde");
}


void testJoinTest(){
    std::vector<std::string> inputText;
    std::string expected;
    std::string actual;
    
    // Join with default delimeter
    inputText = {
        "one", "two", "three", "four"
    };
    expected = "one two three four";
    actual = joinText(inputText);
    
    assert(actual == expected);
    
    // Join with passed in delimeter
    inputText = {
        "one", "two", "three", "four"
    };
    expected = "one,two,three,four";
    actual = joinText(inputText, ',');
    
    assert(actual == expected);
}

void testSliceWordVec(){
    std::vector<std::string> input = {"one", "two", "three", "four", "five"};
    int start = 1;
    int end = 4;
    std::vector<std::string> expected = {"two", "three", "four"};
    auto actual = sliceWordVec(input, start, end);
    
    assert(actual.size() == 3);
    assert(input.size() == 5);
    assert(actual[0] == expected[0]);
    assert(actual[1] == expected[1]);
    assert(actual[2] == expected[2]);
}
