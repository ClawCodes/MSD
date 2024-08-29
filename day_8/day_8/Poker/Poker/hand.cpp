//
//  hand.cpp
//  Poker
//
//  Created by Christopher Lawton on 8/28/24.
//

#include <stdio.h>
#include <cstdlib>
#include <vector>
#include "hand.h"

Hand dealHand(Deck &deck){
    // Seed pseudorandom generator
    std::srand(unsigned(time(NULL)));
    std::vector<Card> sampleHand;
    for (int i = 0; i < 5; i++){
        int sampleIndex = rand() % deck.cards.size();
        sampleHand.push_back(deck.cards[sampleIndex]);
    }
    return Hand{sampleHand};
}
