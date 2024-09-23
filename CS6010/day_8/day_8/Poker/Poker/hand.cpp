//
//  hand.cpp
//  Poker
//
//  Created by Christopher Lawton on 8/29/24.
//

#include <algorithm>
#include <string>
#include <cassert>
#include <map>
#include "hand.h"

void swapCard(Card &a, Card &b){
    Card temp = b;
    b = a;
    a = temp;
}

void sortHand(std::vector<Card> &cards){
    int min;
    for (int i = 0; i < cards.size(); i++){
        min = i;
        for (int j = i+1; j < cards.size(); j++){
            if (cards[min].rank > cards[j].rank){
                min = j;
                }
            }
        swapCard(cards[min], cards[i]);
    }
}

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


// Function to run assertions against member functions of the Hand struct
void testHand(){
    // Assert hand is flush
    Hand flush = Hand{
        {
            Card{ACE, clubs},
            Card{KING, clubs},
            Card{QUEEN, clubs},
            Card{JACK, clubs},
            Card{TEN, clubs}
        }
    };
    assert(flush.isFlush());
    
    // Assert hand is not flush
    Hand notFlush = Hand{
        {
            Card{ACE, clubs},
            Card{KING, hearts},
            Card{QUEEN, clubs},
            Card{JACK, clubs},
            Card{TEN, clubs}
        }
    };
    assert(!(notFlush.isFlush()));

    // Assert hand is stright
    Hand straight = Hand{
        {
            Card{TWO, hearts},
            Card{ACE, clubs},
            Card{FOUR, diamonds},
            Card{THREE, hearts},
            Card{FIVE, hearts}
        }
    };
    
    assert(straight.isStraight());
    
    // Assert hand is not stright
    Hand notStraight = Hand{
        {
            Card{KING, hearts},
            Card{ACE, clubs},
            Card{FOUR, diamonds},
            Card{THREE, hearts},
            Card{FIVE, hearts}
        }
    };
    
    assert(!(notStraight.isStraight()));
    
    // Assert hand is stright with Ace high
    Hand straightAceHigh = Hand{
        {
            Card{TEN, hearts},
            Card{ACE, clubs},
            Card{JACK, diamonds},
            Card{KING, hearts},
            Card{QUEEN, hearts}
        }
    };
    
    assert(straight.isStraight());
    
    // Assert hand is stright flush
    Hand straightFlush = Hand{
        {
            Card{TWO, hearts},
            Card{ACE, hearts},
            Card{FOUR, hearts},
            Card{THREE, hearts},
            Card{FIVE, hearts}
        }
    };
    
    assert(straightFlush.isStraightFlush());
    
    // Assert hand is NOT stright flush
    Hand notStraightFlush = Hand{
        {
            Card{TWO, hearts},
            Card{ACE, hearts},
            Card{FOUR, hearts},
            Card{THREE, clubs},
            Card{FIVE, hearts}
        }
    };
    
    assert(!(notStraightFlush.isStraightFlush()));
    
    // Assert hand is a royal flush
    Hand royalFlush = Hand{
        {
            Card{TEN, hearts},
            Card{JACK, hearts},
            Card{QUEEN, hearts},
            Card{KING, hearts},
            Card{ACE, hearts},
        }
    };
    
    assert(royalFlush.isRoyalFlush());
    
    // Assert hand is NOT a royal flush
    Hand notRoyalFlush = Hand{
        {
            Card{JACK, clubs},
            Card{JACK, hearts},
            Card{QUEEN, hearts},
            Card{KING, hearts},
            Card{ACE, hearts},
        }
    };
    
    assert((!(notRoyalFlush.isRoyalFlush())));
    
    // Assert hand is a full house
    Hand fullHouse = Hand{
        {
            Card{TEN, hearts},
            Card{TEN, clubs},
            Card{TEN, diamonds},
            Card{KING, clubs},
            Card{KING, hearts}
        }
    };
    
    assert(fullHouse.isFullHouse());
    
    // Assert hand is a full house
    Hand notFullHouse = Hand{
        {
            Card{TEN, hearts},
            Card{JACK, hearts},
            Card{QUEEN, diamonds},
            Card{KING, clubs},
            Card{THREE, clubs}
        }
    };
    
    assert(!(notFullHouse.isFullHouse()));
}
