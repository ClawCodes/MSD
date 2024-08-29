//
//  deck.h
//  Cards
//
//  Created by Christopher Lawton on 8/27/24.
//

#ifndef deck_h
#define deck_h

#include <iostream>
#include <vector>
#include <cstdlib>
#include "card.h"

enum DeckType{
    STRIPPED,
    STANDARD
};

struct Deck{
    DeckType type;
    std::vector<Card> cards;
    
    /// <#Description#> - Member function to get the human readable form of DeckType
    /// returns - string of 'standard 'or 'stripped' depeding of type value
    std::string getTypeName(){
        if (type == STANDARD)
            return "standard";
        else
            return "stripped";
    }
    /// <#Description#> - Member function fo get the deck representation as a string containing the deck type and a sample of the set of card representations
    void printDeck(){
        std::string cardRepr;
        if (cards.size() == 0)
            cardRepr = "NO CARDS IN DECK";
        else if (cards.size() == 1)
            cardRepr = cards[0].getCardRepr();
        else
            cardRepr = cards[0].getCardRepr() + "..." + cards[cards.size() - 1].getCardRepr();
        std::cout << "Deck{Type: " << getTypeName() << ", Count: " << cards.size() << ", Cards: " << cardRepr << std::endl;
    }
    void shuffleDeck(){
        
    }
};

Deck createDeck(std::vector<CardRank> excludeRanks = {});

void shuffleDeck(Deck &deck);

#endif /* deck_h */
