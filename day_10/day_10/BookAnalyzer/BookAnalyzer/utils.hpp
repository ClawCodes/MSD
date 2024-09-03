//
//  utils.hpp
//  BookAnalyzer
//
//  Created by Christopher Lawton on 9/1/24.
//

#ifndef utils_hpp
#define utils_hpp

#include <string>
#include <fstream>

std::vector<std::string> getDirFileNames(std::string path = ".");

std::string getFileName(std::filesystem::path fileName);

bool containsString (std::string target, std::vector<std::string> collection);

std::vector<std::string> readFile(std::string fileName);

std::string strip(std::string rawString);

std::string joinText(std::vector<std::string> inputText, char delim = ' ');

std::vector<std::string> sliceWordVec(std::vector<std::string> &input, int startIdx, int endIdx);

#endif /* utils_hpp */
