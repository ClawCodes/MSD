//
//  hand.h
//  Poker
//
//  Created by Christopher Lawton on 8/28/24.
//

#ifndef hand_h
#define hand_h

#include <algorithm>
#include <string>
#include <cassert>
#include <map>
#include "deck.h"

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

struct Hand{
//    Card cards[5];
    std::vector<Card> cards;
    
    /// <#Description#> Determine if the hand is a flush
    bool isFlush(){
        std::string suit = cards[0].suit;
        for(int i = 1; i < cards.size(); i++){
            if (suit != cards[i].suit)
                return false;
        }
        return true;
    }
    /// <#Description#> Determine if the hand is a stright
    bool isStraight(){
        // First sort cards by rank in ascending order
        sortHand(cards);
        
        // If an ace exists and the following card is not two
        // then we can consider the ace high
        if (cards[0].rank == ACE && cards[1].rank > 2){
            CardRank* aceRank = &cards[0].rank;
            *aceRank = ACEHIGH;
            sortHand(cards);
        }
        
        CardRank previous = cards[0].rank;
        for (int i = 1; i < cards.size(); i++){
            if (previous + 1 != cards[i].rank)
                return false;
            else
                previous = cards[i].rank;
        }
        return true;
    }
    /// <#Description#> Determine if the hand is a stright flush
    bool isStraightFlush(){
        return isFlush() && isStraight();
    }
    /// <#Description#> Determine if the hand is a royal flush
    bool isRoyalFlush(){
        sortHand(cards);
        return cards[0].rank == ACE && cards[1].rank == TEN && isStraightFlush();
    }
    bool isFullHouse(){
        std::map<CardRank, int> rankCounter;
        for (Card card : cards){
            CardRank rank = card.rank;
            if (rankCounter.count(rank))
                rankCounter[rank] += 1;
            // If we already have 2 suits captured and found a new one then it's not a fullhouse
            else if (rankCounter.size() == 2)
                return false;
            else
                rankCounter[rank] = 1;
        }
        int firstRankCount = rankCounter.begin()->second;
        // At this point there are only two suits
        return firstRankCount == 2 || firstRankCount == 3;
    }
};

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

#endif /* hand_h */
