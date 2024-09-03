//
//  main.cpp
//  BookAnalyzer
//
//  Created by Christopher Lawton on 8/30/24.
//

#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include "utils.hpp"
#include "test_book.hpp"
#include "test_utils.h"
#include "book.hpp"

namespace fs = std::filesystem;

// Get parent dir of main to use for absolute path creation
const fs::path parentDir = fs::path(__FILE__).parent_path();

void runTests(){
    testGetFileNames();
    testGetFileName();
    testStrip();
    testReadFile();
    testExtractLine();
    testJoinTest();
    testSliceWordVec();
    testExtractLine();
    testGetCharCount();
    testFindBookStartIdx();
    testFindBookEndIdx();
    std::cout << "All tests passed!" << std::endl;
}

void listBooks(std::vector<std::string> books){
    for (std::string book : books){
        std::cout << "\t" << book << std::endl;
    }
}

int main(int argc, const char * argv[]) {
    // tests
    runTests();
    
    std::vector<std::string> availableBooks = getDirFileNames("books");
    
    // Ensure user entered both a valid book name and search word
    if (argc < 2){
        std::cout << "Please enter a book name from the following choices:" << std::endl;
        listBooks(availableBooks);
        exit(1);
    }
    std::string const userBook = argv[1];
    
    if (!(containsString(userBook, availableBooks))){
        std::cout << "The entered book " << userBook << " is not available. Please choose a book from the following:" << std::endl;
        listBooks(availableBooks);
    }
    
    if (argc < 3){
        std::cout << "Please enter a word to serach for in " <<  userBook << "." << std::endl;
        exit(1);
    };
    
    std::string searchWord = argv[2];
    fs::path bookPath = parentDir / fs::path("books/" + userBook + ".txt");
    
    Book book = createBook(bookPath);
    
    std::cout << "Statistics for \"" << book.tile << "\" by " << book.author << ":" << std::endl;
    std::cout << "Number of Words: " << book.wordCount() << std::endl;
    std::cout << "Number of Characters: " << book.characterCount() << std::endl;
    std::cout << "Shortest word: " << book.shortestWord() << std::endl;
    std::cout << "Longest word: " << book.longestWord() << std::endl << std::endl;
    book.findWordOccurance(searchWord);
    
    return 0;
}
