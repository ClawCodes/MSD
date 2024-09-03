//
//  book.hpp
//  BookAnalyzer
//
//  Created by Christopher Lawton on 9/2/24.
//

#ifndef book_hpp
#define book_hpp

#include <string>
#include <vector>
#include <fstream>

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
};

Book createBook(fs::path &fileName);

std::vector<std::string> extractHeaderInfo(std::string book);

textMatch extractLine(std::string startWord, std::string endWord, std::vector<std::string> &text, int allowedRange = 100);

#endif /* book_hpp */
