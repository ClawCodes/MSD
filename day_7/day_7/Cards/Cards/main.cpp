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


/**
 * Create a Deck instance of a standard (52) or stripped (less than 52) deck of cards
 *
 * @param excludeRanks A vecotor of CardRank values to exclude to generate a stripped deck. A standard deck will be generated if not provided.
 * @return Instance of Deck
 */
Deck getDeck(std::vector<CardRank> excludeRanks = {}){
    std::vector<CardRank> includedRanks;
    for (CardRank rank : allRanks){
        auto itfound = std::find(excludeRanks.begin(), excludeRanks.end(), rank);
        // Only push rank to includedRanks if the rank is not found in excludeRanks
        if (itfound == excludeRanks.end()){
            includedRanks.push_back(rank);
        }
    };
    std::vector<Card> cards;
    for (std::string suit : allSuits){
        for (CardRank rank: includedRanks)
            cards.push_back(Card{rank, suit});
    }
    
    DeckType deckType;
    if (excludeRanks.size() == 0)
        deckType = STANDARD;
    else
        deckType = STRIPPED;
    
    return Deck{deckType, cards};
};


int main(int argc, const char * argv[]) {
    
    // Create and print the standrd 52 card deck
    Deck standardDeck = getDeck();
    standardDeck.printDeck();
    
    // Create and print a stripped deck that start at hearts of rank 2 and ends at Queen of Diamonds
    std::vector<CardRank> ignoreRanks = {ACE, KING};
    Deck strippedDeck = getDeck(ignoreRanks);
    strippedDeck.printDeck();
    
    return 0;
}
