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

void swapCard(Card &a, Card &b);

void sortHand(std::vector<Card> &cards);

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

Hand dealHand(Deck &deck);

void testHand();

#endif /* hand_h */
