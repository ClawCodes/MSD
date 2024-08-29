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
#include <cassert>
#include "deck.h"
#include "hand.h"


int main(int argc, const char * argv[]) {
    
//    // Create and print the standrd 52 card deck
//    Deck standardDeck = createDeck();
//    standardDeck.printDeck();
//    
//    // Create and print a stripped deck that start at hearts of rank 2 and ends at Queen of Diamonds
//    std::vector<CardRank> ignoreRanks = {ACE, KING};
//    Deck strippedDeck = createDeck(ignoreRanks);
//    strippedDeck.printDeck();
    
    // Testing
//    std::vector<CardRank> ignoreRanks = {ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN};
//    Deck strippedDeck = createDeck(ignoreRanks);
//    
//    std::cout << "ORIGINAL:\n";
//    for (Card c : strippedDeck.cards)
//        std::cout << c.getCardRepr() << std::endl;
//    
//    std::cout << "\nSHUFFLED:\n";
//    shuffleDeck(strippedDeck);
//    for (Card c : strippedDeck.cards)
//        std::cout << c.getCardRepr() << std::endl;
    
    testHand();
    
    return 0;
}
