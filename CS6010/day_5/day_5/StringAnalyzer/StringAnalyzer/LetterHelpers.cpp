//
//  LetterHelpers.cpp
//  StringAnalyzer
//
//  Created by Christopher Lawton on 8/26/24.
//

#include "LetterHelpers.hpp"
#include <string>

/// <#Description#> Check if a given character is a sentence terminator
/// - Parameter c: character to check
bool IsTerminator(char c){
    return (c == '.' || c == '?' || c == '!');
}

/// <#Description#> Check if a given character is punctuation
/// - Parameter c: character to check
bool IsPunctuation(char c){
    return (c == ',' || IsTerminator(c));
}

/// <#Description#> Check if a given character is a vowel
/// - Parameter c: character to check
bool IsVowel(char c){
    char cLower = tolower(c);
    return (cLower == 'a' || cLower == 'e' || cLower == 'i' || cLower == 'o' || cLower == 'u' || cLower == 'y');
}

/// <#Description#> Check if a given character is a space
/// - Parameter c: character to check
bool IsSpace(char c){
    return (c == ' ');
}

/// <#Description#>  Count the number of consanants in a given string
/// - Parameter c:The string to count the number of consonants with
bool IsConsonant(char c){
    return (!(IsPunctuation(c) || IsVowel(c) || IsSpace(c)));
}
