//
//  WordHelpers.cpp
//  StringAnalyzer
//
//  Created by Christopher Lawton on 8/26/24.
//

#include "LetterHelpers.hpp"
#include "WordHelpers.hpp"
#include <string>

/// <#Description#> Count the number of words in a given string
/// - Parameter s: The string to count the number of words with
int NumWords(std::string s){
    int wordCount = 0;
    char previous = ' ';
    for (int i=0; i < s.length(); i++){
        char character = s[i];
        if ((IsSpace(character) || IsTerminator(character)) && !(IsSpace(previous) || IsTerminator(previous))){
                wordCount += 1;
        }
        previous = character;
    }
    return wordCount;
}

/// <#Description#> Count the number of sentences in a given string
/// - Parameter s: The string to search for sentences in
int NumSentences(std::string s){
    int sentenceCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsTerminator(s[i])){
            sentenceCount += 1;
        }
    }
    return sentenceCount;
}


/// <#Description#> Get the number of vowels in a word
/// - Parameter s: sentence to count vowels with
int NumVowels(std::string s){
    int vowelCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsVowel(s[i])){
            vowelCount += 1;
        }
    }
    return vowelCount;
}

/// <#Description#> Get the number of consonants in a word
/// - Parameter s: sentence to count consants with
int NumConsonants(std::string s){
    int consonantCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsConsonant(s[i])){
            consonantCount += 1;
        }
    }
    return consonantCount;
    
}

/// <#Description#> Calculate the average word length in a given sentence
/// - Parameter s: sentence to compute average with
double AverageWordLength(std::string s){
    double numLetters = 0;
    double numWords = NumWords(s);
    for (int i = 0; i < s.length(); i++){
        if (IsConsonant(s[i]) || IsVowel(s[i])){
            numLetters += 1;
        }
    }
    return numLetters / numWords;
}

/// Calculate the average vowels per word in a sentence
/// - Parameter s: sentence to compute average with
double AverageVowelsPerWord(std::string s){
    double numWords = NumWords(s);
    double numVowles = NumVowels(s);
    return numVowles / numWords;
}
