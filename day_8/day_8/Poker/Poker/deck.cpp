//
//  deck.cpp
//  Poker
//
//  Created by Christopher Lawton on 8/28/24.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include "deck.h"

/**
 * Create a Deck instance of a standard (52) or stripped (less than 52) deck of cards
 *
 * @param excludeRanks A vecotor of CardRank values to exclude to generate a stripped deck. A standard deck will be generated if not provided.
 * @return Instance of Deck
 */
Deck createDeck(std::vector<CardRank> excludeRanks){
    // Get all ranks to construct deck with
    std::vector<CardRank> includedRanks;
    for (CardRank rank : allRanks){
        auto itfound = std::find(excludeRanks.begin(), excludeRanks.end(), rank);
        // Only push rank to includedRanks if the rank is not found in excludeRanks
        if (itfound == excludeRanks.end()){
            includedRanks.push_back(rank);
        }
    };
    // Construct deck
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

void shuffleDeck(Deck &deck){
    for (int i = 0; i < deck.cards.size(); i++){
        // Seed pseudorandom generator each iteration
        std::srand(i + int(time(NULL)));
        // Get random index to swap with ith index
        int j = rand() % deck.cards.size();
        
        std::cout << "SWAP:\n" << i << "<->" << j << "\n";
        
        Card temp = deck.cards[i];
        Card* jCard = &deck.cards[j];
        Card* iCard = &deck.cards[i];
        
        iCard = &temp;
        jCard = iCard;
    }
}
    
