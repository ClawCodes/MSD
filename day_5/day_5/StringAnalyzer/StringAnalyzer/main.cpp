//
//  main.cpp
//  StringAnalyzer
//
//  Created by Christopher Lawton on 8/23/24.
//

#include <iostream>
#include <string>
using namespace std;

char period = '.';
char question = '?';
char exclamation = '!';

bool IsTerminator(char c){
    if (c == period || c == question || c == exclamation) {
        return true;
    } else{
        return false;
    }
}

bool IsPunctuation(char c){
    if (c == ',' || IsTerminator(c)){
        return true;
    } else {
        return false;
    }
}

bool IsVowel(char c){
    char cLower = tolower(c);
    if (cLower == 'a' || cLower == 'e' || cLower == 'i' || cLower == 'o' || cLower == 'u' || cLower == 'y'){
        return true;
    } else {
        return false;
    }
}

bool IsSpace(char c){
    if (c == ' '){
        return true;
    } else {
        return false;
    }
}

bool IsConsonant(char c){
    if (!(IsPunctuation(c) || IsVowel(c) || IsSpace(c))){
        return true;
    } else {
        return false;
    }
}

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

int NumSentences(string s){
    int sCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsTerminator(s[i])){
            sCount += 1;
        }
    }
    return sCount;
}


int NumVowels(string s){
    int vCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsVowel(s[i])){
            vCount += 1;
        }
    }
    return vCount;
}

int NumConsonants(string s){
    int cCount = 0;
    for (int i = 0; i < s.length(); i++){
        if (IsConsonant(s[i])){
            cCount += 1;
        }
    }
    return cCount;
    
}

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
