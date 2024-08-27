//
//  main.cpp
//  StringAnalyzer
//
//  Created by Christopher Lawton on 8/23/24.
//

#include <iostream>
#include <string>
#include "WordHelpers.hpp"
using namespace std;


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
