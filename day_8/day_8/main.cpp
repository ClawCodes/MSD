//
//  main.cpp
//  Cards
//
//  Created by Christopher Lawton on 8/27/24.
//

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include "deck.h"


int main(int argc, const char * argv[]) {
    
    // Create and print the standrd 52 card deck
    Deck standardDeck = createDeck();
    standardDeck.printDeck();
    
    // Create and print a stripped deck that start at hearts of rank 2 and ends at Queen of Diamonds
    std::vector<CardRank> ignoreRanks = {ACE, KING};
    Deck strippedDeck = createDeck(ignoreRanks);
    strippedDeck.printDeck();
    
    return 0;
}
