//
//  WordHelpers.hpp
//  StringAnalyzer
//
//  Created by Christopher Lawton on 8/26/24.
//

#ifndef WordHelpers_hpp
#define WordHelpers_hpp

#include <string>

int NumWords(std::string s);

int NumSentences(std::string s);

int NumVowels(std::string s);

int NumConsonants(std::string s);

double AverageWordLength(std::string s);

double AverageVowelsPerWord(std::string s);

#endif /* WordHelpers_hpp */
