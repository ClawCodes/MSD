//
//  main.cpp
//  StringAnalyzer
//
//  Created by Christopher Lawton on 8/23/24.
//

#include <iostream>
#include <string>
using namespace std;


/// <#Description#> Check if a given character is a sentence terminator
/// - Parameter c: character to check
bool IsTerminator(char c){
    if (c == '.' || c == '?' || c == '!') {
        return true;
    } else{
        return false;
    }
}

/// <#Description#> Check if a given character is punctuation
/// - Parameter c: character to check
bool IsPunctuation(char c){
    if (c == ',' || IsTerminator(c)){
        return true;
    } else {
        return false;
    }
}

/// <#Description#> Check if a given character is a vowel
/// - Parameter c: character to check
bool IsVowel(char c){
    char cLower = tolower(c);
    if (cLower == 'a' || cLower == 'e' || cLower == 'i' || cLower == 'o' || cLower == 'u' || cLower == 'y'){
        return true;
    } else {
        return false;
    }
}

/// <#Description#> Check if a given character is a space
/// - Parameter c: character to check
bool IsSpace(char c){
    if (c == ' '){
        return true;
    } else {
        return false;
    }
}

/// <#Description#>  Count the number of consanants in a given string
/// - Parameter c:The string to count the number of consonants with
bool IsConsonant(char c){
    if (!(IsPunctuation(c) || IsVowel(c) || IsSpace(c))){
        return true;
    } else {
        return false;
    }
}

/// <#Description#> Count the number of words in a given string
/// - Parameter s: The string to count the number of words with
int NumWords(string s){
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
int NumSentences(string s){
    int sCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsTerminator(s[i])){
            sCount += 1;
        }
    }
    return sCount;
}


/// <#Description#> Get the number of vowels in a word
/// - Parameter s: sentence to count vowels with
int NumVowels(string s){
    int vCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsVowel(s[i])){
            vCount += 1;
        }
    }
    return vCount;
}

/// <#Description#> Get the number of consonants in a word
/// - Parameter s: sentence to count consants with
int NumConsonants(string s){
    int cCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsConsonant(s[i])){
            cCount += 1;
        }
    }
    return cCount;
    
}

/// <#Description#> Calculate the average word length in a given sentence
/// - Parameter s: sentence to compute average with
double AverageWordLength(string s){
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
double AverageVowelsPerWord(string s){
    double numWords = NumWords(s);
    double numVowles = NumVowels(s);
    return numVowles / numWords;
}

int main(int argc, const char * argv[]) {
    
    // Get sentences from user
    string userInput;
    
    while(userInput != "done"){
        cout << "Please enter a sentence or 'done' to terminate the prompt" << endl;
        getline(cin, userInput);
        if (userInput != "done"){
            // Print sentence statistics
            cout << "Analysis:" << endl;
            cout << "\tNumber of words: " << NumWords(userInput) << endl;
            cout << "\tNumber of sentences: " << NumSentences(userInput) << endl;
            cout << "\tNumber of vowels: " << NumVowels(userInput) << endl;
            cout << "\tNumber of consonants: " << NumConsonants(userInput) << endl;
            cout << "\tReading level (average word length): " << AverageWordLength(userInput) << endl;
            cout << "\tAverage vowels per word: " << AverageVowelsPerWord(userInput) << endl;
        } else {
            cout << "Goodbye." << endl;
        }
    }
    
    return 0;
}
